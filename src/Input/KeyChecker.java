package Input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import Main.GameState;

public class KeyChecker extends KeyAdapter {
    GamePanel panel;

    public KeyChecker(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (GameState.state) {
            case CONTROLS:
                panel.getMenu().keyPressed(e);
                break;
            case PLAYING:
                panel.keyPressed(e);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (GameState.state) {
            case CONTROLS:
                break;
            case PLAYING:
                panel.keyReleased(e);
                break;
        }
    }
}
