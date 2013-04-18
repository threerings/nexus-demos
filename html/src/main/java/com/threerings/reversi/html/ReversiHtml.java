//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.threerings.nexus.client.GWTClient;

import com.threerings.reversi.core.Reversi;
import com.threerings.reversi.core.ReversiSerializer;

public class ReversiHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform platform = HtmlPlatform.register(config);
    platform.assets().setPathPrefix("reversi/");
    PlayN.run(new Reversi(GWTClient.create(Reversi.WEB_PORT, new ReversiSerializer())));
  }
}
