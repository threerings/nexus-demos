//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import tripleplay.game.UIScreen;

public class LobbyScreen extends UIScreen {

  public LobbyScreen (Reversi game, LobbyObject lobobj) {
    _game = game;
    _lobobj = lobobj;
  }

  protected final Reversi _game;
  protected final LobbyObject _lobobj;
}
