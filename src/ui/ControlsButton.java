package ui;

import util.Constants;
import Input.InputKey;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class ControlsButton extends MenuButton {

    private InputKey key;

    public ControlsButton(int x, int y, int w, int h, InputKey key) {
        super(x, y, w, h);
        this.key = key;
    }

    public void draw(Graphics2D gtd) {
        super.draw(gtd);
        gtd.setFont(Constants.buttonFont);
        gtd.setColor(Color.BLACK);
        gtd.drawString(key.getKeyText(), getBounds().x + 14,
                getBounds().y + (getBounds().height + Constants.buttonFont.getSize()) / 2);
        gtd.drawString(key.toString(), getBounds().x - 130,
                getBounds().y + (getBounds().height + Constants.buttonFont.getSize()) / 2);
    }

    public void keyPressed(KeyEvent e) {
        key.setKeyCode(e.getKeyCode());
        resetBools();
    }

}
