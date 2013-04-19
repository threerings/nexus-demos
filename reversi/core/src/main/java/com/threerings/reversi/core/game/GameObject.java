//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.distrib.DMap;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.DSignal;
import com.threerings.nexus.distrib.DValue;
import com.threerings.nexus.distrib.NexusObject;
import com.threerings.nexus.io.Streamable;

import com.threerings.reversi.core.chat.ChatMessage;

public class GameObject extends NexusObject {

  /** Tracks the state of the game. */
  public static enum State {
    PRE_GAME, IN_PLAY, GAME_OVER;
  }

  /** The names of the two players. */
  public final String[] players;

  /** Provides game services. */
  public final DService<GameService> svc;

  /** The current state of the game. */
  public final DValue<State> state = DValue.create(this, State.PRE_GAME);

  /** The index of the current turn-holder. */
  public final DValue<Integer> turnHolder = DValue.create(this, -1);

  /** The current state of the board. A mapping from board coord to chip owner. */
  public final DMap<Coord,Integer> board = DMap.create(this);

  /** A signal emitted when a player sends a chat message. */
  public final DSignal<ChatMessage> onChat = DSignal.create(this);

  public GameObject (String[] players, DService.Factory<GameService> svc) {
    this.players = players;
    this.svc = svc.createService(this);
  }
}
