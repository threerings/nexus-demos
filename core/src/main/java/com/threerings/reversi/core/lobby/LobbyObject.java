//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.DSignal;
import com.threerings.nexus.distrib.NexusObject;
import com.threerings.nexus.distrib.Singleton;
import com.threerings.nexus.io.Streamable;

import com.threerings.reversi.core.chat.ChatMessage;

/** A singleton that provides access to lobby services. */
public class LobbyObject extends NexusObject implements Singleton {

  /** Provides global lobby services. */
  public final DService<LobbyService> lobbySvc;

  /** A signal emitted when a lobby occupant sends a chat message. */
  public final DSignal<ChatMessage> onChat = DSignal.create(this);

  public LobbyObject (DService.Factory<LobbyService> lobbySvc) {
    this.lobbySvc = lobbySvc.createService(this);
  }
}
