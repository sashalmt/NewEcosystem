import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.*;

abstract class Species {
    protected int x, y;
    protected Color color;

    public Species(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public abstract void update(Environment env);

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, 10, 10); // Draw as a dot, adjust size as needed
    }
}
