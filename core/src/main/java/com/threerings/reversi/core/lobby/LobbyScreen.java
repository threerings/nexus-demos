//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import react.Slot;
import react.Value;

import playn.core.Keyboard;
import static playn.core.PlayN.keyboard;

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
import com.threerings.reversi.core.NCallback;
import com.threerings.reversi.core.PCallback;
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
    obj.lobbySvc.get().hello(new NCallback<String>() {
      public void onSuccess (String nickname) {
        _nickname.update(nickname);
      }
    });
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
              _obj.lobbySvc.get().play(onPlay);
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

  protected final PCallback<String> gotNick = new PCallback<String>() {
    public void onSuccess (final String newnick) {
      if (newnick == null || newnick.length() == 0 || _nickname.get().equals(newnick)) return;
      _obj.lobbySvc.get().updateNick(newnick, new NCallback.Unit() {
        public void onSuccess () {
          _nickname.update(newnick);
        }
      });
    }
  };

  protected final NCallback<Address<GameObject>> onPlay = new NCallback<Address<GameObject>>() {
    public void onSuccess (Address<GameObject> addr) {
      // ignore null addrs, we get those if we request to play and then cancel
      if (addr != null) _game.nexus.subscribe(addr, new NCallback<GameObject>() {
        public void onSuccess (GameObject obj) {
          _game.screens.push(new GameScreen(_game, obj));
        }
      });
    }
  };

  protected final LobbyObject _obj;
  protected final Value<String> _nickname = Value.create("");
}
