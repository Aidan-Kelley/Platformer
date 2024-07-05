package ui;

import Main.GameState;
import ui.ControlsMenu.InputKey;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class MenuButton {

    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    private int index;
    private InputKey key;

    public MenuButton(int x, int y, int w, int h, InputKey key) {
        bounds = new Rectangle(x, y, w, h);
        this.key = key;
    }

    public void draw(Graphics2D gtd) {
        if (index == 0)
            gtd.setColor(Color.BLUE);
        else
            gtd.setColor(new Color(173, 216, 230));
        gtd.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

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

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
