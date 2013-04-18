//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.distrib.DMap;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.DValue;
import com.threerings.nexus.distrib.NexusObject;
import com.threerings.nexus.io.Streamable;

public class GameObject extends NexusObject {

  /** Tracks the state of the game. */
  public static enum State {
    PRE_GAME, IN_PLAY, GAME_OVER;
  }

  /** Encapsulates a board coordinate. */
  public static class Coord implements Streamable {
    public final int x, y;
    public Coord (int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override public String toString () { return x + "/" + y; }
    @Override public int hashCode () { return x ^ y; }
    @Override public boolean equals (Object other) {
      return (other instanceof Coord) && ((Coord)other).x == x &&  ((Coord)other).y == y;
    }
  }

  /** The names of the two players. */
  public final String[] players;

  /** Provides game services. */
  public final DService<GameService> svc;

  /** The current state of the game. */
  public DValue<State> state = DValue.create(this, State.PRE_GAME);

  /** The index of the current turn-holder. */
  public DValue<Integer> turnHolder = DValue.create(this, -1);

  /** The current state of the board. A mapping from board coord to chip owner. */
  public DMap<Coord,Integer> board = DMap.create(this);

  public GameObject (String[] players, DService.Factory<GameService> svc) {
    this.players = players;
    this.svc = svc.createService(this);
  }
}
