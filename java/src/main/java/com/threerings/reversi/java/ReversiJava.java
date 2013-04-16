//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.threerings.nexus.client.JVMClient;
import com.threerings.reversi.core.Reversi;

public class ReversiJava {

  public static void main(String[] args) {
    JavaPlatform.Config config = new JavaPlatform.Config();
    // use config to customize the Java platform, if needed
    JavaPlatform.register(config);
    PlayN.run(new Reversi(JVMClient.create(1234)));
  }
}
