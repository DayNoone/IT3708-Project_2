import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class Main extends Application{

    EA evolutionaryAlgorithmCycle;

    AnimationTimer eaLoop;

    GridPane grid;
    int controlsRowNumber = 1;

    Console console;
    XYChart.Series series;
    final NumberAxis xAxis = new NumberAxis(0, Constants.GENERATION_SIZE, 2);
    final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
    final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);

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
        outputArea.setMinHeight(380);
        outputArea.setEditable(false);

        // Console for outputting information without interrupting the evolutionary cycle
        console = new Console(outputArea);

        //--------------------------------- CONTROLS ---------------------------------
        Button restartButton = new Button();
        restartButton.setText("Restart cycle");
        restartButton.setOnAction(event -> evolutionaryAlgorithmCycle.restartCycle(initializeGeneration()));

        Button startButton = new Button();
        startButton.setText("Start cycle");
        startButton.setOnAction(event -> evolutionaryAlgorithmCycle.setRunning(true));

        Label finessThresholdLabel = new Label("Fitness Threshold");
        Label mutationRateLabel = new Label("Mutation Rate");
        Label generationSizeLabel = new Label("Generation Size");
        Label crossoverRateLabel = new Label("Crossover Rate");

        Slider fitnessThresholdSlider = new Slider();
        Slider mutationRateSlider = new Slider();
        Slider generationSizeSlider = new Slider();
        Slider crossoverRateSlider = new Slider();

        //--------------------------------- GUI LAYOUT -------------------------------
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(scenetitle, 0, 0, 2, 1);

        addControl(restartButton);
        addControl(startButton);
        addControl(finessThresholdLabel);
        addControl(fitnessThresholdSlider);
        addControl(mutationRateLabel);
        addControl(mutationRateSlider);
        addControl(generationSizeLabel);
        addControl(generationSizeSlider);
        addControl(crossoverRateLabel);
        addControl(crossoverRateSlider);

        grid.add(outputArea, 1, 1, 1, controlsRowNumber);

        //--------------------------------- PLOT ------------------------------------
        lineChart.setTitle("Population fitness");
        lineChart.setLegendVisible(false);
        series = new XYChart.Series();
        series.setName("My portfolio");

        grid.add(lineChart, 2, 1, 1, controlsRowNumber);


        Scene scene = new Scene(grid, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        startEA();
    }

    private void startEA() {

        eaLoop = new AnimationTimer() {
            public void handle(long now) {
                if(evolutionaryAlgorithmCycle.isRunning()){
                    evolutionaryAlgorithmCycle.iteration();
                    lineChart.getData().retainAll();
                    lineChart.setAnimated(false);
                    series = new XYChart.Series();
                    for (int i = 0; i < evolutionaryAlgorithmCycle.population.size(); i++){
                        series.getData().add(new XYChart.Data(i, (evolutionaryAlgorithmCycle.population.get(i)).getFitness()));
                    }

                    lineChart.getData().add(series);

                    console.writeString("PopSize: "+ evolutionaryAlgorithmCycle.population.size() + " Generation: " + evolutionaryAlgorithmCycle.getGenerationNumber() + "\t Best hypothesis: " + (evolutionaryAlgorithmCycle.getFittest(evolutionaryAlgorithmCycle.population)).getFitness() + "\n");
                    if (evolutionaryAlgorithmCycle.getSolution()){
                        console.writeString("Solution Found");
                        evolutionaryAlgorithmCycle.setRunning(false);
                    }
                }
            }
        };

        eaLoop.start();
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

        public void writeString(String text) {
            for (char letter : text.toCharArray()) {
                output.appendText(String.valueOf((char) letter));
            }
    }

        @Override
        public void write(int i) throws IOException {
            output.appendText(String.valueOf((char) i));
        }
    }

    public void addControl(Node node){
        grid.add(node, 0, controlsRowNumber);
        controlsRowNumber++;
    }



    public static void main(String[] args) {
        launch(args);
    }


}