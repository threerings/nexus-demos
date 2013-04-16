//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.DAttribute;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.DSignal;
import com.threerings.nexus.distrib.NexusObject;
import com.threerings.nexus.distrib.NexusService;
import com.threerings.nexus.distrib.Singleton;
import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.util.Callback;

/** A singleton that provides access to lobby services. */
public class LobbyObject extends NexusObject implements Singleton {

  /** Defines a simple lobby service. */
  public static interface Service extends NexusService {

    /** Requests that our nickname be updated. */
    void updateNick (String nickname, Callback<Void> callback);

    /** Sends a chat message to the lobby. */
    void chat (String message);

    /** Requests that we be matched with another player for a game. */
    void play (Callback<Address<GameObject>> callback);

    /** Cancels any pending match request. */
    void cancel ();
  }

  /** Encapsulates the info in a chat message. */
  public static class ChatMessage implements Streamable {

    /** The nickname of the sender, or null if this is a system message. */
    public final String sender;

    /** The text of the message. */
    public final String message;

    public ChatMessage (String sender, String message) {
      this.sender = sender;
      this.message = message;
    }
  }

  /** Provides global lobby services. */
  public final DService<Service> lobbySvc;

  /** A signal emitted when a lobby occupant sends a chat message. */
  public final DSignal<ChatMessage> onChat = DSignal.create();

  public LobbyObject (DService<Service> lobbySvc) {
    this.lobbySvc = lobbySvc;
  }

  @Override protected DAttribute getAttribute (int index) {
    switch (index) {
    case 0: return lobbySvc;
    case 1: return onChat;
    default: throw new IndexOutOfBoundsException("Invalid attribute index " + index);
    }
  }

  @Override protected int getAttributeCount () {
    return 2;
  }
}