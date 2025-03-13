import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;
import java.util.Random;


public class EvolutionSimulator extends JPanel implements ActionListener {
    private Timer timer;
    private List<Species> speciesList;
    private List<Plant> plantList;

    private Environment env;


    public EvolutionSimulator() {
        speciesList = new ArrayList<>();
        plantList = new ArrayList<>();

        // Initialize your species here (plants, herbivores, carnivores, omnivores)
        initSpecies();

        // Set up a timer to refresh the simulation, e.g., every 30 ms (~33 FPS)
        timer = new Timer(30, this);
        timer.start();
    }

    private void initSpecies() {
        // Example: Adding a plant species (colored green dot)
        Random rand = new Random();

        env = new Environment();
        env.addPlant(new Plant(200, 200));
        env.addPlant(new Plant(200, 250));

        //Size,Sight,Smell
        //
        // ReproducingCost,MatingThreshold,DirectionBias,CooldownTime
        ArrayList<Integer> herbivoreGenome = new ArrayList<Integer>(Arrays.asList(3,100,200,75,80,200));
        ArrayList<Integer> carnivoreGenome = new ArrayList<Integer>(Arrays.asList(3,100,200,75,80,200));
        for (int i = 0; i < 4; i++) {
            env.addHerbivore(new Herbivore(rand.nextInt(800), rand.nextInt(600), new Genome(herbivoreGenome)));
        }

        for (int i = 0; i < 15; i++) {
            env.addPlant(new Plant(rand.nextInt(800), rand.nextInt(600)));
        }

        for (int i = 0; i < 2; i++) {
            env.addCarnivores(new Carnivore(rand.nextInt(800), rand.nextInt(600),new Genome(carnivoreGenome)));
        }
        //env.addCarnivores(new Carnivore(500,500,new Genome(new ArrayList<Integer>(Arrays.asList(3,100)))));



        // Add other species similarly...
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update plants
        for (Plant p : new ArrayList<>(env.getPlants())) {
            p.update(env);
        }
        // Update herbivores
        for (Herbivore h : new ArrayList<>(env.getHerbivores())) {
            h.update(env);
        }

        for (Carnivore c: new ArrayList<>(env.getCarnivores())) {
            c.update(env);
        }
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw each species on the panel.
        for (Plant p : env.getPlants()) {
            p.draw(g);
        }
        for (Herbivore h: env.getHerbivores()) {
            h.draw((g));
        }

        for (Carnivore c: env.getCarnivores()) {
            c.draw((g));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Evolution Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new EvolutionSimulator());
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
