//
// Nexus Chat Demo - demonstrates Nexus with some chattery
// http://github.com/threerings/nexus/blob/master/LICENSE

package nexus.chat.distrib;

import java.util.List;

import react.RFuture;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.NexusService;

/**
 * Defines global chat services.
 */
public interface ChatService extends NexusService
{
    /**
     * Requests that our nickname be updated.
     */
    RFuture<Void> updateNick (String nickname);

    /**
     * Returns a list of names of active chat rooms.
     */
    RFuture<List<String>> getRooms ();

    /**
     * Requests to join the chat room with the specified name. Returns the room object on success
     * (to which the client will be subscribed). Failure communicated via exception message (room
     * no longer exists, access denied).
     */
    RFuture<Address<RoomObject>> joinRoom (String name);

    /**
     * Requests that a chat room with the specified name be created. The caller implicitly joins
     * the room (and leaves any room they currently occupy). Returns the newly created room object
     * on success (to which the client will be subscribed).
     */
    RFuture<Address<RoomObject>> createRoom (String name);
}
