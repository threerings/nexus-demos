//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.io.Streamable;

/** Encapsulates a board coordinate. */
public class Coord implements Streamable {
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
