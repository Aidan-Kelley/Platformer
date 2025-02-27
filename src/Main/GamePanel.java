package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Menu;
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

import static Input.InputKey.*;

import Main.Player.Ability;
import Main.Player.Player;
import ui.ControlsMenu;
import util.Constants;

public class GamePanel extends JPanel implements ActionListener {

    private Player player;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    public int cameraX;
    private int offset;
    private Timer gameTimer;
    private Rectangle restartRect, homeRect;
    private Spiny spiny = new Spiny(350, 556);
    private ControlsMenu controlsMenu = new ControlsMenu();

    public GamePanel() {

        restartRect = new Rectangle(550, 25, 50, 50);
        homeRect = new Rectangle(625, 25, 50, 50);

        player = new Player(400, 300, this);
        reset();

        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                switch (GameState.state) {
                    case PLAYING:
                        gameLoop();
                        break;

                    case CONTROLS:
                        controlsMenu.update();
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

    private void makeWalls(int offset) {
        int size = 50;
        Random rand = new Random();
        int index = rand.nextInt(15);
        // for (int i = 0; i < 14; i++) {
        // tiles.add(new Tile(offset + i * size, 600, size, size, Tile.Type.WALL));

        // }
        if (index == 14) { // staircase
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

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D gtd = (Graphics2D) g;

        switch (GameState.state) {
            case PLAYING:
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
                gtd.setFont(Constants.buttonFont);
                gtd.drawString("R", 564, 60);
                gtd.drawString("C", 639, 60);
                gtd.drawString("" + player.xSubVel, 639, 120);
                break;
            case CONTROLS:
                controlsMenu.draw(gtd);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == LEFT.getKeyCode())
            player.keyLeft = true;
        if (e.getKeyCode() == UP.getKeyCode())
            player.keyJump = true;
        if (e.getKeyCode() == DOWN.getKeyCode())
            player.keyDown = true;
        if (e.getKeyCode() == RIGHT.getKeyCode())
            player.keyRight = true;
        if (e.getKeyCode() == JUMP.getKeyCode())
            player.keyJump = true;
        if (e.getKeyCode() == ABILITY.getKeyCode())
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
        if (e.getKeyCode() == LEFT.getKeyCode())
            player.keyLeft = false;
        if (e.getKeyCode() == UP.getKeyCode())
            player.keyJump = false;
        if (e.getKeyCode() == DOWN.getKeyCode())
            player.keyDown = false;
        if (e.getKeyCode() == RIGHT.getKeyCode())
            player.keyRight = false;
        if (e.getKeyCode() == JUMP.getKeyCode())
            player.keyJump = false;
        if (e.getKeyCode() == ABILITY.getKeyCode())
            player.keyAbility = false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

    }

    public void mouseClicked(MouseEvent e) {
        if (restartRect.contains(new Point(e.getPoint().x - 8, e.getPoint().y - 27)))
            reset();
        else if (homeRect.contains(new Point(e.getPoint().x - 8, e.getPoint().y - 27)))
            GameState.state = GameState.CONTROLS;
    }

    public ControlsMenu getMenu() {
        return controlsMenu;
    }
}
