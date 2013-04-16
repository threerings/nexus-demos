//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.java;

import java.util.concurrent.Executor;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.threerings.nexus.client.JVMClient;
import com.threerings.reversi.core.Reversi;

public class ReversiJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    final JavaPlatform platform = JavaPlatform.register(config);
    Executor playnExec = new Executor() {
      public void execute (Runnable r) {
        platform.invokeLater(r);
      }
    };
    PlayN.run(new Reversi(JVMClient.create(playnExec, 1234)));
  }
}
