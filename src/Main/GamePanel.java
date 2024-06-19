package Main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    Player player;
    ArrayList<Wall> walls = new ArrayList<Wall>();
    int cameraX;
    int offset;
    Timer gameTimer;

    public GamePanel() {

        player = new Player(400, 300, this);

        reset();

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                if (walls.get(walls.size() - 1).x < 800) {
                    offset += 700;
                    makeWalls(offset);
                }

                player.set();
                for (Wall wall : walls)
                    wall.set(cameraX);

                for (int i = 0; i < walls.size(); i++) {
                    if (walls.get(i).x < -800)
                        walls.remove(i);
                }
                repaint();
            }

        }, 0, 17);
    }

    public void reset() {
        player.x = 200;
        player.y = 150;
        cameraX = 150;
        player.xVel = 0;
        player.yVel = 0;
        walls.clear();
        offset = -150;
        makeWalls(offset);
    }

    public void makeWalls(int offset) {
        int size = 50;
        Random rand = new Random();
        int index = rand.nextInt(14);
        for (int i = 0; i < 14; i++)
            for (int j = 0; j < rand.nextInt(7); j++)
                walls.add(new Wall(offset + i * size, 600 - j * size, size, size));

    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        player.draw(gtd);
        for (Wall w : walls)
            w.draw(gtd);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'a')
            player.keyLeft = true;
        if (e.getKeyChar() == 'w' || e.getKeyCode() == 39)
            player.keyUp = true;
        if (e.getKeyChar() == 's')
            player.keyDown = true;
        if (e.getKeyChar() == 'd')
            player.keyRight = true;
        if (e.getKeyChar() == 'p') {
            if (walls.get(walls.size() - 1).x < 800) {
                offset += 700;
                makeWalls(offset);
            }

            player.set();
            for (Wall wall : walls)
                wall.set(cameraX);

            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).x < -800)
                    walls.remove(i);
            }
            repaint();
        }
        if (e.getKeyChar() == 'r')
            reset();

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a')
            player.keyLeft = false;
        if (e.getKeyChar() == 'w' || e.getKeyCode() == 39)
            player.keyUp = false;
        if (e.getKeyChar() == 's')
            player.keyDown = false;
        if (e.getKeyChar() == 'd')
            player.keyRight = false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }
}
