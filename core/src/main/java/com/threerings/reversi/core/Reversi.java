//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import playn.core.Game;
import playn.core.util.Clock;

import com.threerings.nexus.client.NexusClient;

import tripleplay.game.ScreenStack;

public class Reversi extends Game.Default {

  private static final int UPDATE_RATE = 33; // call update every 33ms (30 times per second)
  private final Clock.Source _clock = new Clock.Source(UPDATE_RATE);

  /** The port on which the client connects to the server. */
  public static final int PORT = 1234;

  public final NexusClient nexus;
  public final Clock clock = _clock;
  public final ScreenStack screens = new ScreenStack();

  public Reversi(NexusClient nexus) {
    super(UPDATE_RATE);
    this.nexus = nexus;
  }

  @Override
  public void init () {
    screens.push(new ConnectScreen(this));
  }

  @Override
  public void update (int delta) {
    _clock.update(delta);
    screens.update(delta);
  }

  @Override
  public void paint (float alpha) {
    _clock.paint(alpha);
    screens.paint(_clock);
  }
}
