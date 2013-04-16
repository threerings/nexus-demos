//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Stylesheet;

/** UI utility methods. */
public class UI {

  public static Stylesheet stylesheet () {
    return SimpleStyles.newSheet();
  }
}
