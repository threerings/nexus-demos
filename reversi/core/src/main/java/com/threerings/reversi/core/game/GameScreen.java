//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import react.Function;
import react.Slot;

import static playn.core.PlayN.graphics;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Icons;
import tripleplay.ui.Label;
import tripleplay.ui.Layout;
import tripleplay.ui.Root;
import tripleplay.ui.Style;
import tripleplay.ui.ValueLabel;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.ui.layout.TableLayout;

import com.threerings.nexus.client.Subscriber;
import com.threerings.reversi.core.AbstractScreen;
import com.threerings.reversi.core.Reversi;
import com.threerings.reversi.core.UI;
import com.threerings.reversi.core.chat.ChatButton;
import com.threerings.reversi.core.chat.ChatView;

public class GameScreen extends AbstractScreen {

  public final GameObject obj;

  public GameScreen (Reversi game, Subscriber<GameObject> sub, GameObject obj) {
    super(game);
    _sub = sub;
    this.obj = obj;
  }

  void tileClicked (Coord coord) {
    if (obj.turnHolder.get() == _ourIdx && Logic.isLegalPlay(obj.board, _ourIdx, coord)) {
      obj.svc.get().play(coord);
    }
  }

  @Override public void wasRemoved () {
      super.wasRemoved();
      _sub.unsubscribe();
  }

  @Override protected Layout layout () {
    return AxisLayout.horizontal().gap(10).offStretch();
  }

  @Override protected void createIface (Root root) {
    Group info = new Group(AxisLayout.vertical().offStretch().gap(10)).add(
      UI.headerLabel("Reversi"),
      new Group(new TableLayout(TableLayout.COL.alignRight(), TableLayout.COL.fixed(),
                                TableLayout.COL.alignLeft())).add(
        playerIcon(0), new Label(obj.players[0]), turnIcon(0),
        playerIcon(1), new Label(obj.players[1]), turnIcon(1)),
      AxisLayout.stretch((Group)new ChatView(obj.onChat, _conns)),
      new Group(AxisLayout.horizontal(), Style.HALIGN.right).add(
        new ChatButton() { protected void sendChat (String msg) { obj.svc.get().chat(msg); }},
        new Button("Quit") { public void click () { onQuit(); }}));

    root.add(new Group(AxisLayout.vertical()).add(new BoardView(this)),
             AxisLayout.stretch(info));

    obj.svc.get().readyToPlay().onSuccess(new Slot<Integer>() {
        public void onEmit (Integer ourIdx) {
            _ourIdx = ourIdx;
        }
    });
  }

  protected Label playerIcon (final int index) {
    return new Label(Icons.image(BoardView.makeImage(index, 20)));
  }

  protected ValueLabel turnIcon (final int index) {
    return new ValueLabel(obj.turnHolder.map(new Function<Integer,String>() {
      public String apply (Integer thIdx) { return thIdx == index ? "\u2605" : ""; }
    }));
  }

  protected void onQuit () {
    obj.svc.get().byebye();
    _game.screens.remove(this);
  }

  protected int _ourIdx;
  protected final Subscriber<GameObject> _sub;
}
