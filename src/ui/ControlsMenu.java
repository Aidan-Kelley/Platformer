package ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Input.InputKey;

public class ControlsMenu {

    private ControlsButton[] buttons = new ControlsButton[6];

    public ControlsMenu() {
        loadButtons();
    }

    private void loadButtons() {
        buttons[0] = new ControlsButton(300, 50, 100, 70, InputKey.UP);
        buttons[1] = new ControlsButton(300, 150, 100, 70, InputKey.LEFT);
        buttons[2] = new ControlsButton(300, 250, 100, 70, InputKey.RIGHT);
        buttons[3] = new ControlsButton(300, 350, 100, 70, InputKey.DOWN);
        buttons[4] = new ControlsButton(300, 450, 100, 70, InputKey.JUMP);
        buttons[5] = new ControlsButton(300, 550, 100, 70, InputKey.ABILITY);
    }

    public void update() {
        for (ControlsButton mb : buttons)
            if (mb != null)
                mb.update();
    }

    public void draw(Graphics2D gtd) {
        for (ControlsButton mb : buttons)
            if (mb != null)
                mb.draw(gtd);
    }

    public void mouseClicked(MouseEvent e) {
        for (ControlsButton mb : buttons) {
            if (mb != null && isIn(e, mb))
                mb.setMousePressed(true);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (ControlsButton mb : buttons)
            if (mb != null)
                mb.setMouseOver(false);

        for (ControlsButton mb : buttons)
            if (mb != null && isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

    }

    private boolean isIn(MouseEvent e, ControlsButton mb) {
        return mb.getBounds().contains(e.getPoint().x - 8, e.getPoint().y - 27);
    }

    public void keyPressed(KeyEvent e) {
        for (ControlsButton mb : buttons)
            if (mb != null && mb.getIndex() == 2)
                mb.keyPressed(e);
    }
}
