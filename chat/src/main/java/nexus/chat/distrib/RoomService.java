//
// Nexus Chat Demo - demonstrates Nexus with some chattery
// http://github.com/threerings/nexus/blob/master/LICENSE

package nexus.chat.distrib;

import react.RFuture;

import com.threerings.nexus.distrib.NexusService;

/**
 * Defines distributed services available in a room.
 */
public interface RoomService extends NexusService
{
    /**
     * Requests that the supplied chat message be sent to the room.
     */
    RFuture<Void> sendMessage (String message); // TODO: mode (emote, shout, etc.)?
}
