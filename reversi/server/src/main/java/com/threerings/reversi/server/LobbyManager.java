//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import react.RFuture;
import react.RPromise;
import react.UnitSlot;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.Nexus;
import com.threerings.nexus.distrib.NexusException;
import com.threerings.nexus.distrib.Singleton;
import com.threerings.nexus.server.SessionLocal;

import com.threerings.reversi.core.chat.ChatMessage;
import com.threerings.reversi.core.game.GameObject;
import com.threerings.reversi.core.lobby.Factory_LobbyService;
import com.threerings.reversi.core.lobby.LobbyObject;
import com.threerings.reversi.core.lobby.LobbyService;

/** Manages the server side of the Reversi lobby. */
public class LobbyManager implements LobbyService, Singleton {

  public LobbyManager (Nexus nexus) {
    _nexus = nexus;
    // register ourselves as a singleton
    Nexus.Context<LobbyManager> ctx = nexus.register(LobbyManager.class, this);
    // register our lobby object as a child singleton in our same context
    nexus.register(LobbyObject.class, _lobj, ctx);
  }

  public void sendSysMsg (String message) {
    _lobj.onChat.emit(new ChatMessage(null, message));
  }

  @Override public RFuture<String> hello () {
    // create this player's Player object and assign them a nick
    final Player player = new Player(_nexus, "player" + (++_nextPlayerNo));
    SessionLocal.set(Player.class, player);

    // register a listener on this player's session to learn when they go away
    SessionLocal.getSession().onDisconnect().connect(new UnitSlot() {
      @Override public void onEmit () {
        sendSysMsg(player.nickname + " logged off.");
      }
    });

    // let the player know their auto-assigned nickname
    sendSysMsg(player.nickname + " logged on.");
    return RFuture.success(player.nickname);
  }

  @Override public RFuture<Void> updateNick (String newnick) {
    Player player = SessionLocal.get(Player.class);
    String onickname = player.nickname;
    player.nickname = newnick;
    sendSysMsg("<" + onickname + "> is now known as <" + newnick + ">");
    return RFuture.success();
  }

  @Override public void chat (String message) {
    String speaker = SessionLocal.get(Player.class).nickname;
    _lobj.onChat.emit(new ChatMessage(speaker, message));
  }

  @Override public RFuture<Address<GameObject>> play () {
    Player player = SessionLocal.get(Player.class);
    NexusException.require(_waiter != player,
                           "Got play from already waiting player?", "who", player);

    // if no one is already waiting, queue this player up as a waiter
    if (_waiter == null) {
      sendSysMsg(player.nickname + " is ready to play.");
      _waiter = player;
      return _waiterResult = RPromise.create();
    }

    GameManager gmgr = new GameManager(_nexus, ++_nextGameId, new Player[] { _waiter, player });
    _waiterResult.succeed(Address.of(gmgr.gameObj));
    _waiterResult = null;
    sendSysMsg(_waiter.nickname + " and " + player.nickname + " have started a game.");
    _waiter = null;
    return RFuture.success(Address.of(gmgr.gameObj));
  }

  @Override public void cancel () {
    Player player = SessionLocal.get(Player.class);
    NexusException.require(_waiter == player,
                           "Got cancel from non-waiting player?", "who", player);

    sendSysMsg(player.nickname + " has decided not to play.");
    _waiterResult.fail(new NexusException("canceled"));
    _waiter = null;
    _waiterResult = null;
  }

  protected final Nexus _nexus;
  protected final LobbyObject _lobj = new LobbyObject(Factory_LobbyService.createDispatcher(this));

  protected int _nextPlayerNo = 0;
  protected int _nextGameId = 0;
  protected Player _waiter;
  protected RPromise<Address<GameObject>> _waiterResult;
}
