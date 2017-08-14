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
    // TODO: DNA array gets so large the program will eventually freeze.
    private DNA dna;
    private geneRockets geneRocketsController;
    private Random random = new Random();

    private int geneCount = 0;
    private double lastAcceleration = 0;
    private double flightTime = System.currentTimeMillis();
    private double flightStart = System.currentTimeMillis();
    private double setTime = System.currentTimeMillis();
    private double gravity = 0.045;
    // TODO: Please fix the terrible current fitness system.
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
        if (this.crashed()) {
            this.geneRocketsController.addIdleRocket(this);

            // Update the global highest fitness score
            if (this.geneRocketsController.getHighestFitness() < this.fitness) {
                this.geneRocketsController.setHighestFitness(this.fitness);
            }

            if (this.geneRocketsController.getHighestFlightTime() < (System.currentTimeMillis() - this.flightStart) / 1000) {
                this.geneRocketsController.setHighestFlightTime((System.currentTimeMillis() - this.flightStart) / 1000);
            }
        } else if (!this.crashed() && !this.completed) {
            this.updatePositionValues();

            super.setTranslateX(this.position.x);
            super.setTranslateY(this.position.y);

            this.applyAcceleration(200);
            this.updateFitness();
        }
    }

    private boolean crashed() {
        return this.position.x <= 0 ||
                this.position.y <= 0 ||
                this.position.x >= this.geneRocketsController.getWidth() - 30 ||
                this.position.y >= this.geneRocketsController.getHeight() - 145;
    }

    private void updatePositionValues() {
        this.acceleration.y += this.gravity;
        this.velocity.x += this.acceleration.x;
        this.velocity.y += this.acceleration.y;
        this.position.x += this.velocity.x;
        this.position.y += this.velocity.y;

        this.acceleration.x = 0;
        this.acceleration.y = 0;

        this.flightTime = System.currentTimeMillis();
    }

    private double warpDrive() {
        // Probably one of the most badass functions around in this program.
        // Apply accelerations until the rocket crashes into something.
        // WarpDrive is used to fast forward generations.

        while (!this.crashed() && !this.completed) {
            this.applyAcceleration(0);
            this.updatePositionValues();
            geneCount++;
            this.updateFitness();

            // After new vector positioning is finished, let's check if it is crashed or finished before looping again.
            if (this.crashed() || this.completed) {
                return this.fitness;
            }
        }

        // Shouldn't reach here, but if it does you have broken some law in logic. Please inform with someone important.
        return -1;
    }

    private void applyAcceleration(double delay) {
        // TODO: Change this function so time is a variable and it can be used in warpDrive function.
        if (this.lastAcceleration == 0) {
            this.acceleration.x = this.dna.getGenes().get(this.geneCount).x;
            this.acceleration.y = -1.5;
            this.lastAcceleration = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - this.lastAcceleration >= delay) {
            this.acceleration.x = this.dna.getGenes().get(this.geneCount).x;
            this.acceleration.y = this.dna.getGenes().get(this.geneCount).y;
            this.lastAcceleration = System.currentTimeMillis();
        }

        this.geneCount++;
        this.generateNewGene();
    }

    private void generateNewGene() {
        // TODO: This is breaking the program, the vector array gets so large after a while that it floods the RAM. Please fix.
        this.getDna().getGenes().add(new Vec2d(
                random.nextDouble() * 1 - 0.5,
                random.nextDouble() * -1
        ));
    }

    private void updateFitness() {
        // TODO: So this is obviously not going to work, we cannot skip generations if the fitness is based on time.
        // Solution, maybe base fitness on distance to crashable objects,
        // but then we need to give each vector a score to keep track of what vectors are doing good and which aren't

        if (this.flightTime - this.setTime >= 250) {
            this.setTime = System.currentTimeMillis();
            this.fitness *= 1.125;
        }
    }

    public void applyMutations() {
        for (int i = 0; i < this.getDna().getGenes().size(); i++) {
            if (Math.random() < 0.025) {
                this.getDna().getGenes().set(i, new Vec2d(random.nextDouble() * 1 - 0.5, random.nextDouble() * -1));
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