//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/** Launches two clients in forked JVM processes, and then runs the server in this process. */
public class TestServer {

  public static void main (String[] args) throws IOException {
    final Signal sigint = new Signal("INT");
    _ohandler = Signal.handle(sigint, new SignalHandler() {
      public void handle (Signal sig) {
        Signal.handle(sigint, _ohandler);
        for (Process proc : _procs) proc.destroy();
        System.exit(0);
      }
    });

    forkClient();
    forkClient();
    ReversiServer.main(args);
  }

  protected static void forkClient () throws IOException {
    final Process proc = Runtime.getRuntime().exec(new String[] { "mvn", "-q", "-o", "test" },
                                                   new String[0], new File(".."));
    System.err.println("Started " + proc);
    startReader(proc.getInputStream(), System.out);
    startReader(proc.getErrorStream(), System.err);
  }

  protected static void startReader (final InputStream in, final PrintStream out) {
    new Thread("stdout/err reader") {
      public void run () {
        try {
          BufferedReader bin = new BufferedReader(new InputStreamReader(in));
          String line;
          while ((line = bin.readLine()) != null) {
            out.println(line);
          }
        } catch (IOException ioe) {
          ioe.printStackTrace(System.err);
        }
      }
    }.start();
  }

  protected static List<Process> _procs = new ArrayList<Process>();
  protected static SignalHandler _ohandler;
}
