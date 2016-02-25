import java.util.Arrays;

public class OneMaxHypothesis extends Hypothesis {

    //static int[] targetBitstring = {1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1};
    public OneMaxHypothesis(int[] parentGenotype) {
        super(parentGenotype);
    }

    public OneMaxHypothesis(){
        super();
    }

    public void development(){
        this.phenotype = this.genotype;
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
