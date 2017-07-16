import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

public class Controller {
    @FXML
    private Pane rocketArea;
    @FXML
    private Label highestFitnessLabel;
    @FXML
    private Label generationLabel;
    @FXML
    private Label flightTimeLabel;
    @FXML
    public Slider rocketPoolSizeSlider;
    @FXML
    private Label rocketPoolSizeLabel;

    geneRockets controller;

    void initialize(geneRockets controller) {
        this.controller = controller;
    }

    public void addRocketToWindow(Rocket rocket) {
        this.rocketArea.getChildren().add(rocket);
    }

    public void removeRocketFromWindow() {
        this.rocketArea.getChildren().clear();
    }

    public void updateHighestFitnessLabel(String value) {
        this.highestFitnessLabel.setText(value);
    }

    public void updateGenerationLabel(String value) {
        this.generationLabel.setText(value);
    }

    public void updateFlightTimeLabel(String value) {
        this.flightTimeLabel.setText(value);
    }

    public void restartButtonOnAction() {
                this.controller.restart();
    }

    public void updateRocketPoolSizeLabel(String value) {
        this.rocketPoolSizeLabel.setText(value);
    }

    public void rocketPoolSizeSliderOnAction() {
        int value = (int) Math.floor(this.rocketPoolSizeSlider.getValue());
        this.controller.setRocketPoolSize(value);
        this.updateRocketPoolSizeLabel(Integer.toString(value));
    }

}
