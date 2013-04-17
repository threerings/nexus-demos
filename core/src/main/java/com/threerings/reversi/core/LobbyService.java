//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.NexusService;
import com.threerings.nexus.util.Callback;

/** Defines a simple lobby service. */
public interface LobbyService extends NexusService {

  /** Called when a player first enters the lobby. */
  void hello (Callback<String> callback);

  /** Requests that our nickname be updated. */
  void updateNick (String nickname, Callback<Void> callback);

  /** Sends a chat message to the lobby. */
  void chat (String message);

  /** Requests that we be matched with another player for a game. */
  void play (Callback<Address<GameObject>> callback);

  /** Cancels any pending match request. */
  void cancel ();
}
