//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.DAttribute;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.DValue;
import com.threerings.nexus.distrib.Keyed;
import com.threerings.nexus.distrib.NexusObject;

public class GameObject extends NexusObject implements Keyed {

  /** Tracks the state of the game. */
  public static enum State {
    PRE_GAME, IN_PLAY, GAME_OVER;
  }

  /** A unique id assigned to this game. */
  public final Integer gameId;

  /** The names of the two players. */
  public final String[] players;

  /** Provides game services. */
  public final DService<GameService> svc;

  /** The current state of the game. */
  public DValue<State> state = DValue.create(this, State.PRE_GAME);

  /** The index of the current turn-holder. */
  public DValue<Integer> turnHolder = DValue.create(this, -1);

  public GameObject (Integer gameId, String[] players, DService.Factory<GameService> svc) {
    this.gameId = gameId;
    this.players = players;
    this.svc = svc.createService(this);
  }

  @Override public Comparable<?> getKey () {
    return gameId;
  }
}
