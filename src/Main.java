import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application{
    EA ea;
    GUI gui;
    AnimationTimer eaLoop;

    public void start(Stage primaryStage) throws IOException {
        gui = new GUI(this, primaryStage);
    }

    public void startEA(Constants.algorithms oneMax) {
        // To keep multiple loops from running
        if (eaLoop != null) {
            eaLoop.stop();
        }
        ea = null;
        gui.updateConstants();
        ArrayList<Hypothesis> initialGeneration = initializeGeneration(oneMax);
        ea = new EA(initialGeneration);
        gui.charts.clearPlots();

        eaLoop = new AnimationTimer() {
            public void handle(long now) {
                if(ea.isRunning()){
                    ea.development();
                    Hypothesis fittest = ea.getFittest(ea.getPopulation());
                    if (fittest.getFitness() == 1) {
                        ea.setSolution(true);
                        ea.setRunning(false);
                    }
                    ea.incrementGenerationNumber();

                    if (Constants.CHARTS){
                        gui.charts.updateLinechart(ea);
                    }
                    double averageFitness = ea.getAverageFitness(ea.getPopulation());
                    fittest = ea.getFittest(ea.getPopulation());
                    if (Constants.CONSOLE) {
                        gui.console.writeStringln("Gen: " + ea.getGenerationNumber() +
                                "\t Best: " + String.format("%.2f", fittest.getFitness()) +
                                "\t Pop: " + "Po-" +  ea.getPopulation().size() + " A-" + Constants.ADULTS_SIZE + " Pa-" + Constants.PARENTS_SIZE + " E-" + Constants.ELITISM_SIZE +
                                "\t Avg fitness: " + String.format("%.2f", averageFitness) +
                                "\t SD: " + String.format("%.3f", ea.getStandardDeviation(ea.getPopulation(), averageFitness)) +
                                " \t Pheno: " + Arrays.toString(fittest.getPhenotype()));
                    } else if(ea.getGenerationNumber() % 100 == 0) {
                        gui.console.writeStringln("Generation: " + ea.getGenerationNumber());
                    }
                    if (ea.getSolution()) {
                        gui.console.writeStringln("--------------------------------- Solution found ---------------------------------");
                        gui.console.writeStringln("Generation number: "+ea.getGenerationNumber());
                        gui.console.writeStringln(Arrays.toString(fittest.getPhenotype()));
                        eaLoop.stop();
                    }
                    ea.adultSelection();
                    ea.parentSelection();
                    ea.reproduction();
                }
            }
        };
        eaLoop.start();
    }

    private ArrayList<Hypothesis> initializeGeneration(Constants.algorithms algorithm) {
        ArrayList<Hypothesis> initialGeneration = new ArrayList<Hypothesis>();
        for(int i = 0; i < Constants.GENERATION_SIZE; i++){
            switch (algorithm) {
                case ONE_MAX:
                    initialGeneration.add(new OneMaxHypothesis());
                    break;
                case LOLZ:
                    initialGeneration.add(new LolzHypothesis());
                    break;
                case SURPRISE:
                    initialGeneration.add(new SurprisingHypothesis());
                    break;
            }
        }
        return initialGeneration;
    }

    public static void main(String[] args) {
        launch(args);
    }
}