package Main;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface Enemy {

    public void set(int cameraX);

    public void draw(Graphics2D gtd);

    public Rectangle getHitbox();
}
