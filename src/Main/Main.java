package Main;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Main {
    public static void main(String... args) {
        MainFrame frame = new MainFrame();
        frame.setTitle("Sean");
        frame.setSize(700, 700);
        // center window on screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int) (screenSize.getWidth() / 2 -
                frame.getSize().getWidth() / 2),
                (int) (screenSize.getHeight() / 2 - frame.getSize().getHeight() / 2));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
