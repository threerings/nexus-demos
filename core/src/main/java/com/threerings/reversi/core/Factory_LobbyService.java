//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import com.threerings.nexus.distrib.Address;
import com.threerings.nexus.distrib.DService;
import com.threerings.nexus.io.ServiceFactory;
import com.threerings.nexus.util.Callback;

/**
 * Creates {@link LobbyService} marshaller instances.
 */
public class Factory_LobbyService implements ServiceFactory<LobbyService>
{
    @Override
    public DService<LobbyService> createService ()
    {
        return new Marshaller();
    }

    public static DService<LobbyService> createDispatcher (final LobbyService service)
    {
        return new DService.Dispatcher<LobbyService>() {
            @Override public LobbyService get () {
                return service;
            }

            @Override public Class<LobbyService> getServiceClass () {
                return LobbyService.class;
            }

            @Override public void dispatchCall (short methodId, Object[] args) {
                switch (methodId) {
                case 1:
                    service.hello();
                    break;
                case 2:
                    service.updateNick(
                        this.<String>cast(args[0]),
                        this.<Callback<Void>>cast(args[1]));
                    break;
                case 3:
                    service.chat(
                        this.<String>cast(args[0]));
                    break;
                case 4:
                    service.play(
                        this.<Callback<Address<GameObject>>>cast(args[0]));
                    break;
                case 5:
                    service.cancel();
                    break;
                default:
                    super.dispatchCall(methodId, args);
                }
            }
        };
    }

    protected static class Marshaller extends DService<LobbyService> implements LobbyService
    {
        @Override public LobbyService get () {
            return this;
        }
        @Override public Class<LobbyService> getServiceClass () {
            return LobbyService.class;
        }
        @Override public void hello () {
            postCall((short)1);
        }
        @Override public void updateNick (String nickname, Callback<Void> callback) {
            postCall((short)2, nickname, callback);
        }
        @Override public void chat (String message) {
            postCall((short)3, message);
        }
        @Override public void play (Callback<Address<GameObject>> callback) {
            postCall((short)4, callback);
        }
        @Override public void cancel () {
            postCall((short)5);
        }
    }
}
