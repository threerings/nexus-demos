//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import react.UnitSlot;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.util.Callback;

import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;

public class ConnectScreen extends AbstractScreen {

  public ConnectScreen (Reversi game) {
    _game = game;
  }

  @Override protected void createIface (Root root) {
    final Label status = new Label("");

    UnitSlot onConnect = new UnitSlot() { public void onEmit () {
      status.text.update("Connecting...");
      _game.nexus.subscribe(Address.create("localhost", LobbyObject.class),
                            new Callback<LobbyObject>() {
                              public void onSuccess (LobbyObject lobobj) {
                                _game.screens.push(new LobbyScreen(_game, lobobj));
                              }
                              public void onFailure (Throwable cause) {
                                status.text.update("Connect failed: " + cause.getMessage());
                              }
                            });
    }};

    Button connect = new Button("Connect");
    connect.clicked().connect(onConnect);
    root.add(status, connect);

    // if this is the first time this screen is shown, auto-connect
    if (_firstShow) onConnect.onEmit();
    _firstShow = false;
  }

  protected final Reversi _game;
  protected boolean _firstShow = true;
}
