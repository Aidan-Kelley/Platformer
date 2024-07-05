package Main;

import javax.swing.JFrame;

import Input.KeyChecker;
import Input.MouseChecker;

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
        var temp = new MouseChecker(panel);
        addMouseListener(temp);
        addMouseMotionListener(temp);
    }
}
