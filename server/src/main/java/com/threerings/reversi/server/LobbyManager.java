//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import react.UnitSlot;
import tripleplay.util.Logger;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.Nexus;
import com.threerings.nexus.distrib.Singleton;
import com.threerings.nexus.server.SessionLocal;
import com.threerings.nexus.util.Callback;

import com.threerings.reversi.core.Factory_LobbyService;
import com.threerings.reversi.core.GameObject;
import com.threerings.reversi.core.LobbyObject;
import com.threerings.reversi.core.LobbyService;

/** Manages the server side of the Reversi lobby. */
public class LobbyManager implements LobbyService, Singleton {

  public LobbyManager (Nexus nexus) {
    _nexus = nexus;
    // register ourselves as a singleton
    nexus.registerSingleton(this);
    // register our lobby object as a child singleton in our same context
    nexus.registerSingleton(_lobj, this);
  }

  public void sendSysMsg (String message) {
    _lobj.onChat.emit(new LobbyObject.ChatMessage(null, message));
  }

  @Override public void hello (Callback<String> callback) {
    // create this player's Player object and assign them a nick
    String nick = "player" + (++_nextPlayerNo);
    SessionLocal.set(Player.class, new Player(_nexus, nick));

    // register a listener on this player's session to learn when they go away
    SessionLocal.getSession().onDisconnect().connect(new UnitSlot() {
      @Override public void onEmit () {
        Player player = SessionLocal.get(Player.class);
        sendSysMsg(player.nickname + " logged off.");
      }
    });

    // let the player know their auto-assigned nickname
    callback.onSuccess(nick);
  }

  @Override public void updateNick (String newnick, Callback<Void> callback) {
    Player player = SessionLocal.get(Player.class);
    String onickname = player.nickname;
    player.nickname = newnick;
    sendSysMsg("<" + onickname + "> is now known as <" + newnick + ">");
    callback.onSuccess(null);
  }

  @Override public void chat (String message) {
    String speaker = SessionLocal.get(Player.class).nickname;
    _lobj.onChat.emit(new LobbyObject.ChatMessage(speaker, message));
  }

  @Override public void play (Callback<Address<GameObject>> callback) {
    Player player = SessionLocal.get(Player.class);
    if (_waiter == player) {
      _log.warning("Got play from already waiting player?", "who", player);
      return;
    }
    if (_waiter != null) {
      GameManager gmgr = new GameManager(_nexus, ++_nextGameId, new Player[] { _waiter, player });
      _waiterCallback.onSuccess(Address.of(gmgr.gameObj));
      callback.onSuccess(Address.of(gmgr.gameObj));
      _waiter = null;
      _waiterCallback = null;
    } else {
      _waiter = player;
      _waiterCallback = callback;
    }
  }

  @Override public void cancel () {
    Player player = SessionLocal.get(Player.class);
    if (_waiter != player) {
      _log.warning("Got cancel from non-waiting player?", "who", player);
      return;
    }
    _waiterCallback.onSuccess(null);
    _waiter = null;
    _waiterCallback = null;
  }

  protected final Logger _log = new Logger("lobby");
  protected final Nexus _nexus;
  protected final LobbyObject _lobj = new LobbyObject(Factory_LobbyService.createDispatcher(this));

  protected int _nextPlayerNo = 0;
  protected int _nextGameId = 0;
  protected Player _waiter;
  protected Callback<Address<GameObject>> _waiterCallback;
}
