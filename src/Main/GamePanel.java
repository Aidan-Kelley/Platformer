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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import Main.Player.Player;

public class GamePanel extends JPanel implements ActionListener {

    private Player player;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    public int cameraX;
    private int offset;
    private Timer gameTimer;
    private Rectangle restartRect, homeRect;
    private Font buttonFont = new Font("Arial", Font.BOLD, 30);
    private Spiny spiny = new Spiny(350, 556);
    private GameState state = GameState.GAME;

    public enum GameState {
        GAME, CONTROLS
    }

    public GamePanel() {

        restartRect = new Rectangle(550, 25, 50, 50);
        homeRect = new Rectangle(625, 25, 50, 50);

        player = new Player(400, 300, this);
        reset();
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                switch (state) {
                    case GAME:
                        gameLoop();
                        break;

                    case CONTROLS:
                        break;
                }
                repaint();
            }

        }, 0, 17);
    }

    public void reset() {
        player.reset();
        tiles.clear();
        cameraX = 150;
        offset = -150;
        makeWalls(offset);
    }

    private void gameLoop() {
        if (tiles.get(tiles.size() - 1).x < 800) {
            offset += 700;
            makeWalls(offset);
        }

        player.set();
        for (Tile tile : tiles)
            tile.set(cameraX);
        // spiny.set(cameraX);

        for (int i = 0; i < tiles.size(); i++) {
            if (tiles.get(i).x < -800)
                tiles.remove(i);
        }
    }

    public void makeWalls(int offset) {
        int size = 50;
        Random rand = new Random();
        int index = rand.nextInt(15);
        // for (int i = 0; i < 14; i++) {
        // tiles.add(new Tile(offset + i * size, 600, size, size, Tile.Type.WALL));

        // }
        if (index == 14) {
            for (int i = 0; i < 14; i++)
                if (i < 6)
                    tiles.add(new Tile(offset + i * size, 600, size, size,
                            Tile.Type.WALL));
                else
                    for (int j = 0; j < i - 5; j++) {
                        tiles.add(new Tile(offset + i * size, 600 - j * size, size, size,
                                Tile.Type.WALL));
                    }
        } else {
            for (int i = 0; i < 14; i++) {
                for (int j = 0; j < rand.nextInt(9); j++)
                    if (j == index / 3 && rand.nextInt(index + 1) < 6) // j == index / 3
                        continue;
                    else
                        tiles.add(new Tile(offset + i * size, 600 - j * size, size, size,
                                Tile.Type.WALL));
            }
        }
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        player.draw(gtd);
        for (Tile t : tiles)
            t.draw(gtd);
        // spiny.draw(gtd);
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
        if (e.getKeyChar() == 'w')
            player.keyJump = true;
        if (e.getKeyChar() == 's')
            player.keyDown = true;
        if (e.getKeyChar() == 'd')
            player.keyRight = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            player.keyJump = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            player.keyAbility = true;
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
            System.out.println(cameraX);

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'a')
            player.keyLeft = false;
        if (e.getKeyChar() == 'w')
            player.keyJump = false;
        if (e.getKeyChar() == 's')
            player.keyDown = false;
        if (e.getKeyChar() == 'd')
            player.keyRight = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            player.keyJump = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            player.keyAbility = false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }

    public void mouseClicked(MouseEvent e) {
        if (restartRect.contains(new Point(e.getPoint().x, e.getPoint().y - 27)))
            reset();
        else if (homeRect.contains(new Point(e.getPoint().x, e.getPoint().y - 27)))
            state = GameState.CONTROLS;
    }
}
