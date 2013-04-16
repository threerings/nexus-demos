//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.threerings.reversi.core.Reversi;

public class ReversiActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Reversi());
  }
}
