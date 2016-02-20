import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application{

    EA evolutionaryAlgorithmCycle;

    AnimationTimer eaLoop;

    GridPane gridControls;
    GridPane gridPlots;
    int controlsRowNumber = 1;

    ArrayList<TextField> inputFields = new ArrayList<>();
    Console console;
    XYChart.Series<Number, Number> series;
    XYChart.Series<Number, Number> bestFitnessSeries;
    XYChart.Series<Number, Number> averageFitnessSeries;
    XYChart.Series<Number, Number> standardDeviationSeries;
    final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
    final NumberAxis xAxis = new NumberAxis();
    final LineChart<Number,Number> generationFitnessPlot = new LineChart<Number,Number>(xAxis, yAxis);
    final LineChart<Number,Number> bestFitnessPlot = new LineChart<Number,Number>(new NumberAxis(), yAxis);
    final LineChart<Number,Number> averageFitnessPlot = new LineChart<Number,Number>(new NumberAxis(), yAxis);
    final LineChart<Number,Number> standardDeviationPlot = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());

    public void start(Stage primaryStage) throws IOException {
        //--------------------------------- GUI OBJECTS ------------------------------
        primaryStage.setTitle("Evolutionary Algorithm");

        Text scenetitle = new Text("Settings");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.LIGHT, 20));

        TextArea outputArea = new TextArea();
        outputArea.setStyle("-fx-focus-color: transparent;");
        outputArea.setMinHeight(720);
        outputArea.setMinWidth(540);
        outputArea.setEditable(false);

        // Console for outputting information without interrupting the evolutionary cycle
        console = new Console(outputArea);
        //--------------------------------- CONTROLS ---------------------------------
        Button oneMaxButton = new Button();
        GridPane.setHalignment(oneMaxButton, HPos.CENTER);
        oneMaxButton.setText("Start OneMax EA");
        oneMaxButton.setOnAction(event -> startEA(Constants.algorithms.ONE_MAX));

        Button lolzButton = new Button();
        GridPane.setHalignment(lolzButton, HPos.CENTER);
        lolzButton.setText("Start Lolz EA");
        lolzButton.setOnAction(event -> startEA(Constants.algorithms.ONE_MAX));

        Button supriseButton = new Button();
        GridPane.setHalignment(supriseButton, HPos.CENTER);
        supriseButton.setText("Start Surprise EA");
        supriseButton.setOnAction(event -> startEA(Constants.algorithms.ONE_MAX));

        //--------------------------------- GUI LAYOUT -------------------------------
        gridControls = new GridPane();
        gridControls.setMinHeight(Constants.SCENE_HEIGHT);
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

        Node oneMaxGenotypeSize = inputField("OneMax bitsize", Constants.GENOTYPE_ONEMAX_SIZE);
        addControl(oneMaxGenotypeSize);

        addControl(oneMaxButton);
        addControl(lolzButton);
        addControl(supriseButton);

        gridControls.add(outputArea, 1, 1, 1, controlsRowNumber);

        //--------------------------------- PLOT ------------------------------------
        gridPlots = new GridPane();
        gridPlots.setHgap(10);
        gridPlots.setVgap(10);
        gridPlots.setPadding(new Insets(25, 25, 25, 25));

        generationFitnessPlot.setTitle("Population fitness");
        generationFitnessPlot.setLegendVisible(false);
        series = new XYChart.Series<>();
        series.setName("My portfolio");
        gridPlots.add(generationFitnessPlot, 0, 0);

        bestFitnessSeries = new XYChart.Series<>();
        bestFitnessPlot.setTitle("Best fitness");
        bestFitnessPlot.setAnimated(false);
        bestFitnessPlot.setLegendVisible(false);
        bestFitnessPlot.setCreateSymbols(false);
        bestFitnessPlot.setStyle("");
        bestFitnessPlot.getData().add(bestFitnessSeries);
        gridPlots.add(bestFitnessPlot, 1, 0);

        averageFitnessSeries = new XYChart.Series<>();
        averageFitnessPlot.setTitle("Average fitness");
        averageFitnessPlot.setAnimated(false);
        averageFitnessPlot.setLegendVisible(false);
        averageFitnessPlot.setCreateSymbols(false);
        averageFitnessPlot.getData().add(averageFitnessSeries);
        gridPlots.add(averageFitnessPlot, 0, 1);

        standardDeviationSeries = new XYChart.Series<>();
        standardDeviationPlot.setTitle("Standard deviation");
        standardDeviationPlot.setAnimated(false);
        standardDeviationPlot.setLegendVisible(false);
        standardDeviationPlot.setCreateSymbols(false);
        standardDeviationPlot.getData().add(standardDeviationSeries);
        gridPlots.add(standardDeviationPlot, 1, 1);

        BorderPane root = new BorderPane();
        root.setLeft(gridControls);
        root.setRight(gridPlots);
        Scene scene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
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


    private void startEA(Constants.algorithms oneMax) {
        // To keep multiple loops from running
        if (evolutionaryAlgorithmCycle != null && evolutionaryAlgorithmCycle.isRunning()) {
            eaLoop.stop();
        }
        evolutionaryAlgorithmCycle = null;
        updateConstants();
        ArrayList<Hypothesis> initialGeneration = initializeGeneration(oneMax);
        evolutionaryAlgorithmCycle = new EA(initialGeneration);
        clearPlots();

        eaLoop = new AnimationTimer() {
            public void handle(long now) {
                if(evolutionaryAlgorithmCycle.isRunning()){
                    evolutionaryAlgorithmCycle.iteration();
                    updateLinechart();
                    double averageFitness = evolutionaryAlgorithmCycle.getAverageFitness(evolutionaryAlgorithmCycle.population);
                    Hypothesis fittest = evolutionaryAlgorithmCycle.getFittest(evolutionaryAlgorithmCycle.population);
                    console.writeStringln("Gen: " + evolutionaryAlgorithmCycle.getGenerationNumber() +
                            "\t Best: " + String.format("%.2f", fittest.getFitness()) +
                            "\t Pop: " + evolutionaryAlgorithmCycle.population.size() + " - " + Constants.ADULTS_SIZE + " - " + Constants.PARENTS_SIZE +
                            "\t Avg fitness: " + String.format("%.2f", averageFitness) +
                            "\t SD: " + String.format("%.3f", evolutionaryAlgorithmCycle.getStandardDeviation(evolutionaryAlgorithmCycle.population, averageFitness)) +
                            "\t Pheno: " + Arrays.toString(fittest.getPhenotype()));
                    if (evolutionaryAlgorithmCycle.getSolution()) {
                        console.writeStringln("--------------------------------- Solution found ---------------------------------");
                        eaLoop.stop();
                    }
                }
            }
        };

        eaLoop.start();
    }

    private void updateConstants() {
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
                    Constants.GENOTYPE_ONEMAX_SIZE = value.intValue();
                    break;
            }
        }
    }

    private ArrayList<Hypothesis> initializeGeneration(Constants.algorithms algorithm) {
        ArrayList<Hypothesis> initialGeneration = new ArrayList<Hypothesis>();
        for(int i = 0; i < Constants.GENERATION_SIZE; i++){
            switch (algorithm) {
                case ONE_MAX:
                    initialGeneration.add(new OneMaxHypothesis());
                    break;
                case LOLZ:
                    //initialGeneration.add(new LolzHypothesis());
                    break;
                case SURPRISE:
                    //initialGeneration.add(new SurpriseHypothesis());
                    break;
            }
        }
        return initialGeneration;
    }

    // ---------------------- PLOTS --------------------
    private void updateLinechart() {
        ArrayList<Hypothesis> population = evolutionaryAlgorithmCycle.population;
        generationFitnessPlot.getData().retainAll();
        generationFitnessPlot.setAnimated(false);
        series = new XYChart.Series<>();
        for (int i = 0; i < population.size(); i++){
            series.getData().add(new XYChart.Data<>(i, (population.get(i)).getFitness()));
        }
        generationFitnessPlot.getData().add(series);


        double averageFitness = evolutionaryAlgorithmCycle.getAverageFitness(population);
        bestFitnessSeries.getData().add(new XYChart.Data<>(evolutionaryAlgorithmCycle.getGenerationNumber(), (evolutionaryAlgorithmCycle.getFittest(population)).getFitness()));
        averageFitnessSeries.getData().add(new XYChart.Data<>(evolutionaryAlgorithmCycle.getGenerationNumber(), averageFitness));
        standardDeviationSeries.getData().add(new XYChart.Data<>(evolutionaryAlgorithmCycle.getGenerationNumber(), (evolutionaryAlgorithmCycle.getStandardDeviation(population, averageFitness))));
    }
    private void clearPlots() {
        bestFitnessSeries.getData().retainAll();
        averageFitnessSeries.getData().retainAll();
        standardDeviationSeries.getData().retainAll();
    }

    // ------------------- I/O ------------------------
    public static class Console extends OutputStream {

        private TextArea output;

        public Console(TextArea ta) {
            this.output = ta;
        }

        public void writeStringln(String text) {
            for (char letter : text.toCharArray()) {
                output.appendText(String.valueOf((char) letter));
            }
            output.appendText("\n");
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
                console.writeStringln("ADULT: " + Constants.ADULT_SELECTION);
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
        RadioButton tournamentSelection = new RadioButton("Tournament selection");
        tournamentSelection.setToggleGroup(parentSelectionRadio);
        tournamentSelection.setSelected(true);
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
                console.writeStringln("PARENT: " + Constants.PARENT_SELECTION);
            }
        });
        addControl(parentSelectionLabel);
        addControl(fitnessProportionate);
        addControl(sigmaScaling);
        addControl(tournamentSelection);
        addControl(uniformSelection);
    }



    public static void main(String[] args) {
        launch(args);
    }


}