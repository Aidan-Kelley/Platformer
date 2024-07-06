package ui;

import Main.GameState;
import util.Constants;
import Input.InputKey;

import static Input.InputKey.ABILITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class MenuButton {

    private boolean mouseOver, mousePressed;
    private Rectangle bounds, display;
    private int index;
    private InputKey key;

    public MenuButton(int x, int y, int w, int h, InputKey key) {
        bounds = new Rectangle(x, y, w, h);
        this.key = key;
    }

    public void draw(Graphics2D gtd) {
        if (index == 0)
            gtd.setColor(Color.BLUE);
        else if (index == 1)
            gtd.setColor(new Color(173, 216, 230));
        else
            gtd.setColor(Color.DARK_GRAY);
        gtd.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        gtd.setFont(Constants.buttonFont);

        gtd.setColor(Color.BLACK);
        gtd.drawString(key.toString(), bounds.x - 130,
                bounds.y + (bounds.height + Constants.buttonFont.getSize()) / 2);
        gtd.drawString(key.getKeyText(), bounds.x + 20,
                bounds.y + (bounds.height + Constants.buttonFont.getSize()) / 2);
    }

    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    public void keyPressed(KeyEvent e) {
        key.setKeyCode(e.getKeyCode());
        resetBools();
        GameState.state = GameState.PLAYING;
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
