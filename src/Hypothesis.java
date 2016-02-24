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
        for (int i = 0; i < Constants.BITSIZE; i++){
            genotype[i] = random.nextInt(2);
        }
        this.phenotype = new int[Constants.BITSIZE];
    }
    public abstract void calculateFitness();

    public abstract void development();

    public void mutate() {
        if (random.nextDouble() < Constants.MUTATION_RATE_ALL) {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                this.getGenotype()[i] = this.getGenotype()[i] == 0 ? 1 : 0;
            }
        } else {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                if (random.nextDouble() < Constants.MUTATION_RATE) {
                    this.getGenotype()[i] = this.getGenotype()[i] == 0 ? 1 : 0;
                }
            }
        }
    }

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
