//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.chat;

import playn.core.Keyboard;
import playn.core.util.Callback;
import static playn.core.PlayN.keyboard;

import react.UnitSlot;
import tripleplay.ui.Button;

public abstract class ChatButton extends Button {

  public ChatButton () {
    super("Chat");
    onClick(new UnitSlot() { public void onEmit () {
      keyboard().getText(Keyboard.TextType.DEFAULT, "Enter message:", "", gotChat);
    }});
  }

  protected abstract void sendChat (String msg);

  protected final Callback<String> gotChat = new Callback<String>() {
    public void onSuccess (final String msg) {
      if (msg == null || msg.length() == 0) return;
      sendChat(msg);
    }
    public void onFailure (Throwable cause) {} // can't happen
  };
}
