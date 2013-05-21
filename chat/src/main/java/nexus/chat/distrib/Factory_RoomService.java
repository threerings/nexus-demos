//
// Nexus Chat Demo - demonstrates Nexus with some chattery
// http://github.com/threerings/nexus/blob/master/LICENSE

package nexus.chat.distrib;

import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.NexusObject;

import react.RFuture;

/**
 * Creates {@link RoomService} marshaller instances.
 */
public class Factory_RoomService implements DService.Factory<RoomService>
{
    @Override
    public DService<RoomService> createService (NexusObject owner)
    {
        return new Marshaller(owner);
    }

    public static DService.Factory<RoomService> createDispatcher (final RoomService service)
    {
        return new DService.Factory<RoomService>() {
            public DService<RoomService> createService (NexusObject owner) {
                return new DService.Dispatcher<RoomService>(owner) {
                    @Override public RoomService get () {
                        return service;
                    }

                    @Override public Class<RoomService> getServiceClass () {
                        return RoomService.class;
                    }

                    @Override public RFuture<?> dispatchCall (short methodId, Object[] args) {
                        RFuture<?> result = null;
                        switch (methodId) {
                        case 1:
                            result = service.sendMessage(
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

    protected static class Marshaller extends DService<RoomService> implements RoomService
    {
        public Marshaller (NexusObject owner) {
            super(owner);
        }
        @Override public RoomService get () {
            return this;
        }
        @Override public Class<RoomService> getServiceClass () {
            return RoomService.class;
        }
        @Override public RFuture<Void> sendMessage (String message) {
            return this.<Void>postCall((short)1, message);
        }
    }
}
