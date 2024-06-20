package Main;

import java.awt.Color;
import java.awt.Graphics2D;

public class Lava extends Tile {
    int BORDER = 2;

    public Lava(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.BLACK);
        gtd.fillRect(x, y, width, height);
        gtd.setColor(Color.RED);
        gtd.fillRect(x + BORDER, y + BORDER, width - 2 * BORDER, height - 2 * BORDER);
    }
}
