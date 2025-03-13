import java.util.ArrayList;
import java.util.List;

public class Environment {
    private List<Plant> plants;
    private List<Herbivore> herbivores;
    private List<Carnivore> carnivores;


    public Environment() {
        plants = new ArrayList<>();
        herbivores = new ArrayList<>();
        carnivores = new ArrayList<>();

    }

    public List<Plant> getPlants() {
        return plants;
    }

    public List<Herbivore> getHerbivores() {
        return herbivores;
    }

    public List<Carnivore> getCarnivores() {
        return carnivores;
    }

    public void addPlant(Plant p) {
        plants.add(p);
    }

    public void addCarnivores(Carnivore c) {
        carnivores.add(c);
    }

    public void addHerbivore(Herbivore h) {
        herbivores.add(h);
    }

    public void removePlant(Plant p) {
        plants.remove(p);
    }
    public void removeHerbivore(Herbivore h) {
        herbivores.remove(h);
    }
    public void removeCarnivore(Carnivore c) {
        carnivores.remove(c);
    }



    // You might need to provide methods for removing herbivores as well.
}
