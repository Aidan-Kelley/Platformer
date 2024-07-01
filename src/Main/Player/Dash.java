package Main.Player;

import static util.Constants.SUBS_PER_PIXEL;

public class Dash extends Ability {

    private Player player;

    public Dash(Player p) {
        super(1, 11, 5, 18);
        player = p;
    }

    @Override
    public void init() {
        player.yVel = 0;
        player.framesJumping = 0;
    }

    @Override
    public void run() {
        player.xSubVel += 154 * player.inputDirection;
        if (Math.abs(player.xSubVel) > 15 * SUBS_PER_PIXEL)
            player.xSubVel = player.inputDirection * 15 * SUBS_PER_PIXEL;
    }

    @Override
    public void end() {
        player.xSubVel = 0;
        player.yMovement();
    }
}
