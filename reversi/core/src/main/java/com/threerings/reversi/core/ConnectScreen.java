//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import react.Slot;
import react.UnitSlot;

import com.threerings.nexus.distrib.Address;
import com.threerings.reversi.core.lobby.LobbyObject;
import com.threerings.reversi.core.lobby.LobbyScreen;

import tripleplay.ui.Button;
import tripleplay.ui.Label;
import tripleplay.ui.Root;

public class ConnectScreen extends AbstractScreen {

  public ConnectScreen (Reversi game) {
    super(game);
  }

  @Override protected void createIface (Root root) {
    final Label status = new Label("");

    UnitSlot onConnect = new UnitSlot() { public void onEmit () {
      status.text.update("Connecting...");
      _game.nexus.<LobbyObject>subscriber().
          subscribe(Address.create("localhost", LobbyObject.class)).
          onSuccess(new Slot<LobbyObject>() {
              public void onEmit (LobbyObject obj) {
                  obj.onLost.connect(new UnitSlot() { public void onEmit () {
                      _showStatus = "Lost connection.";
                      _game.screens.popTo(ConnectScreen.this);
                  }});
                  _game.screens.push(new LobbyScreen(_game, obj));
              }
          }).onFailure(new Slot<Throwable>() {
              public void onEmit (Throwable cause) {
                  status.text.update("Connect failed: " + cause.getMessage());
              }
          });
    }};

    Button connect = new Button("Connect");
    connect.clicked().connect(onConnect);
    root.add(status, connect);

    // if this is the first time this screen is shown, auto-connect
    if (_showStatus == null) onConnect.onEmit();
    else status.text.update(_showStatus);
  }

  protected String _showStatus = null;
}
