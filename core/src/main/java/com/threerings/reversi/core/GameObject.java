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
  public DValue<State> state = DValue.create(State.PRE_GAME);

  /** The index of the current turn-holder. */
  public DValue<Integer> turnHolder = DValue.create(-1);

  public GameObject (Integer gameId, String[] players, DService<GameService> svc) {
    this.gameId = gameId;
    this.players = players;
    this.svc = svc;
  }

  @Override public Comparable<?> getKey () {
    return gameId;
  }

  @Override protected DAttribute getAttribute (int index) {
    switch (index) {
    case 0: return svc;
    case 1: return state;
    case 2: return turnHolder;
    default: throw new IndexOutOfBoundsException("Invalid attribute index " + index);
    }
  }

  @Override protected int getAttributeCount () {
    return 3;
  }
}
