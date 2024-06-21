package Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.Rectangle;
import java.net.SocketImpl;

public class Player {

    public int x, y, subX;
    public double yVel, temp;
    public int xSubVel;
    public boolean keyLeft, keyRight, keyDown, keyUp, reset;

    private GamePanel panel;
    private final int SUBPIXELS_PER_PIXEL = 64;
    private int width, height;
    private double gravity = 0.5;
    private int xAccel = 0;
    private double yAccel = gravity;
    private Rectangle hitBox;
    private Color color = Color.BLACK;
    private int framesUpPressed = 0;
    private int inAir = 0;
    private int framesJumping = 0;
    private State currentState;

    private boolean skipXCollision = false;;

    private enum State {
        CROUCH, STAND
    }

    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;

        width = 46; // 46
        height = 90; // 90
        hitBox = new Rectangle(x, y, width, height);
        currentState = State.STAND;
    }

    public void set() {
        int prev = panel.cameraX;
        xAccel = 0;
        if (inAir == 0) // can only crouch/uncrouch if on ground
            if (keyDown) {
                if (currentState != State.CROUCH) {
                    setState(State.CROUCH);
                }
            } else {
                if (currentState != State.STAND) {
                    setState(State.STAND);
                    for (Tile tile : panel.tiles) {
                        if (hitBox.intersects(tile.hitBox)) {
                            setState(State.CROUCH);
                            break;
                        }
                    }

                }
            }

        if (keyLeft && keyRight || (!keyLeft && !keyRight)) {
            if (Math.abs(xSubVel) < 64)
                xSubVel = 0;
            else if (currentState == State.CROUCH)
                xAccel = -Integer.signum(xSubVel) * 16;
            else if (inAir == 0)
                xAccel = -Integer.signum(xSubVel) * 29;
        } else if (keyLeft) {
            move(-1);
        } else if (keyRight) {
            move(1);
        }

        xSubVel += xAccel;

        yAccel = gravity;
        yVel += yAccel;

        if (Math.abs(yVel) > 20)
            yVel = Math.signum(yVel) * 20;
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
        if (!skipXCollision) {
            hitBox.x += subPixelToPixel(xSubVel);
            for (Tile tile : panel.tiles) {
                if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                    int cornerClip = 20;
                    hitBox.y -= cornerClip;
                    if (hitBox.intersects(tile.hitBox)) {
                        hitBox.y += cornerClip;
                        hitBox.x -= subPixelToPixel(xSubVel);
                        handleXCollision(tile);
                    } else if (Math.abs(xSubVel) > 320 && yVel < -1) {
                        skipYCollision = true;
                        hitBox.x -= subPixelToPixel(xSubVel);
                        hitBox.y += cornerClip;
                    } else {
                        skipYCollision = true;
                        hitBox.x -= subPixelToPixel(xSubVel);
                        hitBox.x -= Math.signum(xSubVel) * 1;
                        hitBox.y += cornerClip;
                        if (hitBox.intersects(tile.hitBox) || inAir <= 1) { // inAir check is for running over one block
                                                                            // gaps
                            hitBox.x += subPixelToPixel(xSubVel);
                            while (tile.hitBox.intersects(hitBox))
                                hitBox.y -= 1;
                            yVel = 0;
                            y = hitBox.y;
                        } else {
                            handleXCollision(tile);
                        }
                    }

                }
            }
        }
        skipXCollision = false;
        // Vertical Collision
        if (!skipYCollision) {
            hitBox.y += yVel;
            for (Tile tile : panel.tiles) {
                if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                    hitBox.width = 16;
                    int offset = (width - hitBox.width) / 2;
                    hitBox.x += offset;
                    if (playerIsMovingAwayFrom(tile) && yVel < 0 && !hitBox.intersects(tile.hitBox)) {
                        hitBox.x -= offset;
                        // byte pushDirection;
                        // if (hitBox.intersects(tile.hitBox))
                        // pushDirection = 1;
                        // else
                        // pushDirection = -1;
                        hitBox.width = width;
                        // while (tile.hitBox.intersects(hitBox))
                        // hitBox.x += pushDirection;
                        // hitBox.x -= subPixelToPixel(xSubVel);
                        panel.cameraX += x - hitBox.x;
                        if (Math.abs(xSubVel) > 128) {
                            skipXCollision = true;
                        }
                    } else {
                        hitBox.x -= offset;
                        hitBox.width = width;
                        hitBox.y -= yVel;
                        handelYCollision(tile);
                    }
                }
            }

        }
        // System.out.println(xSubVel / 64 + " " + xSubVel % 64);
        panel.cameraX -= subPixelToPixel(xSubVel);
        if (prev - panel.cameraX > 7)
            System.out.println(prev - panel.cameraX);
        y += yVel;
        hitBox.x = x;
        hitBox.y = y;

        if (y > 800)
            panel.reset();
        else {
            for (Tile tile : panel.tiles)
                if (tile.isHarmful && tile.hitBox.intersects(hitBox)) {
                    panel.reset();
                    break;
                }
        }
        if (reset)
            panel.reset();
    }

    private boolean playerIsMovingAwayFrom(Tile wall) {
        return (xSubVel == 0) || (xSubVel > 0 && hitBox.x > wall.hitBox.x) || (xSubVel < 0 && hitBox.x < wall.hitBox.x);
    }

    private void move(int dir) {
        switch (currentState) {
            case STAND:
                if (xSubVel * dir < 448) {
                    if (xSubVel * dir >= 1)
                        xAccel = 19 * dir; // ground speeding up
                    else if (inAir > 0)
                        xAccel = 38 * dir; // air slowing down
                    else
                        xAccel = 77 * dir; // groudn slowing down
                }
                break;

            case CROUCH:
                if (inAir > 0) {
                    if (xSubVel * dir < 448)
                        if (xSubVel * dir >= 1)
                            xAccel = 19 * dir; // crouch air speed up
                        else
                            xAccel = 38 * dir; // crouch air slow down
                } else {
                    if (xSubVel * dir < 64)
                        xAccel = 19 * dir; // crouch walk
                    else
                        xAccel = -Integer.signum(xSubVel) * 16; // slow down to crouch speed
                }
                break;
        }
    }

    private void handleXCollision(Tile wall) {
        if (Math.abs(xSubVel) >= 1 && !wall.hitBox.intersects(hitBox)) {
            while (!wall.hitBox.intersects(hitBox))
                hitBox.x += Integer.signum(xSubVel);
            hitBox.x -= Integer.signum(xSubVel);
        } else {
            hitBox.width = 1;
            byte pushDirection;
            if (hitBox.intersects(wall.hitBox))
                pushDirection = 1;
            else
                pushDirection = -1;
            hitBox.width = width;
            // while (wall.hitBox.intersects(hitBox))
            hitBox.x += pushDirection;
        }
        xSubVel = 0;
        panel.cameraX += x - hitBox.x;
    }

    private void handelYCollision(Tile wall) {
        if (yVel != 0) {
            while (!wall.hitBox.intersects(hitBox))
                hitBox.y += Math.signum(yVel);
            hitBox.y -= Math.signum(yVel);
            yVel = 0;
        } else {
            hitBox.height = 1;
            byte pushDirection;
            if (hitBox.intersects(wall.hitBox))
                pushDirection = 1;
            else
                pushDirection = -1;
            hitBox.height = height;
            // while (wall.hitBox.intersects(hitBox))
            hitBox.x += pushDirection;
        }
        y = hitBox.y;
    }

    private int subPixelToPixel(int amount) {
        return amount / SUBPIXELS_PER_PIXEL;
    }

    private void setState(State s) {
        switch (s) {
            case CROUCH:
                currentState = State.CROUCH;
                hitBox.height = 40;
                hitBox.y += height - 40;
                y = hitBox.y;
                break;

            case STAND:
                currentState = State.STAND;
                hitBox.height = height;
                hitBox.y -= height - 40;
                y = hitBox.y;
                break;
        }
    }

    private boolean onGround() {
        for (Tile tile : panel.tiles)
            if (tile.hitBox.intersects(hitBox)) {
                hitBox.y--;
                if (!tile.hitBox.intersects(hitBox)) {
                    hitBox.y++;
                    return true;
                }
                hitBox.y++;
            }
        return false;
    }

    public void draw(Graphics2D gtd) {
        gtd.setColor(color);
        gtd.fillRect(x, y, width, hitBox.height);
    }

    public void init() {
        reset = false;
        x = 200;
        y = -100;
        hitBox.x = x;
        hitBox.y = y;
        xSubVel = 0;
        yVel = 0;
    }
}
