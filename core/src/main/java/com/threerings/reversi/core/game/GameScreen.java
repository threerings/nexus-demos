//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import react.Function;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Layout;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.ValueLabel;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

import com.threerings.reversi.core.AbstractScreen;
import com.threerings.reversi.core.Reversi;
import com.threerings.reversi.core.UI;
import com.threerings.reversi.core.chat.ChatButton;
import com.threerings.reversi.core.chat.ChatView;

public class GameScreen extends AbstractScreen {

  public GameScreen (Reversi game, GameObject obj) {
    super(game);
    _obj = obj;
  }

  @Override protected Layout layout () {
    return AxisLayout.horizontal().gap(10).offStretch();
  }

  @Override protected void createIface (Root root) {
    Group info = new Group(AxisLayout.vertical().offStretch().gap(10)).add(
      UI.headerLabel("Reversi"),
      new Group(new TableLayout(TableLayout.COL.alignRight(), TableLayout.COL.alignLeft())).add(
        playerIcon(0), new Label(_obj.players[0]),
        playerIcon(1), new Label(_obj.players[1])),
      AxisLayout.stretch((Group)new ChatView(_obj.onChat, _conns)),
      new Group(AxisLayout.horizontal(), Style.HALIGN.right).add(
        new ChatButton() { protected void sendChat (String msg) { _obj.svc.get().chat(msg); }},
        new Button("Quit") { public void click () { onQuit(); }}));

    root.add(new Group(AxisLayout.vertical()).add(new BoardView(_obj)),
             AxisLayout.stretch(info));

    _obj.svc.get().readyToPlay();
  }

  protected ValueLabel playerIcon (final int index) {
    return new ValueLabel(_obj.turnHolder.map(new Function<Integer,String>() {
      public String apply (Integer thIdx) { return thIdx == index ? "\u2605" : ""; }
    }));
  }

  protected void onQuit () {
    _obj.svc.get().byebye();
    _game.screens.remove(this);
  }

  protected final GameObject _obj;
}
