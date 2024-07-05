package ui;

import java.awt.Graphics2D;

import java.awt.event.MouseEvent;

public class ControlsMenu {

    private MenuButton[] buttons = new MenuButton[6];

    protected enum InputKey {
        UP, DOWN, LEFT, RIGHT, JUMP, ABILITY;
    }

    public ControlsMenu() {
        loadButtons();
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(300, 300, 100, 100, InputKey.ABILITY);
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
}
