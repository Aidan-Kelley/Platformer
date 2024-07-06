package ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Input.InputKey;

public class ControlsMenu {

    private MenuButton[] buttons = new MenuButton[6];

    public ControlsMenu() {
        loadButtons();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(300, 100, 100, 70, InputKey.UP);
        buttons[1] = new MenuButton(300, 200, 100, 70, InputKey.LEFT);
        buttons[2] = new MenuButton(300, 300, 100, 70, InputKey.RIGHT);
        buttons[3] = new MenuButton(300, 400, 100, 70, InputKey.DOWN);
        buttons[4] = new MenuButton(300, 500, 100, 70, InputKey.JUMP);
        buttons[5] = new MenuButton(300, 600, 100, 70, InputKey.ABILITY);
    }

    public void update() {
        for (MenuButton mb : buttons)
            if (mb != null)
                mb.update();
    }

    public void draw(Graphics2D gtd) {
        for (MenuButton mb : buttons)
            if (mb != null)
                mb.draw(gtd);
    }

    public void mouseClicked(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (mb != null && isIn(e, mb))
                mb.setMousePressed(true);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            if (mb != null)
                mb.setMouseOver(false);

        for (MenuButton mb : buttons)
            if (mb != null && isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }

    }

    private boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getPoint().x - 8, e.getPoint().y - 27);
    }

    public void keyPressed(KeyEvent e) {
        for (MenuButton mb : buttons)
            if (mb != null && mb.getIndex() == 2)
                mb.keyPressed(e);
    }
}
