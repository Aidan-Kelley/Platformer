package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Menu;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import Input.InputKey;
import Main.GameState;

public class ControlsMenu {

    private MenuButton[] buttons = new MenuButton[7];
    private final int EXIT_INDEX = 6;

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
        buttons[EXIT_INDEX] = new MenuButton(550, 20, 100, 70, "Exit");
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
        if (buttons[EXIT_INDEX] != null && isIn(e, buttons[EXIT_INDEX])) {
            GameState.state = GameState.PLAYING;
            return;
        }
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
            if (mb != null && mb.getIndex() == 2 && mb instanceof ControlsButton)
                ((ControlsButton) mb).keyPressed(e);
    }
}
