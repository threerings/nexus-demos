//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import react.Functions;
import react.RFuture;
import react.Slot;

import com.threerings.nexus.distrib.Keyed;
import com.threerings.nexus.distrib.Nexus;
import com.threerings.nexus.distrib.NexusException;
import com.threerings.nexus.server.SessionLocal;

import com.threerings.reversi.core.chat.ChatMessage;
import com.threerings.reversi.core.game.Board;
import com.threerings.reversi.core.game.Coord;
import com.threerings.reversi.core.game.Factory_GameService;
import com.threerings.reversi.core.game.GameObject;
import com.threerings.reversi.core.game.GameService;
import com.threerings.reversi.core.game.Logic;

/** Manages an individual game of Reversi. */
public class GameManager implements GameService, Keyed {

  public final Integer gameId;
  public final GameObject gameObj;

  public GameManager (Nexus nexus, Integer gameId, Player[] players) {
    this.gameId = gameId;
    _nexus = nexus;

    String[] pnames = new String[players.length];
    for (int ii = 0; ii < players.length; ii++) {
      pnames[ii] = players[ii].nickname;
      _players.put(players[ii], ii);
    }

    // register ourselves as a keyed entity
    Nexus.Context<GameManager> ctx = nexus.registerKeyed(GameManager.class, this);
    // create and register our game object as an anonymous child in our same context
    this.gameObj = new GameObject(pnames, Factory_GameService.createDispatcher(this));
    nexus.register(gameObj, ctx);
  }

  public void shutdown () {
    if (gameObj.getId() == 0) return; // we already shutdown
    _nexus.clear(gameObj);
    _nexus.clearKeyed(GameManager.class, this);
  }

  @Override public Comparable<?> getKey () {
    return gameId;
  }

  @Override public RFuture<Integer> readyToPlay () {
    Player player = SessionLocal.get(Player.class);
    NexusException.require(gameObj.state.get() == GameObject.State.PRE_GAME,
                           "Got readyToPlay, but it's not pre-game", "from", player);
    int idx = index(player);
    NexusException.require(idx != -1, "Got readyToPlay from non-player", "from", player);

    // if this player disconnects before leaving normally, clear them out
    SessionLocal.getSession().onDisconnect().map(Functions.constant(player)).
      connect(playerLeft).holdWeakly();

    // note that they're ready to play and maybe start the game
    _here.add(player);
    if (_here.size() == _players.size()) {
      gameObj.turnHolder.update(0);
      int mid = Board.SIZE/2;
      gameObj.board.put(new Coord(mid-1, mid-1), 0);
      gameObj.board.put(new Coord(mid-1, mid), 1);
      gameObj.board.put(new Coord(mid, mid-1), 1);
      gameObj.board.put(new Coord(mid, mid), 0);
      gameObj.state.update(GameObject.State.IN_PLAY);
    }

    // let the player know their index
    return RFuture.success(idx);
  }

  @Override public void play (Coord coord) {
    // make sure the requester is the current turn holder
    int thIdx = gameObj.turnHolder.get();
    Player player = SessionLocal.get(Player.class);
    NexusException.require(thIdx >= 0 && index(player) == thIdx,
                           "Got play from non-turnholder", "from", player, "at", coord, "th", thIdx);

    // double check that the play is legal (the client also checks)
    NexusException.require(Logic.isLegalPlay(gameObj.board, thIdx, coord),
                           "Got illegal play request", "from", player, "at", coord,
                           "board", gameObj.board);

    // apply the play, flipping the appropriate tiles
    gameObj.board.put(coord, thIdx);
    Logic.applyPlay(gameObj.board, thIdx, coord);

    int nextThIdx = nextTurnHolder(thIdx);
    gameObj.turnHolder.update(nextThIdx);

    if (nextThIdx == thIdx) {
      sendSysMsg(gameObj.players[1-thIdx] + " has no legal moves. Skipping their turn.");

    } else if (nextThIdx == -1) {
      // count up the tiles and declare a winner
      int[] owned = new int[_players.size()];
      for (int owner : gameObj.board.values()) owned[owner]++;
      if (owned[0] == owned[1]) {
        sendSysMsg("It's a tie!");
      } else if (owned[0] > owned[1]) {
        sendSysMsg(gameObj.players[0] + " wins!");
      } else {
        sendSysMsg(gameObj.players[1] + " wins!");
      }
      gameObj.state.update(GameObject.State.GAME_OVER);
    }
  }

  protected int nextTurnHolder (int thIdx) {
    // if the next player can move, they're up
    if (Logic.hasLegalPlays(gameObj.board, 1-thIdx)) return 1-thIdx;
    // otherwise see if the current player can still move
    if (Logic.hasLegalPlays(gameObj.board, thIdx)) return thIdx;
    // otherwise the game is over
    return -1;
  }

  @Override public void chat (String message) {
    String speaker = SessionLocal.get(Player.class).nickname;
    gameObj.onChat.emit(new ChatMessage(speaker, message));
  }

  @Override public void byebye () {
    Player player = SessionLocal.get(Player.class);
    NexusException.require(_players.containsKey(player), "Got byebye from non-player?",
                           "game", gameId, "from", player);
    playerLeft.onEmit(player);
  }

  protected void sendSysMsg (String message) {
      gameObj.onChat.emit(new ChatMessage(null, message));
  }

  protected int index (Player player) {
    Integer idx = _players.get(player);
    return (idx == null) ? -1 : idx;
  }

  protected final Slot<Player> playerLeft = new Slot<Player>() { public void onEmit (Player player) {
    if (_here.remove(player)) sendSysMsg(player.nickname + " left the game.");
    // if all players have left, shut the game down
    if (_here.size() == 0) shutdown();
  }};

  protected final Nexus _nexus;
  protected final Map<Player,Integer> _players = new HashMap<Player,Integer>();
  protected final Set<Player> _here = new HashSet<Player>();
}
