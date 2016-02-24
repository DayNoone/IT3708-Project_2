import java.util.Arrays;

public class OneMaxHypothesis extends Hypothesis {

    public OneMaxHypothesis(int[] parentGenotype) {
        super(parentGenotype);
    }

    public OneMaxHypothesis(){
        for (int i = 0; i < Constants.BITSIZE; i++){
            genotype[i] = random.nextInt(2);
        }
        this.phenotype = new int[Constants.BITSIZE];
    }

    public void calculateFitness(){
        double oneCount = 0;
        for(int i: phenotype){
            if (i == 1) oneCount++;
        }
        this.fitness = oneCount / phenotype.length;
    }

    public OneMaxHypothesis getNewChild(int[] childGenotype){
        return new OneMaxHypothesis(childGenotype);
    }

    public String toString(){
        return Arrays.toString(genotype);
    }
}
