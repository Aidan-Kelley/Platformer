package Main.Abilities;

import Main.Player;

public class Dash extends Ability {

    private Player player;

    public Dash(Player p) {
        super(0, 11, 5, 18);
        player = p;
    }

    @Override
    public void init() {
        player.dashInit();
    }

    @Override
    public void run() {
        player.dashRun();
    }

    @Override
    public void end() {
        player.dashEnd();
    }
}
