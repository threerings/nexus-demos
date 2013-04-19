//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.io.Streamer;

/**
 * Handles the streaming of {@link Coord} and/or nested classes.
 */
public class Streamer_Coord
    implements Streamer<Coord>
{
    @Override
    public Class<?> getObjectClass () {
        return Coord.class;
    }

    @Override
    public void writeObject (Streamable.Output out, Coord obj) {
        writeObjectImpl(out, obj);
    }

    @Override
    public Coord readObject (Streamable.Input in) {
        return new Coord(
            in.readInt(),
            in.readInt()
        );
    }

    public static  void writeObjectImpl (Streamable.Output out, Coord obj) {
        out.writeInt(obj.x);
        out.writeInt(obj.y);
    }
}
