import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class Charts {
    GridPane grid;

    XYChart.Series<Number, Number> series;
    XYChart.Series<Number, Number> bestFitnessSeries;
    XYChart.Series<Number, Number> averageFitnessSeries;
    XYChart.Series<Number, Number> standardDeviationSeries;
    final NumberAxis yAxis = new NumberAxis(0, 1, 0.1);
    final NumberAxis yAxis1 = new NumberAxis(0, 1, 0.1);
    final NumberAxis yAxis2 = new NumberAxis(0, 1, 0.1);
    final NumberAxis xAxis = new NumberAxis();
    final LineChart<Number,Number> generationFitnessPlot = new LineChart<Number,Number>(xAxis, yAxis);
    final LineChart<Number,Number> bestFitnessPlot = new LineChart<Number,Number>(new NumberAxis(), yAxis1);
    final LineChart<Number,Number> averageFitnessPlot = new LineChart<Number,Number>(new NumberAxis(), yAxis2);
    final LineChart<Number,Number> standardDeviationPlot = new LineChart<Number,Number>(new NumberAxis(),new NumberAxis());


    public Charts() {
        grid = new GridPane();

        generationFitnessPlot.setTitle("Population fitness");
        generationFitnessPlot.setLegendVisible(false);
        series = new XYChart.Series<>();
        series.setName("My portfolio");
        grid.add(generationFitnessPlot, 0, 0);

        bestFitnessSeries = new XYChart.Series<>();
        bestFitnessPlot.setTitle("Best fitness");
        bestFitnessPlot.setAnimated(false);
        bestFitnessPlot.setLegendVisible(false);
        bestFitnessPlot.setCreateSymbols(false);
        bestFitnessPlot.setStyle("");
        bestFitnessPlot.getData().add(bestFitnessSeries);
        grid.add(bestFitnessPlot, 1, 0);

        averageFitnessSeries = new XYChart.Series<>();
        averageFitnessPlot.setTitle("Average fitness");
        averageFitnessPlot.setAnimated(false);
        averageFitnessPlot.setLegendVisible(false);
        averageFitnessPlot.setCreateSymbols(false);
        averageFitnessPlot.getData().add(averageFitnessSeries);
        grid.add(averageFitnessPlot, 0, 1);

        standardDeviationSeries = new XYChart.Series<>();
        standardDeviationPlot.setTitle("Standard deviation");
        standardDeviationPlot.setAnimated(false);
        standardDeviationPlot.setLegendVisible(false);
        standardDeviationPlot.setCreateSymbols(false);
        standardDeviationPlot.getData().add(standardDeviationSeries);
        grid.add(standardDeviationPlot, 1, 1);
    }
    public GridPane getGrid(){
        return this.grid;
    }

    public void updateLinechart(EA evolutionaryAlgorithmCycle) {
        ArrayList<Hypothesis> population = evolutionaryAlgorithmCycle.getPopulation();


        if (population.size() < 100 || evolutionaryAlgorithmCycle.getGenerationNumber() % 10 == 0) {
        }
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
    public void clearPlots() {
        bestFitnessSeries.getData().retainAll();
        averageFitnessSeries.getData().retainAll();
        standardDeviationSeries.getData().retainAll();
    }
}
