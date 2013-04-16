//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.io.Streamer;

/**
 * Handles the streaming of {@link GameObject} and/or nested classes.
 */
public class Streamer_GameObject
    implements Streamer<GameObject>
{
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
            in.<Integer>readValue()
        );
        obj.readContents(in);
        return obj;
    }

    public static  void writeObjectImpl (Streamable.Output out, GameObject obj) {
        out.writeValue(obj.gameId);
    }
}
