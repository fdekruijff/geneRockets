import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Rocket extends Rectangle {

    private Image rocketImage = new Image("rocket.png");
    private Vec2d position;
    private Vec2d velocity = new Vec2d(0, 0);
    private Vec2d acceleration = new Vec2d(0, 0);
    private DNA dna;
    private geneRockets geneRocketsController;
    private Random random = new Random();

    private int geneCount = 0;
    private double lastAcceleration = 0;
    private double flightTime = System.currentTimeMillis();
    private double flightStart = System.currentTimeMillis();
    private double setTime = System.currentTimeMillis();
    private double gravity = 0.045;
    private double fitness = 1;
    private boolean crashed = false;
    private boolean completed = false;

    public Rocket(geneRockets geneRocketsController) {
        this.geneRocketsController = geneRocketsController;
        this.dna = new DNA();
        this.initializeRocket();
    }

    public Rocket(geneRockets geneRocketsController, DNA dna) {
        this.geneRocketsController = geneRocketsController;
        this.dna = dna;
        this.initializeRocket();
    }

    private void initializeRocket() {
        this.position = new Vec2d(this.geneRocketsController.getWidth() / 2, this.geneRocketsController.getHeight() - 150);
        super.setWidth(32);
        super.setHeight(32);
        super.setFill(new ImagePattern(this.rocketImage));
        super.setTranslateX(this.position.x);
        super.setTranslateY(this.position.y);
    }

    public void update() {
        if (this.position.x <= 0 || this.position.x >= (this.geneRocketsController.getWidth() - 30) || this.position.y <= 0 ||
                this.position.y >= (this.geneRocketsController.getHeight() - 145)) {
            if (!this.crashed) {
                this.geneRocketsController.addIdleRocket(this);

                // Update the global highest fitness score
                if (this.geneRocketsController.getHighestFitness() < this.fitness) {
                    this.geneRocketsController.setHighestFitness(this.fitness);
                }

                if (this.geneRocketsController.getHighestFlightTime() < (System.currentTimeMillis() - this.flightStart) / 1000) {
                    this.geneRocketsController.setHighestFlightTime((System.currentTimeMillis() - this.flightStart) / 1000);
                }
            }
            this.crashed = true;
        }

        if (!this.crashed && !this.completed) {
            this.acceleration.y += this.gravity;
            this.velocity.x += this.acceleration.x;
            this.velocity.y += this.acceleration.y;
            this.position.x += this.velocity.x;
            this.position.y += this.velocity.y;

            this.acceleration.x = 0;
            this.acceleration.y = 0;

            this.flightTime = System.currentTimeMillis();

            super.setTranslateX(this.position.x);
            super.setTranslateY(this.position.y);

            this.applyAcceleration();
            this.updateFitness();
        }
    }

    private void applyAcceleration() {
        if (this.lastAcceleration == 0) {
            this.acceleration.x = this.dna.getGenes().get(this.geneCount).getVector().x;
            this.acceleration.y = -1.5;
            this.geneCount++;
            this.lastAcceleration = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - this.lastAcceleration >= 150) {
            this.acceleration.x = this.dna.getGenes().get(this.geneCount).getVector().x;
            this.acceleration.y = this.dna.getGenes().get(this.geneCount).getVector().y;
            this.geneCount++;
            this.lastAcceleration = System.currentTimeMillis();
        }

        generateNewGene();
    }

    private void generateNewGene() {
        this.getDna().getGenes().add(new Gene(new Vec2d(
                random.nextDouble() * 1 - 0.5,
                random.nextDouble() * -1
        )));
    }

    private void updateFitness() {
        if (this.flightTime - this.setTime >= 250) {
            this.setTime = System.currentTimeMillis();
            this.fitness *= 1.125;
        }
    }

    public void applyMutations() {
        for (int i = 0; i < this.getDna().getGenes().size(); i++) {
            if (Math.random() < 0.025) {
                this.getDna().getGenes().set(
                        i,
                        new Gene(new Vec2d(
                                random.nextDouble() * 1 - 0.5,
                                random.nextDouble() * -1)
                        )
                );
            }
        }
    }

    public double getFitness() {
        return fitness;
    }

    public DNA getDna() {
        return dna;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }
}