//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.io.ServiceFactory;

/**
 * Creates {@link GameService} marshaller instances.
 */
public class Factory_GameService implements ServiceFactory<GameService>
{
    @Override
    public DService<GameService> createService ()
    {
        return new Marshaller();
    }

    public static DService<GameService> createDispatcher (final GameService service)
    {
        return new DService.Dispatcher<GameService>() {
            @Override public GameService get () {
                return service;
            }

            @Override public Class<GameService> getServiceClass () {
                return GameService.class;
            }

            @Override public void dispatchCall (short methodId, Object[] args) {
                switch (methodId) {
                case 1:
                    service.play(
                        this.<Integer>cast(args[0]),
                        this.<Integer>cast(args[1]));
                    break;
                default:
                    super.dispatchCall(methodId, args);
                }
            }
        };
    }

    protected static class Marshaller extends DService<GameService> implements GameService
    {
        @Override public GameService get () {
            return this;
        }
        @Override public Class<GameService> getServiceClass () {
            return GameService.class;
        }
        @Override public void play (int x, int y) {
            postCall((short)1, x, y);
        }
    }
}