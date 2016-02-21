import java.util.Random;

public abstract class Hypothesis {
    public int[] genotype = new int[Constants.BITSIZE];
    int[] phenotype;
    double fitness;
    double sigma;

    static Random random = new Random();

    public Hypothesis(int[] genotypeBitstring) {
        this.genotype = genotypeBitstring;
        this.phenotype = genotype;
        calculateFitness();
    }
    public Hypothesis(){

    }
    public abstract void calculateFitness();

    public abstract void development();

    public double getFitness() {
        return fitness;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public int[] getGenotype() {
        return genotype;
    }

    public int[] getPhenotype() {
        return phenotype;
    }

    public abstract Hypothesis getNewChild(int[] childGenotype);


}
