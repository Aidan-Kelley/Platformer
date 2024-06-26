package util;

public class Calculate {

    public static int finalVelocity(int m1, int v1, int m2, int v2) {
        return ((m1 - m2) * v1 + 2 * m2 * v2) / (m1 + m2);
    }

}
