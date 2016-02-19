import java.util.ArrayList;
import java.util.List;

public abstract class Hypothesis {

    public Hypothesis(int[] genotypeBitstring, List<Hypothesis> parents) {
    }
    public Hypothesis(){

    }
    public abstract void calculateFitness();

    public abstract double getFitness();

    public abstract void development();

    public abstract void calculateSigma(double averageFitness, double standardDeviation);

    public abstract double getSigma();

    public abstract void mutate();

    public abstract ArrayList<Hypothesis> crossover(Hypothesis parent);

    public abstract int[] getGenotype();

    public abstract int[] getPhenotype();
}
