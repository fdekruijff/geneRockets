import com.sun.javafx.geom.Vec2d;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DNA {

    private List<Vec2d> genes;
    private Random random;

    public DNA() {
        this.genes = new ArrayList<>();
        this.random = new Random();
        this.newGenes();
    }

    public DNA(List<Vec2d> genes) {
        this.genes = genes;
        this.random = new Random();
    }

    private void newGenes() {
        for (int x = 0; x < 1; x++) {
            this.genes.add(
                    new Vec2d(random.nextDouble() * 1 - 0.5,random.nextDouble() * -1)
            );
        }
    }

    public List<Vec2d> getGenes() {
        return genes;
    }
}
