import java.util.ArrayList;
import java.util.List;

public abstract class Hypothesis {

    int[] genotype;
    List<Hypothesis> parents = new ArrayList<Hypothesis>();
    private int fitness;
    private double sigma;


    public Hypothesis(int[] genotypeBitstring, List<Hypothesis> parents) {
    }
    public Hypothesis(){

    }
    public abstract void calculateFitness();

    public abstract double getFitness();

    public abstract void development();

    public abstract void calculateSigma(ArrayList<Hypothesis> adults, double averageFitness, double standardDeviation);

    public abstract double getSigma();

    public abstract void mutate();
}
