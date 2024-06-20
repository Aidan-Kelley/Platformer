package Main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Tile {
    int x, y;
    int width, height;
    private int startX;
    Rectangle hitBox;

    public Tile(int x, int y, int width, int height) {
        this.x = x;
        startX = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBox = new Rectangle(x, y, width, height);
    }

    public abstract void draw(Graphics2D gtd);

    public int set(int cameraX) {
        x = startX + cameraX;
        hitBox.x = x;
        return x;
    }
}
