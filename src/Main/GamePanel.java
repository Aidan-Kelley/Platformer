package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    Player player;
    ArrayList<Tile> tiles = new ArrayList<Tile>();
    int cameraX;
    int offset;
    Timer gameTimer;
    Rectangle restartRect, homeRect;
    Font buttonFont = new Font("Arial", Font.BOLD, 30);

    public GamePanel() {

        restartRect = new Rectangle(550, 25, 50, 50);
        homeRect = new Rectangle(625, 25, 50, 50);

        player = new Player(400, 300, this);

        reset();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (tiles.get(tiles.size() - 1).x < 800) {
                    offset += 700;
                    makeWalls(offset);
                }

                player.set();
                for (Tile tile : tiles)
                    tile.set(cameraX);

                for (int i = 0; i < tiles.size(); i++) {
                    if (tiles.get(i).x < -800)
                        tiles.remove(i);
                }
                repaint();
            }

        }, 0, 17);
    }

    public void reset() {
        player.init();
        tiles.clear();
        cameraX = 150;
        offset = -150;
        makeWalls(offset);
    }

    public void makeWalls(int offset) {
        int size = 50;
        Random rand = new Random();
        int index = rand.nextInt(14);
        for (int i = 0; i < 14; i++) {
            // tiles.add(new Wall(offset + i * size, 600, size, size));
            for (int j = 0; j < rand.nextInt(7); j++)
                if (j == 1 && rand.nextInt(index + 1) < 6)
                    tiles.add(new Lava(offset + i * size, 600 - j * size, size, size));
                else
                    tiles.add(new Wall(offset + i * size, 600 - j * size, size, size));
            // if (rand.nextInt(10) > 6)
            // tiles.add(new Wall(offset + i * size, 300, size, size));

        }

    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        player.draw(gtd);
        for (Tile t : tiles)
            t.draw(gtd);

        gtd.setColor(Color.BLACK);
        gtd.drawRect(550, 25, 50, 50);
        gtd.drawRect(625, 25, 50, 50);
        gtd.setColor(Color.WHITE);
        gtd.fillRect(551, 26, 48, 48);
        gtd.fillRect(626, 26, 48, 48);
        gtd.setColor(Color.BLACK);
        gtd.setFont(buttonFont);
        gtd.drawString("R", 564, 60);
        gtd.drawString("H", 639, 60);
        gtd.drawString("" + player.xSubVel, 639, 120);
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
            if (tiles.get(tiles.size() - 1).x < 800) {
                offset += 700;
                makeWalls(offset);
            }

            player.set();
            for (Tile tile : tiles)
                tile.set(cameraX);

            for (int i = 0; i < tiles.size(); i++) {
                if (tiles.get(i).x < -800)
                    tiles.remove(i);
            }
            repaint();
        }
        if (e.getKeyChar() == 'r')
            player.reset = true;
        if (e.getKeyChar() == 'o')
            System.out.println('o');

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

    public void mouseClicked(MouseEvent e) {
        if (restartRect.contains(new Point(e.getPoint().x, e.getPoint().y - 27)))
            reset();
    }
}
