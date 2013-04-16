//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import tripleplay.game.UIScreen;
import tripleplay.ui.Root;

public class LobbyScreen extends AbstractScreen {

  public LobbyScreen (Reversi game, LobbyObject lobobj) {
    _game = game;
    _lobobj = lobobj;
    // let the lobby know that we're here
    lobobj.lobbySvc.get().hello();
  }

  @Override protected void createIface (Root root) {
  }

  protected final Reversi _game;
  protected final LobbyObject _lobobj;
}
