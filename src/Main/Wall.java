package Main;

import java.awt.Color;
import java.awt.Graphics2D;

public class Wall extends Tile {
    // int x, y;
    // int width, height;
    // int startX;
    int BORDER = 2;
    // Rectangle hitBox;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        // this.x = x;
        // startX = x;
        // this.y = y;
        // this.width = width;
        // this.height = height;
        // hitBox = new Rectangle(x, y, width, height);
    }

    @Override
    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.BLACK);
        gtd.fillRect(x, y, width, height);
        gtd.setColor(Color.WHITE);
        gtd.fillRect(x + BORDER, y + BORDER, width - 2 * BORDER, height - 2 * BORDER);
    }
}
