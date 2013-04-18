//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.reversi.core.AbstractScreen;
import com.threerings.reversi.core.Reversi;

import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.ValueLabel;

public class GameScreen extends AbstractScreen {

  public GameScreen (Reversi game, GameObject obj) {
    super(game);
    _obj = obj;
  }

  @Override protected void createIface (Root root) {
    _obj.svc.get().readyToPlay();
    root.add(new Label("TODO"));
    root.add(new ValueLabel(_obj.state));
  }

  protected final GameObject _obj;
}
