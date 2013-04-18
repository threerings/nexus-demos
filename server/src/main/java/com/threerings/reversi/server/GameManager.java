//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import react.Functions;
import react.Slot;

import tripleplay.util.Logger;

import com.threerings.nexus.distrib.Keyed;
import com.threerings.nexus.distrib.Nexus;
import com.threerings.nexus.server.SessionLocal;

import com.threerings.reversi.core.Factory_GameService;
import com.threerings.reversi.core.GameObject;
import com.threerings.reversi.core.GameService;

/** Manages an individual game of Reversi. */
public class GameManager implements GameService, Keyed {

  public final Integer gameId;
  public final GameObject gameObj;

  public GameManager (Nexus nexus, Integer gameId, Player[] players) {
    this.gameId = gameId;
    _nexus = nexus;
    _log = new Logger("game:" + gameId);

    String[] pnames = new String[players.length];
    for (int ii = 0; ii < players.length; ii++) {
      pnames[ii] = players[ii].nickname;
      _players.put(players[ii], ii);
    }

    // register ourselves as a keyed entity
    nexus.registerKeyed(this);
    // create and register our game object as an anonymous child in our same context
    this.gameObj = new GameObject(pnames, Factory_GameService.createDispatcher(this));
    nexus.register(gameObj, this);
  }

  public void shutdown () {
    _nexus.clear(gameObj);
    _nexus.clearKeyed(this);
  }

  @Override public Comparable<?> getKey () {
    return gameId;
  }

  @Override public void readyToPlay () {
    Player player = SessionLocal.get(Player.class);
    if (gameObj.state.get() != GameObject.State.PRE_GAME) {
      _log.warning("Got readyToPlay, but it's not pre-game", "from", player);
      return;
    }

    // if this player disconnects before leaving normally, clear them out
    SessionLocal.getSession().onDisconnect().map(Functions.constant(player)).
      connect(playerLeft).holdWeakly();

    // if this is a player, note that they're ready to play and maybe start the game
    if (_players.containsKey(player)) {
      _here.add(player);
      if (_here.size() == _players.size()) {
        gameObj.turnHolder.update(0);
        gameObj.state.update(GameObject.State.IN_PLAY);
      }
    }
  }

  @Override public void play (int x, int y) {
    int thIdx = gameObj.turnHolder.get();
    Player player = SessionLocal.get(Player.class);
    if (thIdx < 0 || index(player) != thIdx) {
      _log.warning("Got illegal play request", "from", player, "x", x, "y", y);
      return;
    }
    GameObject.Coord c = new GameObject.Coord(x, y);
    if (gameObj.board.containsKey(c)) {
      _log.warning("Got request to play on occupied spot", "from", player, "at", c,
                   "owner", gameObj.board.get(c));
      return;
    }

    // TODO: the flipping logic
    gameObj.board.put(c, thIdx);
    // TODO: check for winner

    // advance to the other player's turn
    gameObj.turnHolder.update(1-thIdx);
  }

  @Override public void byebye () {
    playerLeft.onEmit(SessionLocal.get(Player.class));
  }

  protected int index (Player player) {
    Integer idx = _players.get(player);
    return (idx == null) ? -1 : idx;
  }

  protected final Slot<Player> playerLeft = new Slot<Player>() { public void onEmit (Player player) {
    if (_players.containsKey(player)) {
      _here.remove(player);
      // if all players have left, shut the game down
      if (_here.size() == 0) shutdown();
    }
  }};

  protected final Nexus _nexus;
  protected final Logger _log;
  protected final Map<Player,Integer> _players = new HashMap<Player,Integer>();
  protected final Set<Player> _here = new HashSet<Player>();
}
