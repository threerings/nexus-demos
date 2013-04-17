//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import playn.core.util.Callback;

public abstract class PCallback<T> implements Callback<T> {

  public void onFailure (Throwable cause) {
    cause.printStackTrace(System.err);
  }
}
