//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import react.RFuture;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.NexusService;
import com.threerings.reversi.core.game.GameObject;

/** Defines a simple lobby service. */
public interface LobbyService extends NexusService {

  /** Called when a player first enters the lobby. */
  RFuture<String> hello ();

  /** Requests that our nickname be updated. */
  RFuture<Void> updateNick (String nickname);

  /** Sends a chat message to the lobby. */
  void chat (String message);

  /** Requests that we be matched with another player for a game. */
  RFuture<Address<GameObject>> play ();

  /** Cancels any pending match request. */
  void cancel ();
}
