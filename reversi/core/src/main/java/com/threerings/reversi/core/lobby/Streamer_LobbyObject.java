//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.io.Streamer;

/**
 * Handles the streaming of {@link LobbyObject} and/or nested classes.
 */
public class Streamer_LobbyObject
    implements Streamer<LobbyObject>
{
    @Override
    public Class<?> getObjectClass () {
        return LobbyObject.class;
    }

    @Override
    public void writeObject (Streamable.Output out, LobbyObject obj) {
        writeObjectImpl(out, obj);
        obj.writeContents(out);
    }

    @Override
    public LobbyObject readObject (Streamable.Input in) {
        LobbyObject obj = new LobbyObject(
            in.<LobbyService>readService()
        );
        obj.readContents(in);
        return obj;
    }

    public static  void writeObjectImpl (Streamable.Output out, LobbyObject obj) {
        out.writeService(obj.lobbySvc);
    }
}
