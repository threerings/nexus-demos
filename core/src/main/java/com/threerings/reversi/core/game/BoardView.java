//
// Reversi - a simple game demonstrating the use of PlayN and Nexus together
// https://github.com/threerings/reversi-playn

package com.threerings.reversi.core.game;

import java.util.HashMap;
import java.util.Map;

import com.threerings.nexus.distrib.DMap;

import playn.core.CanvasImage;
import playn.core.ImageLayer;
import playn.core.ImmediateLayer;
import playn.core.Surface;
import static playn.core.PlayN.graphics;

import tripleplay.ui.SizableWidget;

/** Displays the game board and handles basic interaction. */
public class BoardView extends SizableWidget<BoardView> {

  public static final int SQ_SIZE = 50;

  public BoardView (GameObject obj) {
    super(SQ_SIZE*Board.SIZE, SQ_SIZE*Board.SIZE);

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
    for (Map.Entry<Coord,Integer> entry : obj.board.entrySet()) {
      addChip(entry.getKey()).setImage(makeImage(entry.getValue()));
    }
    obj.board.connect(new DMap.Listener<Coord,Integer>() {
      public void onPut (Coord coord, Integer pidx) {
        ImageLayer chip = _chips.get(coord);
        if (chip == null) chip = addChip(coord);
        chip.setImage(makeImage(pidx));
      }
      // no onRemove: chips are never removed
    });
  }

  @Override protected Class<?> getStyleClass () {
    return BoardView.class;
  }

  protected ImageLayer addChip (Coord coord) {
    ImageLayer chip = graphics().createImageLayer();
    layer.addAt(chip, SQ_SIZE*coord.x, SQ_SIZE*coord.y);
    _chips.put(coord, chip);
    return chip;
  }

  protected CanvasImage makeImage (int pidx) {
    CanvasImage image = graphics().createImage(SQ_SIZE, SQ_SIZE);
    int mid = SQ_SIZE/2;
    image.canvas().setFillColor(pidx == 0 ? 0xFF000000 : 0xFFFFFFFF).fillCircle(mid-1, mid, mid-1);
    image.canvas().setStrokeColor(0xFF000000).strokeCircle(mid-1, mid, mid-1);
    return image;
  }

  protected Map<Coord,ImageLayer> _chips = new HashMap<Coord,ImageLayer>();
}
