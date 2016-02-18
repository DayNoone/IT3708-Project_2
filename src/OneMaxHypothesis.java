import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OneMaxHypothesis extends Hypothesis {
    int[] genotype = new int[Constants.GENOTYPE_ONEMAX_SIZE];
    int[] phenotype = new int[Constants.GENOTYPE_ONEMAX_SIZE];
    double fitness;
    double sigma;

    Random random = new Random();

    public OneMaxHypothesis(OneMaxHypothesis parent) {
        genotype = parent.genotype;
    }

    public OneMaxHypothesis(){
        for (int i = 0; i < genotype.length; i++){
            genotype[i] = random.nextInt(2);
        }
    }

    public void calculateFitness(){
        double oneCount = 0.0;
        for(int i: genotype){
            if (i == 1) oneCount++;
        }
        this.fitness = (double) oneCount / genotype.length;
    }
    public void calculateSigma(ArrayList<Hypothesis> adults, double averageFitness, double standardDeviation){
        sigma = ((fitness - averageFitness) / (2 * standardDeviation)) + 1;
    }

    public double getFitness() {
        return fitness;
    }
    public double getSigma() {
        return sigma;
    }

    public void mutate() {
        for (int i = 0; i < phenotype.length; i++) {
            if (random.nextDouble() <= Constants.MUTATION_RATE){
                if (phenotype[i] == 0) {
                    phenotype[i] = 1;
                } else {
                    phenotype[i] = 0;
                }
            }
        }
    }

    public void development(){
        this.phenotype = this.genotype;
    }

    public String toString(){
        return Arrays.toString(genotype);
    }
}
