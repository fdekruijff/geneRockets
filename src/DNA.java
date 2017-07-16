import com.sun.javafx.geom.Vec2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DNA {

    private List<Gene> genes;
    private Random random;

    public DNA() {
        this.genes = new ArrayList<>();
        this.random = new Random();
        this.newGenes();
    }

    public DNA(List<Gene> genes) {
        this.genes = genes;
        this.random = new Random();
    }

    private void newGenes() {
        // TODO: Genes have to be generated more specifically after generation increase

        for (int x = 0; x < 100; x++) {
            this.genes.add(
                    new Gene(new Vec2d(
                            random.nextDouble() * 1 - 0.5,
                            random.nextDouble() * -1
                    ))
            );
        }
    }

    public List<Gene> getGenes() {
        return genes;
    }

}
