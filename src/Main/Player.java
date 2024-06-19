package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player {

    GamePanel panel;
    int x, y;
    int width, height;
    double xVel, yVel;
    double gravity = 0.5;
    double xAccel = 0.0, yAccel = gravity;
    Rectangle hitBox;
    Color color = Color.BLACK;
    int framesUpPressed = 0;
    int inAir = 0;
    int framesJumping = 0;
    boolean keyLeft, keyRight, keyDown, keyUp;

    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;

        width = 46;
        height = 90;
        hitBox = new Rectangle(x, y, width, height);
    }

    public void set() {
        xAccel = 0;
        if (keyLeft && keyRight || (!keyLeft && !keyRight)) {
            if (Math.abs(xVel) < 1)
                xVel = 0;
            else if (inAir == 0)
                xAccel = -Math.signum(xVel) * 0.25;
        } else if (keyLeft) {
            if (xVel > -7) {
                if (xVel <= -1)
                    xAccel = -0.3;
                else if (inAir > 0)
                    xAccel = -0.6;
                else
                    xAccel = -1.2;
            }
        } else if (keyRight) {
            if (xVel < 7) {
                if (xVel >= 1)
                    xAccel = 0.3;
                else if (inAir > 0)
                    xAccel = 0.6;
                else
                    xAccel = 1.2;
            }
        }
        xVel += xAccel;

        yAccel = gravity;
        yVel += yAccel;

        if (Math.abs(yVel) > 20)
            yVel = Math.signum(yVel) * 20;
        else
            color = Color.BLACK;
        hitBox.y++;
        if (onGround())
            inAir = 0;
        else
            inAir++;
        hitBox.y--;
        if (keyUp) {
            if (framesUpPressed <= 1 && inAir == 0)
                framesJumping = 1;
            else if (framesJumping >= 1)
                framesJumping++;
            else
                framesJumping = 0;

            if (framesJumping > 0) {
                if ((framesJumping == 1)) {
                    yVel = -13;
                } else if (framesJumping < 6)
                    yVel = -14;
            }
            framesUpPressed++;
        } else {
            framesUpPressed = 0;
            framesJumping = 0;
        }
        if (keyUp && yVel < 0)
            gravity = 1;
        else
            gravity = 2;
        // Horizontal Collision
        boolean skipYCollision = false;
        hitBox.x += xVel;
        for (Wall wall : panel.walls) {
            if (hitBox.intersects(wall.hitBox)) {
                int cornerClip = 20;
                hitBox.y -= cornerClip;
                if (hitBox.intersects(wall.hitBox)) {
                    hitBox.y += cornerClip;
                    hitBox.x -= xVel;
                    handleXCollision(wall);
                } else if (Math.abs(xVel) > 5 && yVel < -1) {
                    skipYCollision = true;
                    hitBox.x -= xVel;
                    hitBox.y += cornerClip;
                } else {
                    skipYCollision = true;
                    hitBox.x -= xVel;
                    hitBox.x -= Math.signum(xVel) * 1;
                    hitBox.y += cornerClip;
                    if (hitBox.intersects(wall.hitBox) || inAir <= 1) { // inAir check is for running over one block
                                                                        // gaps
                        hitBox.x += xVel;
                        while (wall.hitBox.intersects(hitBox))
                            hitBox.y -= 1;
                        yVel = 0;
                        y = hitBox.y;
                    } else {
                        handleXCollision(wall);
                    }
                }

            }
        }

        // Vertical Collision
        if (!skipYCollision) {
            hitBox.y += yVel;
            for (Wall wall : panel.walls) {
                if (hitBox.intersects(wall.hitBox)) {
                    hitBox.width = 16;
                    int offset = (width - hitBox.width) / 2;
                    hitBox.x += offset;
                    if (Math.abs(xVel) <= 10 && yVel < 0 && !hitBox.intersects(wall.hitBox)) {
                        hitBox.x -= offset;
                        byte pushDirection;
                        if (hitBox.intersects(wall.hitBox))
                            pushDirection = 1;
                        else
                            pushDirection = -1;
                        hitBox.width = width;
                        while (wall.hitBox.intersects(hitBox))
                            hitBox.x += pushDirection;
                        hitBox.x -= xVel;
                        panel.cameraX += x - hitBox.x;
                    } else {
                        hitBox.x -= offset;
                        hitBox.width = width;
                        hitBox.y -= yVel;
                        while (!wall.hitBox.intersects(hitBox))
                            hitBox.y += Math.signum(yVel);
                        hitBox.y -= Math.signum(yVel);
                        yVel = 0;
                        y = hitBox.y;
                    }
                }
            }

        }
        panel.cameraX -= Math.round(xVel);
        y += yVel;
        hitBox.x = x;
        hitBox.y = y;

        if (y > 800)
            panel.reset();

    }

    private void handleXCollision(Wall wall) {
        if (xVel != 0) {
            while (!wall.hitBox.intersects(hitBox))
                hitBox.x += Math.signum(xVel);
            hitBox.x -= Math.signum(xVel);
            xVel = 0;
        }
        panel.cameraX += x - hitBox.x;
    }

    private boolean onGround() {
        for (Wall wall : panel.walls)
            if (wall.hitBox.intersects(hitBox)) {
                hitBox.y--;
                if (!wall.hitBox.intersects(hitBox)) {
                    hitBox.y++;
                    return true;
                }
                hitBox.y++;
            }
        return false;
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(color);
        gtd.fillRect(x, y, width, height);
    }
}
