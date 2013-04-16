//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.threerings.nexus.server.GWTConnectionManager;
import com.threerings.nexus.server.JVMConnectionManager;
import com.threerings.nexus.server.NexusConfig;
import com.threerings.nexus.server.NexusServer;

import com.threerings.reversi.core.Reversi;

/** The main entry point for the Reversi server. */
public class ReversiServer {

  public static void main (String[] args) throws IOException {
    Properties props = new Properties();
    props.setProperty("nexus.node", "reversi");
    props.setProperty("nexus.hostname", "localhost");
    props.setProperty("nexus.rpc_timeout", "1000");
    NexusConfig config = new NexusConfig(props);

    // create our server
    ExecutorService exec = Executors.newFixedThreadPool(3);
    NexusServer server = new NexusServer(config, exec);

    // create our singleton lobby manager
    new LobbyManager(server);

    // set up a connection manager and listen on a port
    final JVMConnectionManager jvmmgr = new JVMConnectionManager(server.getSessionManager());
    jvmmgr.listen(config.publicHostname, Reversi.PORT);
    jvmmgr.start();

    // // set up a Jetty instance and our GWTIO servlet
    // final GWTConnectionManager gwtmgr = new GWTConnectionManager(
    //   server.getSessionManager(), new ReversiSerializer(), config.publicHostname, 6502);
    // gwtmgr.setDocRoot(new File("dist/webapp"));
    // gwtmgr.start();
  }
}
