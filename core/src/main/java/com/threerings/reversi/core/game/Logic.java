//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Does the math for Reversi. */
public class Logic {

  /** Returns true if the specified player can play a tile at the specified coordinate. */
  public static boolean isLegalPlay (Map<Coord,Integer> board, int myIndex, Coord coord) {
    if (!inBounds(coord.x, coord.y) || board.containsKey(coord)) return false;

    // look in each direction from this piece, if we see the other guy's pieces and then one of our
    // own, then this is a legal move
    for (int ii = 0; ii < DX.length; ii++) {
      boolean sawOther = false;
      int x = coord.x, y = coord.y;
      for (int dd = 0; dd < Board.SIZE; dd++) {
        x += DX[ii];
        y += DY[ii];
        if (!inBounds(x, y)) break; // stop when we end up off the board
        Integer color = board.get(new Coord(x, y));
        if (color == null) break;
        else if (color == 1-myIndex) sawOther = true;
        else if (color == myIndex && sawOther) return true;
        else break;
      }
    }

    return false;
  }

  /**
   * Applies the specified play (caller must have already checked its legality). Flips tiles as
   * appropriate.
   */
  public static void applyPlay (Map<Coord,Integer> board, int myIndex, Coord coord) {
    List<Coord> toFlip = new ArrayList<Coord>();
    // determine where this piece "captures" pieces of the opposite color
    for (int ii = 0; ii < DX.length; ii++) {
      // look in this direction for captured pieces
      int x = coord.x, y = coord.y;
      for (int dd = 0; dd < Board.SIZE; dd++) {
        x += DX[ii];
        y += DY[ii];
        if (!inBounds(x, y)) break; // stop when we end up off the board
        Coord fc = new Coord(x, y);
        Integer color = board.get(fc);
        if (color == null) break;
        else if (color == 1-myIndex) toFlip.add(fc);
        else if (color == myIndex) {
          for (Coord tf : toFlip) board.put(tf, myIndex); // flip it!
          break;
        }
      }
      toFlip.clear();
    }
  }

  /**
   * Returns true if the player with the specified color has legal moves.
   */
  public static boolean hasLegalPlays (Map<Coord,Integer> board, int myIndex) {
    // search every board position for a legal move; the force, it's so brute!
    for (int yy = 0; yy < Board.SIZE; yy++) {
      for (int xx = 0; xx < Board.SIZE; xx++) {
        Coord coord = new Coord(xx, yy);
        if (board.containsKey(coord)) continue;
        if (isLegalPlay(board, myIndex, coord)) return true;
      }
    }
    return false;
  }

  protected static final boolean inBounds (int x, int y) {
    return (x >= 0) && (x < Board.SIZE) && (y >= 0) && (y < Board.SIZE);
  }

  protected static final int[] DX = { -1, 0, 1, -1, 1, -1, 0, 1 };
  protected static final int[] DY = { -1, -1, -1, 0, 0, 1, 1, 1 };
}
