//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.chat;

import playn.core.Keyboard;
import static playn.core.PlayN.keyboard;

import tripleplay.ui.Button;

import com.threerings.reversi.core.PCallback;

public abstract class ChatButton extends Button {

  public ChatButton () {
    super("Chat");
  }

  @Override public void click () {
    keyboard().getText(Keyboard.TextType.DEFAULT, "Enter message:", "", gotChat);
  }

  protected abstract void sendChat (String msg);

  protected final PCallback<String> gotChat = new PCallback<String>() {
    public void onSuccess (final String msg) {
      if (msg == null || msg.length() == 0) return;
      sendChat(msg);
    }
  };
}
