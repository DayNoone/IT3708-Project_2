import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class GUI {
    GridPane gridControls;
    Charts charts;
    int controlsRowNumber = 1;

    ArrayList<TextField> inputFields = new ArrayList<>();
    Console console;

    public GUI(Main main, Stage primaryStage){
        //--------------------------------- GUI OBJECTS ------------------------------
        primaryStage.setTitle("Evolutionary Algorithm");

        Text scenetitle = new Text("Settings");
        scenetitle.setId("sceneTitle");

        TextArea outputArea = new TextArea();
        outputArea.setStyle("-fx-focus-color: transparent;");
        outputArea.setMinHeight(300);
        outputArea.setMinWidth(1000);
        outputArea.setEditable(false);

        // Console for outputting information without interrupting the evolutionary cycle
        console = new Console(outputArea);
        //--------------------------------- CONTROLS ---------------------------------
        Button oneMaxButton = new Button();
        GridPane.setHalignment(oneMaxButton, HPos.CENTER);
        oneMaxButton.setText("START ONEMAX EA");
        oneMaxButton.setOnAction(event -> main.startEA(Constants.algorithms.ONE_MAX));

        Button lolzButton = new Button();
        GridPane.setHalignment(lolzButton, HPos.CENTER);
        lolzButton.setText("START LOLZ EA");
        lolzButton.setOnAction(event -> main.startEA(Constants.algorithms.LOLZ));

        Button supriseButton = new Button();
        GridPane.setHalignment(supriseButton, HPos.CENTER);
        supriseButton.setText("START SURPRISE EA");
        supriseButton.setOnAction(event -> main.startEA(Constants.algorithms.SURPRISE));

        //--------------------------------- GUI LAYOUT -------------------------------
        gridControls = new GridPane();
        gridControls.setMinHeight(Constants.SCENE_HEIGHT-300);
        gridControls.setAlignment(Pos.TOP_CENTER);
        gridControls.setHgap(10);
        gridControls.setVgap(10);
        gridControls.setPadding(new Insets(25, 25, 25, 25));

        gridControls.add(scenetitle, 0, 0, 2, 1);

        Node generationSize = inputField("Generation size", Constants.GENERATION_SIZE);
        Node adultSize = inputField("Adult size", Constants.ADULTS_SIZE);
        Node mutationRateField = inputField("Mutation rate", Constants.MUTATION_RATE);
        Node crossoverRate = inputField("Crossover rate", Constants.CROSSOVER_RATE);


        addControl(generationSize);
        addControl(adultSize);
        addControl(mutationRateField);
        addControl(crossoverRate);

        addAdultSelectionRadio();
        addParentSelectionRadio();

        Node tournamentGroupSize = inputField("Tournament group size", Constants.TOURNAMENT_GROUP_SIZE);
        Node tournamentProbability = inputField("Tournament probability", Constants.TOURNAMENT_PROBABILITY);
        addControl(tournamentGroupSize);
        addControl(tournamentProbability);

        Node oneMaxGenotypeSize = inputField("OneMax bitsize", Constants.BITSIZE);
        addControl(oneMaxGenotypeSize);

        addControl(oneMaxButton);
        addControl(lolzButton);
        addControl(supriseButton);

        charts = new Charts();
        BorderPane root = new BorderPane();
        root.setLeft(gridControls);
        root.setRight(charts.getGrid());
        root.setBottom(outputArea);
        Scene scene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void updateConstants() {
        for (int i = 0; i < inputFields.size(); i++) {
            Double value = Double.parseDouble(inputFields.get(i).getText());
            switch (i){
                case 0:
                    Constants.GENERATION_SIZE = value.intValue();
                    Constants.PARENTS_SIZE = Constants.GENERATION_SIZE - Constants.ELITISM_SIZE;
                    break;
                case 1:
                    Constants.ADULTS_SIZE = value.intValue();
                    break;
                case 2:
                    Constants.MUTATION_RATE = value;
                    break;
                case 3:
                    Constants.CROSSOVER_RATE = value;
                    break;
                case 4:
                    Constants.TOURNAMENT_GROUP_SIZE = value.intValue();
                    break;
                case 5:
                    Constants.TOURNAMENT_PROBABILITY = value;
                    break;
                case 6:
                    Constants.BITSIZE = value.intValue();
                    break;
            }
        }
    }

    // ------------------- I/O ------------------------
    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        public void writeStringln(String text) {
            output.appendText(text + "\n");
        }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }

    public void addControl(Node node){
        gridControls.add(node, 0, controlsRowNumber);
        controlsRowNumber++;
    }

    private Node inputField(String name, double constant) {
        Label label1 = new Label(name);
        label1.setMinWidth(120);
        label1.setMaxWidth(120);
        TextField textField = new TextField(Double.toString(constant));
        inputFields.add(textField);
        textField.setMaxWidth(60);
        textField.setMinWidth(60);
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField);
        hb.setSpacing(10);
        return hb;
    }

    private void addAdultSelectionRadio() {
        Label adultSelectionLabel = new Label("Adult selection");
        adultSelectionLabel.setId("radioGroup");
        final ToggleGroup adultSelectionRadio = new ToggleGroup();
        RadioButton fullGeneration = new RadioButton("Full generation");
        fullGeneration.setToggleGroup(adultSelectionRadio);
        RadioButton overproduction = new RadioButton("Overproduction");
        overproduction.setToggleGroup(adultSelectionRadio);
        RadioButton generationalMixing = new RadioButton("Generational mixing");
        generationalMixing.setSelected(true);
        generationalMixing.setToggleGroup(adultSelectionRadio);
        adultSelectionRadio.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (adultSelectionRadio.getSelectedToggle() != null) {
                if (new_toggle == fullGeneration) {
                    Constants.ADULT_SELECTION = Constants.adultSelection.FULL_GENERATION;
                } else if (new_toggle == overproduction) {
                    Constants.ADULT_SELECTION = Constants.adultSelection.OVER_PRODUCTION;
                } else if (new_toggle == generationalMixing) {
                    Constants.ADULT_SELECTION = Constants.adultSelection.GENERATIONAL_MIXING;
                }
            }
        });
        addControl(adultSelectionLabel);
        addControl(fullGeneration);
        addControl(overproduction);
        addControl(generationalMixing);
    }

    private void addParentSelectionRadio() {
        Label parentSelectionLabel = new Label("Parent selection");
        parentSelectionLabel.setId("radioGroup");
        final ToggleGroup parentSelectionRadio = new ToggleGroup();
        RadioButton fitnessProportionate = new RadioButton("Fitness proportionate");
        fitnessProportionate.setToggleGroup(parentSelectionRadio);
        RadioButton sigmaScaling= new RadioButton("Sigma scaling");
        sigmaScaling.setToggleGroup(parentSelectionRadio);
        sigmaScaling.setSelected(true);
        RadioButton tournamentSelection = new RadioButton("Tournament selection");
        tournamentSelection.setToggleGroup(parentSelectionRadio);
        RadioButton uniformSelection = new RadioButton("Uniform selection");
        uniformSelection.setToggleGroup(parentSelectionRadio);
        parentSelectionRadio.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (parentSelectionRadio.getSelectedToggle() != null) {
                if (new_toggle == fitnessProportionate) {
                    Constants.PARENT_SELECTION = Constants.parentSelection.FITNESS_PROPORTIONATE;
                } else if (new_toggle == sigmaScaling) {
                    Constants.PARENT_SELECTION = Constants.parentSelection.SIGMA_SCALING;
                } else if (new_toggle == tournamentSelection) {
                    Constants.PARENT_SELECTION = Constants.parentSelection.TOURNAMENT_SELECTION;
                } else if (new_toggle == uniformSelection) {
                    Constants.PARENT_SELECTION = Constants.parentSelection.UNIFORM_SELECTION;
                }
            }
        });
        addControl(parentSelectionLabel);
        addControl(fitnessProportionate);
        addControl(sigmaScaling);
        addControl(tournamentSelection);
        addControl(uniformSelection);
    }
}
