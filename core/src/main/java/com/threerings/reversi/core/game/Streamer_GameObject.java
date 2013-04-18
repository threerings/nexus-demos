//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.io.Streamer;

/**
 * Handles the streaming of {@link GameObject} and/or nested classes.
 */
public class Streamer_GameObject
    implements Streamer<GameObject>
{
    /**
     * Handles the streaming of {@link GameObject.Coord} instances.
     */
    public static class Coord
        implements Streamer<GameObject.Coord>
    {
        @Override
        public Class<?> getObjectClass () {
            return GameObject.Coord.class;
        }

        @Override
        public void writeObject (Streamable.Output out, GameObject.Coord obj) {
            writeObjectImpl(out, obj);
        }

        @Override
        public GameObject.Coord readObject (Streamable.Input in) {
            return new GameObject.Coord(
                in.readInt(),
                in.readInt()
            );
        }

        public static  void writeObjectImpl (Streamable.Output out, GameObject.Coord obj) {
            out.writeInt(obj.x);
            out.writeInt(obj.y);
        }
    }

    @Override
    public Class<?> getObjectClass () {
        return GameObject.class;
    }

    @Override
    public void writeObject (Streamable.Output out, GameObject obj) {
        writeObjectImpl(out, obj);
        obj.writeContents(out);
    }

    @Override
    public GameObject readObject (Streamable.Input in) {
        GameObject obj = new GameObject(
            in.readStrings(),
            in.<GameService>readService()
        );
        obj.readContents(in);
        return obj;
    }

    public static  void writeObjectImpl (Streamable.Output out, GameObject obj) {
        out.writeStrings(obj.players);
        out.writeService(obj.svc);
    }
}
