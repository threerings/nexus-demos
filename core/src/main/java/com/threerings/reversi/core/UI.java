//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core;

import playn.core.Font;
import static playn.core.PlayN.graphics;

import tripleplay.ui.Element;
import tripleplay.ui.Label;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;

/** UI utility methods. */
public class UI {

  /** The default text color. */
  public static final int TEXT_COLOR = 0xFF321601;

  /** The default text point size. */
  public static final float TEXT_SIZE = 18;

  public static Stylesheet stylesheet () {
    return SimpleStyles.newSheetBuilder().
      add(Element.class, Style.FONT.is(textFont(TEXT_SIZE)), Style.COLOR.is(TEXT_COLOR)).
      create();
  }

  public static Label headerLabel (String text, Style.Binding<?>... styles) {
    return new Label(text).
      addStyles(Style.COLOR.is(0xFFFFFFFF), Style.HIGHLIGHT.is(TEXT_COLOR),
                Style.TEXT_EFFECT.vectorOutline, Style.OUTLINE_WIDTH.is(2f)).
      addStyles(styles);
  }

  public static Font textFont (float size) {
    return graphics().createFont("Helvetica", Font.Style.PLAIN, size);
  }
}
