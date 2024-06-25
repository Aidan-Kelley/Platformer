package util;

import java.awt.Rectangle;

public class SubPixelRectangle extends Rectangle {

    public int subX;
    public int subY;
    private final int SUBS_PER_PIXEL = 64;

    public SubPixelRectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
        subX = 0;
        subY = 0;
    }

    public void addSubX(int dx) {
        x += Math.floorDiv(dx, SUBS_PER_PIXEL);
        subX += dx;
        subX = Math.floorMod(subX, SUBS_PER_PIXEL);
    }

    public void subtractSubX(int dx) {
        x += Math.floorDiv(-dx, SUBS_PER_PIXEL);
        subX -= dx;
        subX = Math.floorMod(subX, SUBS_PER_PIXEL);
    }

    public void addSubY(int dy) {
        y += Math.floorDiv(dy, SUBS_PER_PIXEL);
        subY += dy;
        subY = Math.floorMod(subY, SUBS_PER_PIXEL);
    }

    public void subtractSubY(int dy) {
        y += Math.floorDiv(-dy, SUBS_PER_PIXEL);
        subY -= dy;
        subY = Math.floorMod(subY, SUBS_PER_PIXEL);
    }
}