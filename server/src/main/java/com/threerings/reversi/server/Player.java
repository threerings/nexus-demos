//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.server;

import com.threerings.nexus.distrib.Action;
import com.threerings.nexus.distrib.Nexus;

/**
 * A class used to store session-local data for a player.
 */
public class Player {

  /** This player's configured nickname. */
  public String nickname;

  public Player (Nexus nexus, String nickname) {
    this.nickname = nickname;
    _nexus = nexus;
  }

  // public void enterRoom (String name) {
  //   leaveRoom(); // leave any current room
  //   _name = name;
  //   _nexus.invoke(RoomManager.class, _name, new Action<RoomManager>() {
  //     public void invoke (RoomManager mgr) {
  //       mgr.chatterEntered(nickname);
  //     }
  //   });
  // }

  // public void leaveRoom () {
  //   if (_name != null) {
  //     _nexus.invoke(RoomManager.class, _name, new Action<RoomManager>() {
  //       public void invoke (RoomManager mgr) {
  //         mgr.chatterLeft(nickname);
  //       }
  //     });
  //     _name = null;
  //   }
  // }

  protected Nexus _nexus;
}
