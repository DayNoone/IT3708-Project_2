import java.util.Arrays;
import java.util.List;

public class OneMaxHypothesis extends Hypothesis {
    int genotypeSize;
    int bitstringSize;
    int[] bitstring;

    public OneMaxHypothesis(int[] bitstring, List<Hypothesis> parents) {
        super(bitstring, parents);
    }

    public OneMaxHypothesis(){
        bitstring = new int[Settings.GENOTYPE_ONEMAX_SIZE];
    }

    public double calculateFitness(){
        double oneCount = 0.0;
        for(int i: bitstring){
            if (i == 1) oneCount++;
        }
        return (double) oneCount / bitstring.length;
    }

    public String toString(){
        return Arrays.toString(bitstring);
    }
}
