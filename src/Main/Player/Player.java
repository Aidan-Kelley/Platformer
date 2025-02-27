package Main.Player;

import static util.Constants.SUBS_PER_PIXEL;

import java.awt.Color;
import java.awt.Graphics2D;

import Main.GamePanel;
import Main.Tile;
import Main.Tile.Type;
import util.SubPixelRectangle;

public class Player {

    public int x, y, cameraSubX = 0;
    public double yVel, temp;
    public int xSubVel;
    public boolean keyLeft, keyRight, keyUp, keyDown, keyJump, keyAbility, reset;
    protected int inputDirection;
    protected int actionTimer, coolDownTimer;
    protected int inAir = 0;
    protected int framesJumping = 0;
    protected MovementState state;
    private GamePanel panel;
    private int width, height;
    private int mass;
    private double gravity = 0.5;
    private SubPixelRectangle hitBox;
    private Color color = Color.BLACK;
    private int framesUpPressed = 0;
    private boolean skipXCollision = false;
    private boolean skipYCollision;
    private AbilityRunner abilityRunner;

    public enum MovementState {
        NORMAL, CROUCH, DASH
    }

    public Player(int x, int y, GamePanel panel) {
        this.panel = panel;
        this.x = x;
        this.y = y;

        width = 46;
        height = 90;
        mass = width * height;
        hitBox = new SubPixelRectangle(x, y, width, height);
        state = MovementState.NORMAL;
        abilityRunner = new AbilityRunner(this);
    }

    public void set() {
        // update in air status
        if (onGround())
            inAir = 0;
        else
            inAir++;

        inputDirection = getInputDirection();

        if (actionTimer > 0)
            actionTimer--;
        if (coolDownTimer > 0)
            coolDownTimer--;

        if (coolDownTimer <= 0 && state == MovementState.NORMAL) {
            color = Color.BLACK;
            if (keyAbility && inputDirection != 0)
                if (state != MovementState.DASH) {
                    state = MovementState.DASH;
                    abilityRunner.setAbility(new Dash(this));
                    coolDownTimer = 50;
                }
        }

        movement();
        if (!skipXCollision)
            horizontalCollision();
        skipXCollision = false;

        if (!skipYCollision)
            verticalCollision();
        skipYCollision = false;

        hitBox.addSubX(xSubVel);
        panel.cameraX += x - hitBox.x;

        y += yVel;
        hitBox.x = x;
        hitBox.y = y;

        if (y > 800) // check if player fell off screen
            respawn();
        else {
            for (Tile tile : panel.getTiles())
                if (tile.hitBox.intersects(hitBox)) {
                    if (tile.isHarmful) {
                        panel.reset();
                        break;
                    }
                    if (tile.getType() == Tile.Type.SPEED) {
                        xSubVel = 9 * SUBS_PER_PIXEL;
                    }

                }
        }
        if (reset)
            panel.reset();
    }

    private void movement() {
        switch (state) {
            case NORMAL:
                defaultMovement();
                break;
            case CROUCH:
                crouchMovement();
                break;
            case DASH:
                abilityRunner.update();
                break;
        }
    }

    private void defaultMovement() {
        int xAccel = 0;
        setCrouching(keyDown);
        xAccel = getDefaultXAccel(inputDirection);
        // speed cap
        if (xSubVel < 448 && (xSubVel + xAccel) * inputDirection > 448)
            xAccel = 448 * inputDirection - xSubVel;

        xSubVel += xAccel;
        yMovement();
    }

    private void crouchMovement() {
        int xAccel = 0;
        setCrouching(keyDown);
        xAccel = getCrouchXAccel(inputDirection);
        // speed cap
        if (xSubVel < 448 && (xSubVel + xAccel) * inputDirection > 448)
            xAccel = 448 * inputDirection - xSubVel;
        xSubVel += xAccel;

        yMovement();
    }

    private int getInputDirection() {
        if (keyLeft && keyRight || (!keyLeft && !keyRight)) {
            if (Math.abs(xSubVel) < 64 && inAir == 0)
                xSubVel = 0;
            return 0;
        } else if (keyLeft) {
            return -1;
        } else if (keyRight) {
            return 1;
        }
        return 0;
    }

    protected void yMovement() {
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
                if ((framesJumping <= 3)) {
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

    // goofy collisions
    private void horizontalCollision() {
        boolean temporary = true;
        hitBox.addSubX(xSubVel);
        for (Tile tile : panel.getTiles()) {
            if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                if (Math.abs(xSubVel) >= 320) {
                    int cornerClip = 25;
                    hitBox.y -= cornerClip;
                    if (hitBox.intersects(tile.hitBox)) {
                        hitBox.y += cornerClip;
                        hitBox.subtractSubX(xSubVel);
                        xPushOutOfWall(tile);
                        return;
                    } else {
                        if (yVel < 0) { // if moving upwards, allow corner clip
                            skipYCollision = true;
                            hitBox.subtractSubX(xSubVel);
                            hitBox.y += cornerClip;
                            temporary = false;
                        } else { // moving downwards push up
                            hitBox.y += cornerClip;
                            while (tile.hitBox.intersects(hitBox))
                                hitBox.y -= 1;
                            yVel = 0;
                            y = hitBox.y;
                        }

                    }
                } else {
                    hitBox.subtractSubX(xSubVel);
                    xPushOutOfWall(tile);
                    return;
                }
            }
        }
        if (temporary)
            hitBox.subtractSubX(xSubVel);

    }

    private void xPushOutOfWall(Tile wall) {
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
            hitBox.x += pushDirection;
        }
        xSubVel = 0;
        panel.cameraX += x - hitBox.x;
        hitBox.x = x;
    }

    private void verticalCollision() {
        hitBox.y += yVel;
        for (Tile tile : panel.getTiles()) {
            if (tile.isSolid && hitBox.intersects(tile.hitBox)) {
                hitBox.width = 16;
                int offset = (width - hitBox.width) / 2;
                hitBox.x += offset;
                if (playerIsMovingAwayFrom(tile) && yVel < 0 && !hitBox.intersects(tile.hitBox)) {
                    hitBox.x -= offset;
                    hitBox.width = width;
                    // panel.cameraX += x - hitBox.x;
                    if (Math.abs(xSubVel) > 128) {
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
            hitBox.y += pushDirection;
        }
        y = hitBox.y;
    }

    private int getDefaultXAccel(int dir) {
        if (dir == 0) { // not pressing left or right
            if (inAir == 0)
                return -Integer.signum(xSubVel) * 29; // on ground friction
            else
                return 0;
        }
        if (xSubVel * dir < 448) {
            if (xSubVel * dir >= 1)
                return 19 * dir; // ground speeding up
            else if (inAir > 0)
                return 35 * dir; // air slowing down
            else
                return 77 * dir; // groudn slowing down
        } else if (xSubVel > 448 && inAir == 0) {
            return -Integer.signum(xSubVel) * 29; // slow down to running speed
        }
        return 0;
    }

    private int getCrouchXAccel(int dir) {
        if (dir == 0)
            if (inAir == 0)
                return -Integer.signum(xSubVel) * 29;
            else
                return 0;
        if (inAir > 0) {
            if (xSubVel * dir < 448)
                if (xSubVel * dir >= 1)
                    return 19 * dir; // crouch air speed up
                else
                    return 35 * dir; // crouch air slow down
        } else {
            if (xSubVel * dir < 64)
                return 19 * dir; // crouch walk
            else
                return -Integer.signum(xSubVel) * 16; // slow down to crouch speed
        }
        return 0;
    }

    private void setCrouching(boolean setCrouch) {
        if (inAir == 0)
            if (setCrouch) {
                if (state != MovementState.CROUCH) {
                    state = MovementState.CROUCH;
                    hitBox.y += height - 40;
                    hitBox.height = 40;
                    y = hitBox.y;
                }
            } else {
                if (state == MovementState.CROUCH) {
                    hitBox.y -= height - 40;
                    for (Tile tile : panel.getTiles()) {
                        if (hitBox.intersects(tile.hitBox)) {
                            hitBox.y += height - 40;
                            return;
                        }
                    }
                    hitBox.height = height;
                    y = hitBox.y;
                    state = MovementState.NORMAL;
                }
            }
    }

    public void setState(MovementState s) {
        state = s;
    }

    private void respawn() {
        panel.cameraX += 450;
        if (panel.cameraX > 350)
            panel.cameraX = 350;
        reset();
    }

    public void reset() {
        reset = false;
        x = 200;
        y = -100;
        hitBox.x = x;
        hitBox.y = y;
        xSubVel = 0;
        yVel = 0;
    }

    public void draw(Graphics2D gtd) {
        if (state == MovementState.DASH) {
            gtd.setColor(new Color(149, 149, 149));
            gtd.fillRect(x - xSubVel / SUBS_PER_PIXEL * 10 / 4, y, width, hitBox.height);
            gtd.setColor(Color.GRAY);
            gtd.fillRect(x - xSubVel / SUBS_PER_PIXEL * 2, y, width, hitBox.height);
            gtd.setColor(new Color(107, 107, 107));
            gtd.fillRect(x - xSubVel / SUBS_PER_PIXEL * 6 / 4, y, width, hitBox.height);
            gtd.setColor(new Color(85, 85, 85));
            gtd.fillRect(x - xSubVel / SUBS_PER_PIXEL * 4 / 4, y, width, hitBox.height);
            gtd.setColor(Color.DARK_GRAY);
            gtd.fillRect(x - xSubVel / SUBS_PER_PIXEL * 2 / 4, y, width, hitBox.height);
        }
        gtd.setColor(color);
        gtd.fillRect(x, y, width, hitBox.height);
    }

    private boolean onGround() {
        hitBox.y++; // check if one pixel off the ground
        for (Tile tile : panel.getTiles())
            if (tile.hitBox.intersects(hitBox)) { // if touching ground
                hitBox.y--; // move up a pixel
                if (!tile.hitBox.intersects(hitBox)) { // check if player isn't inside the groudn
                    return true;
                }
                hitBox.y++;
            }
        hitBox.y--;
        return false;
    }

    private boolean playerIsMovingAwayFrom(Tile wall) {
        return (xSubVel == 0) || (xSubVel > 0 && hitBox.x > wall.hitBox.x) || (xSubVel < 0 && hitBox.x < wall.hitBox.x);
    }
}