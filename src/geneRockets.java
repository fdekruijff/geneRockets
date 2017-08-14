import com.sun.javafx.geom.Vec2d;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class geneRockets extends Application {

    private int width = 1000;
    private int height = 666;
    private int rocketPoolSize = 50;
    private int generationCount = 0;
    private double highestFitness = 0;
    private double highestFlightTime = 0;

    private Stage stage;
    private ArrayList<Rocket> population;
    private ArrayList<Rocket> idleRockets;
    private windowController windowController;
    private Random random;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.population = new ArrayList<>();
        this.idleRockets = new ArrayList<>();
        this.random = new Random();

        this.stage = primaryStage;
        this.stage.setTitle("geneRockets");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
        Parent root = loader.load();
        this.windowController = loader.getController();
        this.windowController.initialize(this);

        this.windowController.updateRocketPoolSizeLabel(Integer.toString(this.rocketPoolSize));
        this.windowController.adjustRocketPoolSizeSliderValue(this.rocketPoolSize);

        for (int x = 0; x < this.rocketPoolSize; x++) {
            Rocket rocket = new Rocket(this);
            this.population.add(rocket);
            this.windowController.addRocketToWindow(rocket);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        }.start();

        stage.setScene(new Scene(root, this.getWidth(), this.getHeight()));
        stage.show();
    }

    /**
     * update tick function
     */
    private void update() {
        for (Rocket rocket : this.population) {
            rocket.update();
        }

        if (this.idleRockets.size() == this.population.size()) {
            this.generateNewRockets();
        }
    }

    /**
     * generateNewRockets generates the new Rocket population for the next generation
     */
    private void generateNewRockets() {
        ArrayList<Rocket> newRockets = new ArrayList<>();
        this.generationCount++;

        for (Rocket rocket : this.population) {
            rocket.setFitness(Math.floor(rocket.getFitness()));
            for (int i = 0; i < rocket.getFitness(); i++) {
                newRockets.add(rocket);
            }
        }

        this.population.clear();

        for (int i = 0; i < this.rocketPoolSize; i++) {
            Rocket r1 = newRockets.get(random.nextInt(newRockets.size()));
            Rocket r2 = newRockets.get(random.nextInt(newRockets.size()));

            this.population.add(new Rocket(this, createChildDNA(r1, r2)));
        }

        // Clear window
        this.windowController.removeRocketFromWindow();
        this.windowController.updateHighestFitnessLabel(Integer.toString((int) Math.floor(this.highestFitness)));
        this.windowController.updateGenerationLabel(Integer.toString(this.generationCount));
        this.windowController.updateFlightTimeLabel(this.formatFlightTime());

        // Add new Rockets to window
        for (Rocket rocket : this.population) {
            rocket.applyMutations();
            this.windowController.addRocketToWindow(rocket);
        }

        // Clear the rocket arrays
        this.idleRockets.clear();
        newRockets.clear();
    }

    /**
     * createChild makes a new Rocket from two parents
     * @param rocket1 Rocket object 1
     * @param rocket2 Rocket object 2
     * @return DNA object
     */
    private DNA createChildDNA(Rocket rocket1, Rocket rocket2) {
        ArrayList<Vec2d> newGenes = new ArrayList<>();

        for (int i = 0; i < rocket1.getDna().getGenes().size(); i++) {
            if (i < rocket1.getDna().getGenes().size() && i < rocket2.getDna().getGenes().size()) {
                double newX = (rocket1.getDna().getGenes().get(i).x + rocket2.getDna().getGenes().get(i).x) / 2;
                double newY = (rocket1.getDna().getGenes().get(i).y + rocket2.getDna().getGenes().get(i).y) / 2;

                newGenes.add(new Vec2d(newX, newY));
            }
        }

        return new DNA(newGenes);
    }

    private String formatFlightTime() {
        int hours = (int)Math.floor(this.highestFlightTime / 3600);
        int minutes = (int)Math.floor((this.highestFlightTime % 3600) / 60);
        int seconds = (int)Math.floor(this.highestFlightTime % 60);

        if (hours >= 1) {
            return "HOUR!";
        } else if (minutes >= 1) {
            return minutes +  ":" + seconds + " min";
        } else if (seconds >= 1) {
            return seconds + " sec";
        }
        return "0 sec";
    }

    public void restart() {
        this.highestFlightTime = 0;
        this.highestFitness = 0;
        this.population.clear();
        this.idleRockets.clear();
        this.generationCount = 1;
        this.windowController.updateHighestFitnessLabel(Integer.toString((int) Math.floor(this.highestFitness)));
        this.windowController.updateGenerationLabel(Integer.toString(this.generationCount));
        this.windowController.updateFlightTimeLabel(this.formatFlightTime());
        this.windowController.removeRocketFromWindow();
        for (int x = 0; x < this.rocketPoolSize; x++) {
            Rocket rocket = new Rocket(this);
            this.population.add(rocket);
            this.windowController.addRocketToWindow(rocket);
        }
    }

    public void skipGeneration() {

    }

    void addIdleRocket(Rocket rocket) {
        this.idleRockets.add(rocket);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public double getHighestFitness() {
        return highestFitness;
    }

    public void setHighestFitness(double highestFitness) {
        this.highestFitness = highestFitness;
    }

    public double getHighestFlightTime() {
        return highestFlightTime;
    }

    public void setHighestFlightTime(double highestFlightTime) {
        this.highestFlightTime = highestFlightTime;
    }

    public void setRocketPoolSize(int rocketPoolSize) {
        this.rocketPoolSize = rocketPoolSize;
    }
}
