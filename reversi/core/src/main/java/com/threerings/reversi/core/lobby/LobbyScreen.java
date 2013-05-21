//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import react.Functions;
import react.Slot;
import react.Value;

import playn.core.Keyboard;
import playn.core.util.Callback;
import static playn.core.PlayN.keyboard;

import com.threerings.nexus.client.Subscriber;
import com.threerings.nexus.distrib.Address;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Layout;
import tripleplay.ui.Root;
import tripleplay.ui.Shim;
import tripleplay.ui.Style;
import tripleplay.ui.ValueLabel;
import tripleplay.ui.layout.AxisLayout;

import com.threerings.reversi.core.AbstractScreen;
import com.threerings.reversi.core.Reversi;
import com.threerings.reversi.core.UI;
import com.threerings.reversi.core.chat.ChatButton;
import com.threerings.reversi.core.chat.ChatView;
import com.threerings.reversi.core.game.GameObject;
import com.threerings.reversi.core.game.GameScreen;

public class LobbyScreen extends AbstractScreen {

  public LobbyScreen (Reversi game, LobbyObject obj) {
    super(game);
    _obj = obj;
    // let the lobby know that we're here
    obj.lobbySvc.get().hello().onSuccess(_nickname.slot());
  }

  @Override protected Layout layout () {
    return AxisLayout.vertical().gap(10).offStretch();
  }

  @Override protected void createIface (Root root) {
    _root.add(
      UI.headerLabel("Lobby"),
      new Group(AxisLayout.horizontal()).add(
        new Label("Nickname:"),
        new ValueLabel(_nickname),
        new Button("Change") { public void click () {
          keyboard().getText(Keyboard.TextType.DEFAULT, "New nickname:",
                             _nickname.get(), gotNick);
        }},
        new Shim(20, 1),
        new Label("Play:"),
        new Button("Play") {
          public void click () {
            if (!_pending) {
              final Subscriber<GameObject> sub = _game.nexus.subscriber();
              _obj.lobbySvc.get().play().flatMap(sub).onSuccess(new Slot<GameObject>() {
                  public void onEmit (GameObject obj) {
                      _game.screens.push(new GameScreen(_game, sub, obj));
                  }
              });
              text.update("Cancel");
            } else {
              _obj.lobbySvc.get().cancel();
              text.update("Play");
            }
            _pending = !_pending;
          }
          protected boolean _pending;
        }),
      AxisLayout.stretch((Group)new ChatView(_obj.onChat, _conns)),
      new Group(AxisLayout.horizontal(), Style.HALIGN.right).add(
        new ChatButton() {
          protected void sendChat (String msg) { _obj.lobbySvc.get().chat(msg); }
        }));
  }

  protected final Callback<String> gotNick = new Callback<String>() {
    public void onSuccess (final String newnick) {
      if (newnick == null || newnick.length() == 0 || _nickname.get().equals(newnick)) return;
      _obj.lobbySvc.get().updateNick(newnick).map(
          Functions.constant(newnick)).onSuccess(_nickname.slot());
    }
    public void onFailure (Throwable cause) {} // can't happen
  };

  protected final LobbyObject _obj;
  protected final Value<String> _nickname = Value.create("");
}
