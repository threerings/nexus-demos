//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.Nexus;
import com.threerings.nexus.distrib.Singleton;
import com.threerings.nexus.util.Callback;

import com.threerings.reversi.core.Factory_LobbyService;
import com.threerings.reversi.core.GameObject;
import com.threerings.reversi.core.LobbyObject;
import com.threerings.reversi.core.LobbyService;

/** Manages the server side of the Reversi lobby. */
public class LobbyManager implements LobbyService, Singleton {

  public LobbyManager (Nexus nexus) {
    _nexus = nexus;
    // register ourselves as a singleton
    nexus.registerSingleton(this);
    // create and register our lobby object as a child singleton in our same context
    LobbyObject lobj = new LobbyObject(Factory_LobbyService.createDispatcher(this));
    nexus.registerSingleton(lobj, this);
  }

  @Override public void updateNick (String nickname, Callback<Void> callback) {
  }

  @Override public void chat (String message) {
  }

  @Override public void play (Callback<Address<GameObject>> callback) {
  }

  @Override public void cancel () {
  }

  protected final Nexus _nexus;
}
