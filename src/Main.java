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

    Console console;
    XYChart.Series<Number, Number> series;
    XYChart.Series<Number, Number> bestFitnessSeries;
    XYChart.Series<Number, Number> averageFitnessSeries;
    XYChart.Series<Number, Number> standardDeviationSeries;
    final NumberAxis xAxis = new NumberAxis(0, Constants.GENERATION_SIZE-1, 2);
    final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
    final LineChart<Number,Number> generationFitnessPlot = new LineChart<Number,Number>(xAxis,yAxis);
    final LineChart<Number,Number> bestFitnessPlot = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());
    final LineChart<Number,Number> averageFitnessPlot = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());
    final LineChart<Number,Number> standardDeviationPlot = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());

    public void start(Stage primaryStage) throws IOException {

        //--------------------------------- INIT EA ----------------------------------
        ArrayList<Hypothesis> initialGeneration = initializeGeneration();
        evolutionaryAlgorithmCycle = new EA(initialGeneration);


        //--------------------------------- GUI OBJECTS ------------------------------
        primaryStage.setTitle("Evolutionary Algorithm");

        Text scenetitle = new Text("Evolutionary Algorithm");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        String outputText = "This is to output text \nTo see the solutions";
        TextArea outputArea = new TextArea();
        outputArea.setMinHeight(720);
        outputArea.setMinWidth(600);
        outputArea.setEditable(false);

        // Console for outputting information without interrupting the evolutionary cycle
        console = new Console(outputArea);
        //--------------------------------- CONTROLS ---------------------------------
        Button oneMaxButton = new Button();
        GridPane.setHalignment(oneMaxButton, HPos.CENTER);
        oneMaxButton.setText("Start OneMax problem");
        oneMaxButton.setOnAction(event -> evolutionaryAlgorithmCycle.restartCycle(initializeGeneration()));

        Button lolzButton = new Button();
        GridPane.setHalignment(lolzButton, HPos.CENTER);
        lolzButton.setText("Start Lolz problem");
        lolzButton.setOnAction(event -> evolutionaryAlgorithmCycle.setRunning(true));

        Button supriseButton = new Button();
        GridPane.setHalignment(supriseButton, HPos.CENTER);
        supriseButton.setText("Start Suprise problem");
        supriseButton.setOnAction(event -> evolutionaryAlgorithmCycle.setRunning(true));

        Label finessThresholdLabel = new Label("Fitness Threshold");
        Label mutationRateLabel = new Label("Mutation Rate");
        Label generationSizeLabel = new Label("Generation Size");
        Label crossoverRateLabel = new Label("Crossover Rate");

        Slider fitnessThresholdSlider = new Slider();
        Slider mutationRateSlider = new Slider();
        Slider generationSizeSlider = new Slider();
        Slider crossoverRateSlider = new Slider();

        //--------------------------------- GUI LAYOUT -------------------------------
        gridControls = new GridPane();
        gridControls.setMinHeight(Constants.SCENE_HEIGHT);
        gridControls.setAlignment(Pos.TOP_CENTER);
        gridControls.setHgap(10);
        gridControls.setVgap(10);
        gridControls.setPadding(new Insets(25, 25, 25, 25));

        gridControls.add(scenetitle, 0, 0, 2, 1);

        addControl(oneMaxButton);
        addControl(lolzButton);
        addControl(supriseButton);
        addControl(finessThresholdLabel);
        addControl(fitnessThresholdSlider);
        addControl(mutationRateLabel);
        addControl(mutationRateSlider);
        addControl(generationSizeLabel);
        addControl(generationSizeSlider);
        addControl(crossoverRateLabel);
        addControl(crossoverRateSlider);

        gridControls.add(outputArea, 1, 1, 1, controlsRowNumber);

        //--------------------------------- PLOT ------------------------------------
        GridPane gridPlots = new GridPane();
        generationFitnessPlot.setTitle("Population fitness");
        generationFitnessPlot.setLegendVisible(false);
        series = new XYChart.Series<>();
        series.setName("My portfolio");
        gridPlots.add(generationFitnessPlot, 0, 0);

        bestFitnessSeries = new XYChart.Series<>();
        bestFitnessPlot.setTitle("Best fitness");
        bestFitnessPlot.setAnimated(false);
        bestFitnessPlot.setLegendVisible(false);
        bestFitnessPlot.getData().add(bestFitnessSeries);
        gridPlots.add(bestFitnessPlot, 1, 0);

        averageFitnessSeries = new XYChart.Series<>();
        averageFitnessPlot.setTitle("Average fitness");
        averageFitnessPlot.setAnimated(false);
        averageFitnessPlot.setLegendVisible(false);
        averageFitnessPlot.getData().add(averageFitnessSeries);
        gridPlots.add(averageFitnessPlot, 0, 1);

        standardDeviationSeries = new XYChart.Series<>();
        standardDeviationPlot.setTitle("Standard deviation");
        standardDeviationPlot.setAnimated(false);
        standardDeviationPlot.setLegendVisible(false);
        standardDeviationPlot.getData().add(standardDeviationSeries);
        gridPlots.add(standardDeviationPlot, 1, 1);

        BorderPane root = new BorderPane();
        root.setLeft(gridControls);
        root.setRight(gridPlots);
        Scene scene = new Scene(root, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        startEA();
    }

    private void startEA() {

        eaLoop = new AnimationTimer() {
            public void handle(long now) {
                if(evolutionaryAlgorithmCycle.isRunning()){
                    evolutionaryAlgorithmCycle.iteration();
                    updateLinechart();
                    if (evolutionaryAlgorithmCycle.getSolution()) {
                        console.writeStringln("--------------------------------- Solution found ---------------------------------");
                        evolutionaryAlgorithmCycle.setRunning(false);
                    }
                    double averageFitness = evolutionaryAlgorithmCycle.getAverageFitness(evolutionaryAlgorithmCycle.population);
                    Hypothesis fittest = evolutionaryAlgorithmCycle.getFittest(evolutionaryAlgorithmCycle.population);
                    console.writeStringln("Gen: " + evolutionaryAlgorithmCycle.getGenerationNumber() +
                            "\t Best: " + String.format("%.2f", fittest.getFitness()) +
                            "\t Avg fitness: " + String.format("%.2f", averageFitness) +
                            "\t SD: " + String.format("%.3f", evolutionaryAlgorithmCycle.getStandardDeviation(evolutionaryAlgorithmCycle.population, averageFitness)) +
                            "\t Pheno: " + Arrays.toString(fittest.getPhenotype()));

                }
            }
        };

        eaLoop.start();
    }

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


    private ArrayList<Hypothesis> initializeGeneration() {
        ArrayList<Hypothesis> initialGeneration = new ArrayList<Hypothesis>();
        for(int i = 0; i < Constants.GENERATION_SIZE; i++){
            initialGeneration.add(new OneMaxHypothesis());
        }
        return initialGeneration;
    }


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



    public static void main(String[] args) {
        launch(args);
    }


}