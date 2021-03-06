//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import react.Connection;
import react.ConnectionGroup;

import tripleplay.game.UIScreen;
import tripleplay.ui.Background;
import tripleplay.ui.Layout;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public abstract class AbstractScreen extends UIScreen {

  @Override public void wasShown () {
    super.wasShown();
    _root = iface.createRoot(layout(), UI.stylesheet(), layer);
    _root.addStyles(Style.BACKGROUND.is(Background.solid(0xFFCCCCCC).inset(10)));
    _root.setSize(width(), height());
    createIface(_root);
  }

  @Override public void wasHidden () {
    super.wasHidden();
    iface.destroyRoot(_root);
    _root = null;
    _conns.disconnect();
  }

  protected AbstractScreen (Reversi game) {
    _game = game;
  }

  protected Layout layout () {
    return AxisLayout.vertical().gap(10);
  }

  protected void noteConnection (Connection conn) {
    _conns.add(conn);
  }

  protected abstract void createIface (Root root);

  protected final Reversi _game;
  protected final ConnectionGroup _conns = new ConnectionGroup();
  protected Root _root;
}
