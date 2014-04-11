//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import java.util.HashMap;
import java.util.Map;

import pythagoras.f.MathUtil;

import com.threerings.nexus.distrib.DMap;

import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.ImmediateLayer;
import playn.core.Pointer;
import playn.core.Surface;
import static playn.core.PlayN.graphics;

import tripleplay.ui.Behavior;
import tripleplay.ui.SizableWidget;

/** Displays the game board and handles basic interaction. */
public class BoardView extends SizableWidget<BoardView> {

  public static final int SQ_SIZE = 50;

  public BoardView (GameScreen game) {
    super(SQ_SIZE*Board.SIZE, SQ_SIZE*Board.SIZE);
    _game = game;

    // add an immediate layer that draws our board
    layer.add(graphics().createImmediateLayer(new ImmediateLayer.Renderer() {
      public void render (Surface surf) {
        surf.setFillColor(0xFFFFFFFF).fillRect(0, 0, _size.width, _size.height);
        surf.setFillColor(0xFF000000);
        for (int yy = 0; yy <= Board.SIZE; yy++) {
          surf.drawLine(0, SQ_SIZE*yy, _size.width, SQ_SIZE*yy, 1);
        }
        for (int xx = 0; xx <= Board.SIZE; xx++) {
          surf.drawLine(SQ_SIZE*xx, 0, SQ_SIZE*xx, _size.height, 1);
        }
      }
    }));

    // add the current pieces, and listen to obj.board for piece additions/updates
    for (Map.Entry<Coord,Integer> entry : _game.obj.board.entrySet()) {
      addChip(entry.getKey()).setImage(makeImage(entry.getValue(), SQ_SIZE));
    }
    _game.obj.board.connect(new DMap.Listener<Coord,Integer>() {
      public void onPut (Coord coord, Integer pidx) {
        ImageLayer chip = _chips.get(coord);
        if (chip == null) chip = addChip(coord);
        chip.setImage(makeImage(pidx, SQ_SIZE));
      }
      // no onRemove: chips are never removed
    });
  }

  @Override protected Class<?> getStyleClass () {
    return BoardView.class;
  }

  @Override protected Behavior<BoardView> createBehavior () {
    return new Behavior.Ignore<BoardView>(this) {
      @Override protected void onPress (Pointer.Event event) {
        float x = event.localX(), y = event.localY();
        _game.tileClicked(new Coord(MathUtil.ifloor(x / SQ_SIZE), MathUtil.ifloor(y / SQ_SIZE)));
      }
    };
  }

  protected ImageLayer addChip (Coord coord) {
    ImageLayer chip = graphics().createImageLayer();
    layer.addAt(chip, SQ_SIZE*coord.x, SQ_SIZE*coord.y);
    _chips.put(coord, chip);
    return chip;
  }

  protected static CanvasImage makeImage (int pidx, float size) {
    CanvasImage image = graphics().createImage(size, size);
    float mid = size/2;
    image.canvas().setFillColor(Board.COLOR[pidx]).fillCircle(mid-1, mid, mid-1);
    image.canvas().setStrokeColor(0xFF666666).strokeCircle(mid-1, mid, mid-1);
    return image;
  }

  protected final Map<Coord,ImageLayer> _chips = new HashMap<Coord,ImageLayer>();
  protected final GameScreen _game;
}
