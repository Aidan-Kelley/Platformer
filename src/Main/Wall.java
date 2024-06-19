package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Wall {
    static Color c = Color.RED;
    Color color;
    int x, y;
    int width, height;
    int startX;
    int BORDER = 2;
    Rectangle hitBox;

    public Wall(int x, int y, int width, int height) {
        // Get saturation and brightness.
        float[] hsbVals = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);

        // Pass .5 (= 180 degrees) as HUE
        c = new Color(Color.HSBtoRGB((float) (hsbVals[0] + 1.0 / 360.0), hsbVals[1], hsbVals[2]));
        color = new Color(
                Color.HSBtoRGB((float) (hsbVals[0] + 1.0 / 360.0), hsbVals[1], 1 - ((y - 350) / 410.0f)));
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
        gtd.setColor(color);
        gtd.fillRect(x + BORDER, y + BORDER, width - 2 * BORDER, height - 2 * BORDER);
    }

    public int set(int cameraX) {
        x = startX + cameraX;
        hitBox.x = x;
        return x;
    }
}
