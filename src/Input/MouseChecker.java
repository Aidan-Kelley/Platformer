package Input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Main.GamePanel;
import Main.GameState;

public class MouseChecker extends MouseAdapter {

    GamePanel panel;

    public MouseChecker(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state) {
            case CONTROLS:
                panel.getMenu().mouseClicked(e);
                break;

            case PLAYING:
                panel.mouseClicked(e);
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case CONTROLS:
                panel.getMenu().mouseMoved(e);
                break;

            case PLAYING:
                break;
        }
    }

}
