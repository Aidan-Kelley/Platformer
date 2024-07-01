package Main.Player;

public abstract class Ability {

    protected int initFrames, runFrames, endFrames, coolDown;

    public Ability(int initFrames, int runFrames, int endFrames, int coolDown) {
        this.initFrames = initFrames;
        this.runFrames = runFrames;
        this.endFrames = endFrames;
        this.coolDown = coolDown;
    }

    public abstract void init();

    public abstract void run();

    public abstract void end();
}