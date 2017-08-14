import java.util.ArrayList;
import java.util.List;

public class populationManager {

    geneRockets geneRocketsController;
    windowController windowController;

    public populationManager(geneRockets geneRocketsController, windowController windowController) {
        this.geneRocketsController = geneRocketsController;
        this.windowController = windowController;
    }

    public populationManager(geneRockets geneRocketsController) {
        this.geneRocketsController = geneRocketsController;
    }

    public void skipGeneration(List<Rocket> currentPopulation) {

        // To skip a generation the following should happen:
        // - The current population should disappear from the screen
        // - A new population should be generated from the current population
        // - This can be calculated by releasing it's vectors from its DNA.

        // First delete all current population from the screen.
        this.windowController.removeRocketFromWindow();

        // Now that we have the population, let's generate a new one without displaying it.
        // To do this we first need to loop through every current rocket.
        // Next we need to calculate the relative position until it crashes (into the walls or obstruction)

        for (Rocket rocket : currentPopulation) {



        }


    }
}
