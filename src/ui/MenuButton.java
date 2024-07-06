package ui;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public abstract class MenuButton {

    private Rectangle bounds;
    private boolean mouseOver, mousePressed;
    private int index;

    public MenuButton(int x, int y, int w, int h) {
        bounds = new Rectangle(x, y, w, h);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public abstract void keyPressed(KeyEvent e);

    public abstract void draw(Graphics2D gtd);

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public int getIndex() {
        return index;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
