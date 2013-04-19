//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.chat;

import com.threerings.nexus.io.Streamable;

/** Encapsulates the info in a chat message. */
public class ChatMessage implements Streamable {

  /** The nickname of the sender, or null if this is a system message. */
  public final String sender;

  /** The text of the message. */
  public final String message;

  public ChatMessage (String sender, String message) {
    this.sender = sender;
    this.message = message;
  }
}
