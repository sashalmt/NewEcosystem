import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Herbivore extends Species {
    private static final int COLLISION_DISTANCE = 5; // threshold to "eat" the plant
    private static final int MATING_THRESHOLD = 200;    // satiation required to mate
    private static final int REPRODUCTION_COST = 75;    // cost in satiation for reproducing
    private static final int COOLDOWN_TIME = 200;        // frames to wait before mating again
    private static final int DIRECTION_BIAS = 80;

    private int satiation = 450;
    private int matingCooldown = 0;
    private Genome genome;
    private Plant targetFood;
    private Herbivore targetMate;
    private Random random = new Random();
    private int pdx;
    private int pdy;
    private int speed;
    private int sightRange;
    private int reproducingCost;
    private int matingThreshold;
    private int directionBias;
    private int cooldownTime;



    public Herbivore(int x, int y, Genome genome) {
        super(x, y, Color.ORANGE);
        this.genome = genome;
        sequence(genome);


    }

    private void sequence(Genome genome) {
        this.speed = this.genome.genome.get(0);
        this.sightRange = this.genome.genome.get(1);
        this.reproducingCost = this.genome.genome.get(2);
        this.matingThreshold = this.genome.genome.get(3);
        this.directionBias = this.genome.genome.get(4);
        this.cooldownTime = this.genome.genome.get(5);

    }

    @Override
    public void update(Environment env) {
        // Decrement mating cooldown if active
        if (satiation < 0) {
            env.removeHerbivore(this);
            return;
        }

        satiation--;

        if (matingCooldown > 0) {
            matingCooldown--;
        }

        // Check for food consumption
        List<Plant> plants = env.getPlants();

        if (targetMate != null) {
            moveToward(targetMate,this.speed);
            if (distanceTo(targetMate) < COLLISION_DISTANCE*10) {
                Herbivore offspring = reproduceWith(targetMate);
                env.addHerbivore(offspring);
                satiation -= this.reproducingCost;
                targetMate.satiation -= this.reproducingCost;
                this.matingCooldown = this.cooldownTime;
                targetMate.matingCooldown = this.cooldownTime;
                targetMate = null;
            }
        } if (targetFood != null) {
            moveToward(targetFood, this.speed);
            // Check if reached the plant (collision)
            if (distanceTo(targetFood) < COLLISION_DISTANCE) {
                // Consume the plant: remove it from the environment and increase satiation
                env.removePlant(targetFood);
                satiation += 50;  // adjust nutrition value as needed
                targetFood = null;  // reset target
            }
        } else {
            // No food found, perform a random search
            randomMove(this.speed);
        }

        // Check if satiation is high enough for reproduction
        if (satiation >= this.matingThreshold && matingCooldown == 0) {
            targetMate = findMate(env.getHerbivores());

        } else if (targetFood == null || !plants.contains(targetFood)) {
            targetFood = findNearestFood(plants, this.sightRange);
        }
    }

    private Plant findNearestFood(List<Plant> plants, int sightRange) {
        Plant nearest = null;
        int sightRangeSquared = sightRange * sightRange;
        double nearestDistanceSquared = sightRangeSquared;
        for (Plant p : plants) {
            int dx = p.x - this.x;
            int dy = p.y - this.y;
            double distSq = dx * dx + dy * dy;
            if (distSq < nearestDistanceSquared) {
                nearestDistanceSquared = distSq;
                nearest = p;
            }
        }
        return nearest;
    }

    private void moveToward(Plant p, int speed) {
        int dx = p.x - this.x;
        int dy = p.y - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            this.x += (int)(speed * dx / distance);
            this.y += (int)(speed * dy / distance);
        }
    }

    private void moveToward(Herbivore h, int speed) {
        int dx = h.x - this.x;
        int dy = h.y - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            this.x += (int)(speed * dx / distance);
            this.y += (int)(speed * dy / distance);
        }
    }

    private double distanceTo(Plant p) {
        int dx = p.x - this.x;
        int dy = p.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double distanceTo(Herbivore h) {
        int dx = h.x - this.x;
        int dy = h.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }


    private void randomMove(int speed) {
        // Small random movement when no food is targeted
        if (random.nextInt(100) > this.directionBias) {
            this.pdx = random.nextInt(1 + (2 * speed)) - speed;
            this.pdy = random.nextInt(1 + (2 * speed)) - speed;

        }
        this.x += this.pdx;
        this.y += this.pdy;
        if (x < 0) {
            this.x += speed;
        }
        if (x > 800) {
            this.x -= speed;
        }
        if (y < 0) {
            this.y += speed;
        }
        if (y > 600) {
            this.y -= speed;
        }
    }

    // Find a mate: for simplicity, look for any other herbivore above the mating threshold and close by
    private Herbivore findMate(List<Herbivore> herbivores) {
        for (Herbivore h : herbivores) {
            if (h != this && h.satiation >= this.matingThreshold && h.matingCooldown == 0) {
                int dx = h.x - this.x;
                int dy = h.y - this.y;
                if (dx * dx + dy * dy < 10000) { // example: within 100 pixels (100^2)
                    return h;
                }
            }
        }
        return null;
    }

    // Produce an offspring with a combined (or averaged) genome and a slight mutation.
    private Herbivore reproduceWith(Herbivore mate) {
        // For example, average the traits
        int childSpeed = (this.speed + mate.speed) / 2;
        int childSight = (this.sightRange + mate.sightRange) / 2;
        int childReproducingCost = (this.reproducingCost + mate.reproducingCost) / 2;
        int childMatingThreshold = (this.matingThreshold + mate.matingThreshold) / 2;
        int childDirectionBias = (this.directionBias + mate.directionBias) / 2;
        int childCooldownTime = (this.cooldownTime + mate.cooldownTime) / 2;

        Genome childGenome = new Genome(new ArrayList<Integer>(Arrays.asList(childSpeed,childSight,childReproducingCost,childMatingThreshold,childDirectionBias,childCooldownTime)));
        childGenome.mutate(); // apply slight mutation

        // Create offspring at a position near the parents
        int childX = this.x + random.nextInt(21) - 10;  // within ±10 pixels
        int childY = this.y + random.nextInt(21) - 10;
        return new Herbivore(childX, childY, childGenome);
    }
        
}
