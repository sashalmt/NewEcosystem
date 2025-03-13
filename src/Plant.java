import java.awt.*;
import java.util.Random;

public class Plant extends Species {
    private static final double REPRODUCTION_PROBABILITY = 0.004; // chance to reproduce per update
    private Random random = new Random();

    // Optional: a genome for plants if you want to mutate certain traits.
    // For now, we'll assume plants are static in appearance.

    public Plant(int x, int y) {
        super(x, y, Color.GREEN);
    }

    @Override
    public void update(Environment env) {
        // Plants could have a growth mechanic here.
        // Also, try to reproduce with a small probability.
        if (random.nextDouble() < REPRODUCTION_PROBABILITY) {
            // Attempt to reproduce: create a new plant near this one
            int newX = this.x + random.nextInt(31) - 10;  // spawn near parent (±10 pixels)
            int newY = this.y + random.nextInt(31) - 10;
            Plant offspring = new Plant(newX, newY);
            // Optionally, add mutation of some traits here.
            env.addPlant(offspring);
        }
    }
}
