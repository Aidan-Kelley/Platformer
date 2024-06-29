package Main;

import java.awt.Color;
import java.awt.Graphics2D;

import util.Calculate;
import util.SubPixelRectangle;

public class Player {

    public int x, y, cameraSubX = 0;
    public double yVel, temp;
    public int subXVel;
    public boolean keyLeft, keyRight, keyUp, keyDown, keyJump, keyAbility, reset;

    private GamePanel panel;
    private final int SUBS_PER_PIXEL = 64;
    private int width, height;
    private int mass;
    private double gravity = 0.5;
    private SubPixelRectangle hitBox;
    private Color color = Color.BLACK;
    private int framesUpPressed = 0;
    private int inAir = 0;
    private int framesJumping = 0;
    private PlayerState playerState;
    private int actionTimer = 0;
    private boolean skipXCollision = false;
    private boolean skipYCollision;

    private enum PlayerState {
        DEFAULT, CROUCH
    }

    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;

        width = 46;
        height = 90;
        mass = width * height;
        hitBox = new SubPixelRectangle(x, y, width, height);
        playerState = PlayerState.DEFAULT;
    }

    public void set() {
        // update in air status
        if (onGround())
            inAir = 0;
        else
            inAir++;

        if (inAir == 0) { // can only crouch/uncrouch if on ground
            if (keyDown) {
                if (playerState != PlayerState.CROUCH) {
                    setState(PlayerState.CROUCH);
                }
            } else {
                if (playerState != PlayerState.DEFAULT) {
                    setState(PlayerState.DEFAULT);
                    for (Tile tile : panel.tiles) {
                        if (hitBox.intersects(tile.hitBox)) {
                            setState(PlayerState.CROUCH);
                            break;
                        }
                    }

                }
            }
        }

        movement();

        if (!skipXCollision) {
            horizontalCollision();
        }
        skipXCollision = false;

        if (!skipYCollision) {
            verticalCollision();
        }
        skipYCollision = false;

        // move camera according to speed
        cameraSubX -= subXVel;
        panel.cameraX += Math.floorDiv(cameraSubX, SUBS_PER_PIXEL);
        cameraSubX = Math.floorMod(cameraSubX, SUBS_PER_PIXEL);

        y += yVel;
        hitBox.x = x;
        hitBox.y = y;

        if (y > 800) // check if player fell off screen
            reset = true;
        else {
            for (Tile tile : panel.tiles)
                if (tile.hitBox.intersects(hitBox)) {
                    if (tile.isHarmful) {
                        panel.reset();
                        break;
                    }
                    if (tile.getType() == Tile.Type.SPEED) {
                        subXVel = 9 * SUBS_PER_PIXEL;
                    }

                }
        }
        if (reset)
            panel.reset();
    }

    private void movement() {
        switch (playerState) {
            case DEFAULT:
                defaultMovement();
                break;
            case CROUCH:
                crouchMovement();
                break;
        }
    }

    private void defaultMovement() {
        int xAccel = 0;

        int dir = getDir();
        xAccel = getDefaultXAccel(dir);
        // speed cap
        if (subXVel < 448 && (subXVel + xAccel) * dir > 448)
            xAccel = 448 * dir - subXVel;

        subXVel += xAccel;
        yMovement();
    }

    private void crouchMovement() {
        int xAccel = 0;

        int dir = getDir();
        xAccel = getCrouchXAccel(dir);
        // speed cap
        if (subXVel < 448 && (subXVel + xAccel) * dir > 448)
            xAccel = 448 * dir - subXVel;
        subXVel += xAccel;

        yMovement();
    }

    private int getDir() {
        if (keyLeft && keyRight || (!keyLeft && !keyRight)) {
            if (Math.abs(subXVel) < 64 && inAir == 0)
                subXVel = 0;
            return 0;
        } else if (keyLeft) {
            return -1;
        } else if (keyRight) {
            return 1;
        }
        return 0;
    }

    private void yMovement() {
        double yAccel = gravity;
        yVel += yAccel;

        // y speed cap
        if (Math.abs(yVel) > 20)
            yVel = Math.signum(yVel) * 20;
        if (keyJump) {
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

        if (keyJump && yVel < 0)
            gravity = 1;
        else
            gravity = 2;
    }

    private void horizontalCollision() {
        hitBox.addSubX(subXVel);
        for (Tile tile : panel.tiles) {
            if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                if (Math.abs(subXVel) > 320) {
                    int cornerClip = 20;
                    hitBox.y -= cornerClip;
                    if (hitBox.intersects(tile.hitBox)) {
                        hitBox.y += cornerClip;
                        hitBox.subtractSubX(subXVel);
                        xPushOutOfWall(tile);
                    } else {
                        skipYCollision = true;
                        hitBox.subtractSubX(subXVel);
                        hitBox.y += cornerClip;
                        if (yVel >= -1) { // if in corner moving downward, snap up to floor, else do nothing
                            hitBox.x -= Math.signum(subXVel) * 1;
                            if (hitBox.intersects(tile.hitBox) || inAir <= 1) { // running over one block gaps
                                hitBox.addSubX(subXVel);
                                while (tile.hitBox.intersects(hitBox))
                                    hitBox.y -= 1;
                                yVel = 0;
                                y = hitBox.y;
                            } else {
                                xPushOutOfWall(tile);
                            }
                        }
                    }
                } else {
                    hitBox.subtractSubX(subXVel);
                    xPushOutOfWall(tile);
                }
            }
        }
    }

    private void verticalCollision() {
        hitBox.y += yVel;
        for (Tile tile : panel.tiles) {
            if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                hitBox.width = 16;
                int offset = (width - hitBox.width) / 2;
                hitBox.x += offset;
                if (playerIsMovingAwayFrom(tile) && yVel < 0 && !hitBox.intersects(tile.hitBox)) {
                    hitBox.x -= offset;
                    hitBox.width = width;
                    // panel.cameraX += x - hitBox.x;
                    if (Math.abs(subXVel) > 128) {
                        skipXCollision = true;
                    }
                } else {
                    hitBox.x -= offset;
                    hitBox.width = width;
                    hitBox.y -= yVel;
                    yPushOutOfWall(tile);
                }
            }
        }
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
        subXVel = 0;
        yVel = 0;
    }

    private int getDefaultXAccel(int dir) {
        if (dir == 0) { // not pressing left or right
            if (inAir == 0)
                return -Integer.signum(subXVel) * 29; // on ground friction
            else
                return 0;
        }
        if (subXVel * dir < 448) {
            if (subXVel * dir >= 1)
                return 19 * dir; // ground speeding up
            else if (inAir > 0)
                return 38 * dir; // air slowing down
            else
                return 77 * dir; // groudn slowing down
        } else if (subXVel > 448 && inAir == 0) {
            return -Integer.signum(subXVel) * 29; // slow down to running speed
        }
        return 0;
    }

    private int getCrouchXAccel(int dir) {
        if (dir == 0)
            if (inAir == 0)
                return -Integer.signum(subXVel) * 29;
            else
                return 0;
        if (inAir > 0) {
            if (subXVel * dir < 448)
                if (subXVel * dir >= 1)
                    return 19 * dir; // crouch air speed up
                else
                    return 38 * dir; // crouch air slow down
        } else {
            if (subXVel * dir < 64)
                return 19 * dir; // crouch walk
            else
                return -Integer.signum(subXVel) * 16; // slow down to crouch speed
        }
        return 0;
    }

    private void xPushOutOfWall(Tile wall) {
        if (Math.abs(subXVel) >= 1 && !wall.hitBox.intersects(hitBox)) {
            while (!wall.hitBox.intersects(hitBox))
                hitBox.x += Integer.signum(subXVel);
            hitBox.x -= Integer.signum(subXVel);
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
        subXVel = 0;
        panel.cameraX += x - hitBox.x;
    }

    private void yPushOutOfWall(Tile wall) {
        if (yVel != 0) {
            while (!wall.hitBox.intersects(hitBox))
                hitBox.y += Math.signum(yVel);
            hitBox.y -= Math.signum(yVel);
            yVel = 0;
        } else {
            int temp = hitBox.height;
            hitBox.height = 1;
            byte pushDirection;
            if (hitBox.intersects(wall.hitBox))
                pushDirection = 1;
            else
                pushDirection = -1;
            hitBox.height = temp;
            // while (wall.hitBox.intersects(hitBox))
            hitBox.x += pushDirection;
        }
        y = hitBox.y;
    }

    private void setState(PlayerState s) {
        switch (s) {
            case CROUCH:
                playerState = PlayerState.CROUCH;
                hitBox.height = 40;
                hitBox.y += height - 40;
                y = hitBox.y;
                break;

            case DEFAULT:
                playerState = PlayerState.DEFAULT;
                hitBox.height = height;
                hitBox.y -= height - 40;
                y = hitBox.y;
                break;
        }
    }

    private boolean onGround() {
        hitBox.y++; // check if one pixel off the ground
        for (Tile tile : panel.tiles)
            if (tile.hitBox.intersects(hitBox)) { // if touching ground
                hitBox.y--; // move up a pixel
                if (!tile.hitBox.intersects(hitBox)) { // check if player isn't inside the groudn
                    return true;
                }
                hitBox.y++;
            }
        return false;
    }

    private boolean playerIsMovingAwayFrom(Tile wall) {
        return (subXVel == 0) || (subXVel > 0 && hitBox.x > wall.hitBox.x) || (subXVel < 0 && hitBox.x < wall.hitBox.x);
    }

}
