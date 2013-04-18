//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.android;

import java.util.concurrent.Executor;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.threerings.nexus.client.JVMClient;
import com.threerings.reversi.core.Reversi;

public class ReversiActivity extends GameActivity {

  @Override
  public void main(){
    Executor playnExec = new Executor() {
      public void execute (Runnable r) {
        platform().invokeLater(r);
      }
    };
    PlayN.run(new Reversi(JVMClient.create(playnExec, Reversi.PORT)));
  }
}
