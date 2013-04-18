//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.distrib.NexusObject;

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

                    @Override public void dispatchCall (short methodId, Object[] args) {
                        switch (methodId) {
                        case 1:
                            service.readyToPlay();
                            break;
                        case 2:
                            service.play(
                                this.<Integer>cast(args[0]),
                                this.<Integer>cast(args[1]));
                            break;
                        case 3:
                            service.byebye();
                            break;
                        default:
                            super.dispatchCall(methodId, args);
                        }
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
        @Override public void readyToPlay () {
            postCall((short)1);
        }
        @Override public void play (int x, int y) {
            postCall((short)2, x, y);
        }
        @Override public void byebye () {
            postCall((short)3);
        }
    }
}
