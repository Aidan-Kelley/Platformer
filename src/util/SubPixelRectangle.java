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
        subX += dx;
        x += Math.floorDiv(subX, SUBS_PER_PIXEL);
        subX = Math.floorMod(subX, SUBS_PER_PIXEL);
    }

    public void subtractSubX(int dx) {
        subX -= dx;
        x += Math.floorDiv(subX, SUBS_PER_PIXEL);
        subX = Math.floorMod(subX, SUBS_PER_PIXEL);
    }

    public void addSubY(int dy) {
        subY += dy;
        y += Math.floorDiv(subY, SUBS_PER_PIXEL);
        subY = Math.floorMod(subY, SUBS_PER_PIXEL);
    }

    public void subtractSubY(int dy) {
        subY -= dy;
        y += Math.floorDiv(subY, SUBS_PER_PIXEL);
        subY = Math.floorMod(subY, SUBS_PER_PIXEL);
    }
}