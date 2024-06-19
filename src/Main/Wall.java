package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Wall {

    int x, y;
    int width, height;
    int startX;
    int BORDER = 1;
    Rectangle hitBox;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        startX = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBox = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(Color.BLACK);
        gtd.fillRect(x, y, width, height);
        gtd.setColor(Color.WHITE);
        gtd.fillRect(x + BORDER, y + BORDER, width - 2 * BORDER, height - 2 * BORDER);
    }

    public int set(int cameraX) {
        x = startX + cameraX;
        hitBox.x = x;
        return x;
    }
}
