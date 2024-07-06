package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import util.Constants;

public class MenuButton {

    private Rectangle bounds;
    private boolean mouseOver, mousePressed;
    private int index;
    private String text;

    public MenuButton(int x, int y, int w, int h) {
        this(x, y, w, h, "");
    }

    public MenuButton(int x, int y, int w, int h, String txt) {
        bounds = new Rectangle(x, y, w, h);
        text = txt;
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void draw(Graphics2D gtd) {
        if (getIndex() == 0)
            gtd.setColor(Color.BLUE);
        else if (getIndex() == 1)
            gtd.setColor(new Color(173, 216, 230));
        else
            gtd.setColor(Color.DARK_GRAY);
        gtd.fillRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        gtd.setFont(Constants.buttonFont);
        gtd.setColor(Color.BLACK);
        gtd.drawString(text, getBounds().x + 14,
                getBounds().y + (getBounds().height + Constants.buttonFont.getSize()) / 2);
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

    public int getIndex() {
        return index;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
