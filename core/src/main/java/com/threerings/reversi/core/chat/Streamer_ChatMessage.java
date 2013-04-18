//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.chat;

import com.threerings.nexus.io.Streamable;
import com.threerings.nexus.io.Streamer;

/**
 * Handles the streaming of {@link ChatMessage} and/or nested classes.
 */
public class Streamer_ChatMessage
    implements Streamer<ChatMessage>
{
    @Override
    public Class<?> getObjectClass () {
        return ChatMessage.class;
    }

    @Override
    public void writeObject (Streamable.Output out, ChatMessage obj) {
        writeObjectImpl(out, obj);
    }

    @Override
    public ChatMessage readObject (Streamable.Input in) {
        return new ChatMessage(
            in.readString(),
            in.readString()
        );
    }

    public static  void writeObjectImpl (Streamable.Output out, ChatMessage obj) {
        out.writeString(obj.sender);
        out.writeString(obj.message);
    }
}
