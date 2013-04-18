//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import react.Slot;
import react.Value;

import playn.core.Keyboard;
import static playn.core.PlayN.keyboard;

import com.threerings.nexus.distrib.Address;
import com.threerings.reversi.core.AbstractScreen;
import com.threerings.reversi.core.NCallback;
import com.threerings.reversi.core.PCallback;
import com.threerings.reversi.core.Reversi;
import com.threerings.reversi.core.UI;
import com.threerings.reversi.core.game.GameObject;
import com.threerings.reversi.core.game.GameScreen;

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
    final Group messages = new Group(AxisLayout.vertical(), Style.HALIGN.left, Style.VALIGN.bottom,
                                     Style.BACKGROUND.is(Background.solid(0xFFEEEEEE).inset(5))) {
      protected void layout () {
        super.layout();
        // lop off chat messages that no longer fit on-screen; hack!
        while (childCount() > 0 && childAt(0).y() < 0) {
          removeAt(0);
        }
      }
    };
    _root.add(UI.headerLabel("Lobby"),
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
              AxisLayout.stretch(messages),
              new Group(AxisLayout.horizontal(), Style.HALIGN.right).add(
                new Button("Chat") { public void click () {
                  keyboard().getText(Keyboard.TextType.DEFAULT, "Enter message:", "", gotChat);
                }}));

    noteConnection(_obj.onChat.connect(new Slot<LobbyObject.ChatMessage>() {
      public void onEmit (LobbyObject.ChatMessage msg) {
        Label label = new Label(msg.sender == null ? msg.message :
                                "<" + msg.sender + ">: " + msg.message);
        messages.add(label.addStyles(Style.TEXT_WRAP.on, Style.HALIGN.left));
      }
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

  protected final PCallback<String> gotChat = new PCallback<String>() {
    public void onSuccess (final String msg) {
      if (msg == null || msg.length() == 0) return;
      _obj.lobbySvc.get().chat(msg);
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
