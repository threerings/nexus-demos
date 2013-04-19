//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.chat;

import react.ConnectionGroup;
import react.SignalView;
import react.Slot;

import tripleplay.ui.Background;
import tripleplay.ui.Group;
import tripleplay.ui.Label;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AxisLayout;

public class ChatView extends Group {

  public ChatView (SignalView<ChatMessage> onChat, ConnectionGroup conns) {
    super(AxisLayout.vertical(), Style.HALIGN.left, Style.VALIGN.bottom,
          Style.BACKGROUND.is(Background.solid(0xFFEEEEEE).inset(5)));
    conns.add(onChat.connect(new Slot<ChatMessage>() {
      public void onEmit (ChatMessage msg) { onChat(msg); }
    }));
  }

  @Override protected void layout () {
    super.layout();
    // lop off chat messages that no longer fit on-screen; hack!
    while (childCount() > 0 && childAt(0).y() < 0) {
      removeAt(0);
    }
  }

  protected void onChat (ChatMessage msg) {
    Label label = new Label(msg.sender == null ? msg.message :
                            "<" + msg.sender + ">: " + msg.message);
    add(label.addStyles(Style.TEXT_WRAP.on, Style.HALIGN.left));
  }
}
