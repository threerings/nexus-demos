//
// Nexus Chat Demo - demonstrates Nexus with some chattery
// http://github.com/threerings/nexus/blob/master/LICENSE

package nexus.chat.distrib;

import java.util.List;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.NexusObject;

import react.RFuture;

/**
 * Creates {@link ChatService} marshaller instances.
 */
public class Factory_ChatService implements DService.Factory<ChatService>
{
    @Override
    public DService<ChatService> createService (NexusObject owner)
    {
        return new Marshaller(owner);
    }

    public static DService.Factory<ChatService> createDispatcher (final ChatService service)
    {
        return new DService.Factory<ChatService>() {
            public DService<ChatService> createService (NexusObject owner) {
                return new DService.Dispatcher<ChatService>(owner) {
                    @Override public ChatService get () {
                        return service;
                    }

                    @Override public Class<ChatService> getServiceClass () {
                        return ChatService.class;
                    }

                    @Override public RFuture<?> dispatchCall (short methodId, Object[] args) {
                        RFuture<?> result = null;
                        switch (methodId) {
                        case 1:
                            result = service.updateNick(
                                this.<String>cast(args[0]));
                            break;
                        case 2:
                            result = service.getRooms();
                            break;
                        case 3:
                            result = service.joinRoom(
                                this.<String>cast(args[0]));
                            break;
                        case 4:
                            result = service.createRoom(
                                this.<String>cast(args[0]));
                            break;
                        default:
                            result = super.dispatchCall(methodId, args);
                        }
                        return result;
                    }
                };
            }
        };
    }

    protected static class Marshaller extends DService<ChatService> implements ChatService
    {
        public Marshaller (NexusObject owner) {
            super(owner);
        }
        @Override public ChatService get () {
            return this;
        }
        @Override public Class<ChatService> getServiceClass () {
            return ChatService.class;
        }
        @Override public RFuture<Void> updateNick (String nickname) {
            return this.<Void>postCall((short)1, nickname);
        }
        @Override public RFuture<List<String>> getRooms () {
            return this.<List<String>>postCall((short)2);
        }
        @Override public RFuture<Address<RoomObject>> joinRoom (String name) {
            return this.<Address<RoomObject>>postCall((short)3, name);
        }
        @Override public RFuture<Address<RoomObject>> createRoom (String name) {
            return this.<Address<RoomObject>>postCall((short)4, name);
        }
    }
}
