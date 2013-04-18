//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.NexusService;

/** Defines actions taken by player in a game. */
public interface GameService extends NexusService {

  /** Lets the server know that we're here and ready to go. */
  void readyToPlay ();

  /** Requests to make our move at the specified coordinates. */
  void play (int x, int y);

  /** Called when the player leaves the game in an orderly manner. */
  void byebye ();
}
