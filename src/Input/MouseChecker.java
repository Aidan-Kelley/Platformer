package Input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Main.GamePanel;

public class MouseChecker extends MouseAdapter {

    GamePanel panel;

    public MouseChecker(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        panel.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

}
