//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.NexusObject;

import react.RFuture;

/**
 * Creates {@link GameService} marshaller instances.
 */
public class Factory_GameService implements DService.Factory<GameService>
{
    @Override
    public DService<GameService> createService (NexusObject owner)
    {
        return new Marshaller(owner);
    }

    public static DService.Factory<GameService> createDispatcher (final GameService service)
    {
        return new DService.Factory<GameService>() {
            public DService<GameService> createService (NexusObject owner) {
                return new DService.Dispatcher<GameService>(owner) {
                    @Override public GameService get () {
                        return service;
                    }

                    @Override public Class<GameService> getServiceClass () {
                        return GameService.class;
                    }

                    @Override public RFuture<?> dispatchCall (short methodId, Object[] args) {
                        RFuture<?> result = null;
                        switch (methodId) {
                        case 1:
                            result = service.readyToPlay();
                            break;
                        case 2:
                            service.play(
                                this.<Coord>cast(args[0]));
                            break;
                        case 3:
                            service.chat(
                                this.<String>cast(args[0]));
                            break;
                        case 4:
                            service.byebye();
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

    protected static class Marshaller extends DService<GameService> implements GameService
    {
        public Marshaller (NexusObject owner) {
            super(owner);
        }
        @Override public GameService get () {
            return this;
        }
        @Override public Class<GameService> getServiceClass () {
            return GameService.class;
        }
        @Override public RFuture<Integer> readyToPlay () {
            return this.<Integer>postCall((short)1);
        }
        @Override public void play (Coord coord) {
            postVoidCall((short)2, coord);
        }
        @Override public void chat (String message) {
            postVoidCall((short)3, message);
        }
        @Override public void byebye () {
            postVoidCall((short)4);
        }
    }
}
