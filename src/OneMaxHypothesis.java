import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class OneMaxHypothesis extends Hypothesis {
    public int[] genotype = new int[Constants.GENOTYPE_ONEMAX_SIZE];
    int[] phenotype;
    double fitness;
    double sigma;

    static Random random = new Random();

    public OneMaxHypothesis(int[] parentGenotype) {
        this.genotype = parentGenotype;
        this.phenotype = genotype;
        calculateFitness();
    }

    public OneMaxHypothesis(){
        for (int i = 0; i < genotype.length; i++){
            genotype[i] = random.nextInt(2);
        }
        this.phenotype = new int[Constants.GENOTYPE_ONEMAX_SIZE];
    }

    public void calculateFitness(){
        double oneCount = 0;
        for(int i: phenotype){
            if (i == 1) oneCount++;
        }
        this.fitness = oneCount / phenotype.length;
    }
    public void calculateSigma(double averageFitness, double standardDeviation){
        sigma = 1 + ((fitness - averageFitness) / (2 * standardDeviation));
    }

    public double getFitness() {
        return fitness;
    }
    public double getSigma() {
        return sigma;
    }

    public void mutate() {
        for (int i = 0; i < Constants.GENOTYPE_ONEMAX_SIZE; i++) {
            if (random.nextDouble() < Constants.MUTATION_RATE){
                if (genotype[i] == 0) {
                    genotype[i] = 1;
                } else {
                    genotype[i] = 0;
                }
            }
        }
    }

    public ArrayList<Hypothesis> crossover(Hypothesis parent) {
        int crosspoint = random.nextInt(Constants.GENOTYPE_ONEMAX_SIZE);
        int[] childGenotype1 = new int[Constants.GENOTYPE_ONEMAX_SIZE];
        int[] childGenotype2 = new int[Constants.GENOTYPE_ONEMAX_SIZE];
        for (int i = 0; i < Constants.GENOTYPE_ONEMAX_SIZE; i++){
            if(i > crosspoint){
                childGenotype1[i] = this.getGenotype()[i];
                childGenotype2[i] = parent.getGenotype()[i];
            }
            else {
                childGenotype1[i] = parent.getGenotype()[i];
                childGenotype2[i] = this.getGenotype()[i];
            }
        }
        ArrayList<Hypothesis> children = new ArrayList<Hypothesis>();
        children.add(new OneMaxHypothesis(childGenotype1));
        children.add(new OneMaxHypothesis(childGenotype2));
        return children;
    }

    public int[] getGenotype() {
        return genotype;
    }

    public int[] getPhenotype() {
        return phenotype;
    }

    public void development(){
        this.phenotype = this.genotype;
    }

    public String toString(){
        return Arrays.toString(genotype);
    }
}
