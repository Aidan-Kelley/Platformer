package Main;

import javax.swing.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class MainFrame extends JFrame {

    public MainFrame() {
        var panel = new GamePanel();
        panel.setLocation(0, 0);
        panel.setSize(this.getSize());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setVisible(true);
        this.add(panel);

        addKeyListener(new KeyChecker(panel));
        addMouseListener(new MouseChecker(panel));
    }
}
