package Input;

import java.awt.event.KeyEvent;

public enum InputKey {
    UP(KeyEvent.VK_W), DOWN(KeyEvent.VK_S), LEFT(KeyEvent.VK_A), RIGHT(KeyEvent.VK_D),
    JUMP(KeyEvent.VK_RIGHT),
    ABILITY(KeyEvent.VK_LEFT);

    private int keyCode;

    private InputKey(int keyCode) {
        this.keyCode = keyCode;
    }

    public void setKeyCode(int code) {
        keyCode = code;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public String getKeyText() {
        return KeyEvent.getKeyText(keyCode);
    }
}
