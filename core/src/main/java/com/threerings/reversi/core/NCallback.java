//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.util.Callback;

public abstract class NCallback<T> implements Callback<T> {

  public static abstract class Unit extends NCallback<Void> {
    public abstract void onSuccess ();
    public final void onSuccess (Void unused) {
      onSuccess();
    }
  }

  public void onFailure (Throwable cause) {
    Log.log.warning(this + " failed.", cause);
  }
}
