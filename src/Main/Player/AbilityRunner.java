package Main.Player;

import Main.Player.Player.MovementState;

public class AbilityRunner {

    private Ability ability;
    private int actionTimer;
    private Player player;

    public AbilityRunner(Player p) {
        player = p;
    }

    public void setAbility(Ability a) {
        ability = a;
        actionTimer = 0;
    }

    public void update() {
        if (actionTimer < ability.initFrames)
            ability.init();
        else if (actionTimer - ability.initFrames < ability.runFrames)
            ability.run();
        else if (actionTimer - ability.initFrames - ability.runFrames < ability.endFrames) {
            ability.end();
        }
        actionTimer++;
        if (actionTimer == ability.initFrames + ability.runFrames + ability.endFrames) {
            player.setState(MovementState.NORMAL);
            player.coolDownTimer = ability.coolDown;
        }
    }

}
