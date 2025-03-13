import java.util.ArrayList;
import java.util.Random;

public class Genome {
    public ArrayList<Integer> genome;

    private static final Random random = new Random();

    public Genome(ArrayList<Integer> genome) {
        this.genome = genome;
    }

    public Genome copy() {
        return new Genome(genome);
    }

    public void mutate() {
        // Apply a small random change to each trait
        for (int i = 0; i < genome.size(); i++) {
            int newGene = genome.get(i) + random.nextInt(3)-1;
            if (newGene < 0) {
                newGene = -1*newGene;
            }
            System.out.println(newGene);
            genome.set(i,newGene);

        }
    }
}
