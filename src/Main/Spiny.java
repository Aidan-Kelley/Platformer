package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.Calculate;

public class Spiny implements Enemy {

    private final int WIDTH = 44 * 4; // 44
    private final int HEIGHT = 44 * 4; // 44
    private BufferedImage sprite;
    private int x, y;
    private int startX;
    public int xVel, yVel;
    private Rectangle hitbox;
    private int timer;
    public int mass;

    public Spiny(int x, int y) {
        try {
            sprite = ImageIO
                    .read(new File("C:\\Users\\aidan\\.vscode\\JavaProjects\\Platformer\\res\\pixil-frame-0 (1).png"));
        } catch (Exception e) {
            System.out.println("oh no");
        }
        timer = 0;
        this.x = x;
        this.y = y;
        xVel = -3;
        startX = x;
        mass = WIDTH * HEIGHT;
        hitbox = new Rectangle(x, y, WIDTH, HEIGHT);
    }

    @Override
    public void set(int cameraX) {
        startX += xVel;
        x = startX + cameraX;
        hitbox.x = x;
        hitbox.y = y;
        timer++;
    }

    @Override
    public void draw(Graphics2D gtd) {
        gtd.drawImage(sprite, x, y, null);
        gtd.setColor(Color.GRAY);
        gtd.fillRect(x, y, WIDTH, HEIGHT);
    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    public void collide(int m, int v) {
        xVel = Math.floorDiv(Calculate.finalVelocity(mass, xVel * 64, m, v), 64);
    }

}
