//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.lobby;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.NexusObject;

import com.threerings.reversi.core.game.GameObject;
import react.RFuture;

/**
 * Creates {@link LobbyService} marshaller instances.
 */
public class Factory_LobbyService implements DService.Factory<LobbyService>
{
    @Override
    public DService<LobbyService> createService (NexusObject owner)
    {
        return new Marshaller(owner);
    }

    public static DService.Factory<LobbyService> createDispatcher (final LobbyService service)
    {
        return new DService.Factory<LobbyService>() {
            public DService<LobbyService> createService (NexusObject owner) {
                return new DService.Dispatcher<LobbyService>(owner) {
                    @Override public LobbyService get () {
                        return service;
                    }

                    @Override public Class<LobbyService> getServiceClass () {
                        return LobbyService.class;
                    }

                    @Override public RFuture<?> dispatchCall (short methodId, Object[] args) {
                        RFuture<?> result = null;
                        switch (methodId) {
                        case 1:
                            result = service.hello();
                            break;
                        case 2:
                            result = service.updateNick(
                                this.<String>cast(args[0]));
                            break;
                        case 3:
                            service.chat(
                                this.<String>cast(args[0]));
                            break;
                        case 4:
                            result = service.play();
                            break;
                        case 5:
                            service.cancel();
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

    protected static class Marshaller extends DService<LobbyService> implements LobbyService
    {
        public Marshaller (NexusObject owner) {
            super(owner);
        }
        @Override public LobbyService get () {
            return this;
        }
        @Override public Class<LobbyService> getServiceClass () {
            return LobbyService.class;
        }
        @Override public RFuture<String> hello () {
            return this.<String>postCall((short)1);
        }
        @Override public RFuture<Void> updateNick (String nickname) {
            return this.<Void>postCall((short)2, nickname);
        }
        @Override public void chat (String message) {
            postVoidCall((short)3, message);
        }
        @Override public RFuture<Address<GameObject>> play () {
            return this.<Address<GameObject>>postCall((short)4);
        }
        @Override public void cancel () {
            postVoidCall((short)5);
        }
    }
}
