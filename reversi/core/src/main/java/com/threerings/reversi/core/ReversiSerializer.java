//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.io.AbstractSerializer;
import com.threerings.reversi.core.chat.*;
import com.threerings.reversi.core.game.*;
import com.threerings.reversi.core.lobby.*;

public class ReversiSerializer extends AbstractSerializer {

  public ReversiSerializer () {
    mapStreamer(new Streamer_ChatMessage());
    mapStreamer(new Streamer_Coord());
    mapStreamer(new Streamer_GameObject());
    mapStreamer(new Streamer_LobbyObject());
    mapEnumStreamer(GameObject.State.class);
    mapService(new Factory_GameService(), GameService.class);
    mapService(new Factory_LobbyService(), LobbyService.class);
  }
}
