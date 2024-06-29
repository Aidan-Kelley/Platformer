package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Tile {
    public int x, y;
    private int width, height;
    private int startX;
    private final int BORDER = 3;
    public Rectangle hitBox;

    public boolean isSolid;
    public boolean isHarmful;
    private Color outlineColor;
    private Color fillCOlor;
    public Type type;

    public static enum Type {
        WALL, LAVA, SPEED
    }

    public Tile(int x, int y, int width, int height, Type type) {
        this.type = type;
        this.x = x;
        startX = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitBox = new Rectangle(x, y, width, height);
        switch (type) {
            case WALL:
                isSolid = true;
                isHarmful = false;
                outlineColor = Color.BLACK;
                fillCOlor = Color.WHITE;
                break;
            case LAVA:
                isSolid = false;
                isHarmful = true;
                outlineColor = Color.BLACK;
                fillCOlor = Color.RED;
                break;
            case SPEED:
                isSolid = false;
                isHarmful = false;
                outlineColor = Color.BLACK;
                fillCOlor = Color.CYAN;
                break;
            default:
                isSolid = false;
                isHarmful = false;
                outlineColor = Color.WHITE;
                fillCOlor = Color.BLACK;
                break;
        }
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(outlineColor);
        gtd.fillRect(x, y, width, height);
        gtd.setColor(fillCOlor);
        gtd.fillRect(x + BORDER, y + BORDER, width - 2 * BORDER, height - 2 * BORDER);
    }

    public int set(int cameraX) {
        x = startX + cameraX;
        hitBox.x = x;
        return x;
    }

    public Type getType() {
        return type;
    }
}
