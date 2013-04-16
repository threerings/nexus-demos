//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.Keyed;
import com.threerings.nexus.distrib.NexusObject;

public class GameObject extends NexusObject implements Keyed {

  /** A unique id assigned to this game. */
  public final Integer gameId;

  public GameObject (Integer gameId) {
    this.gameId = gameId;
  }

  @Override public Comparable<?> getKey () {
    return gameId;
  }
}
