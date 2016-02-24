import java.util.*;

public class SurprisingHypothesis extends Hypothesis {
    public SurprisingHypothesis(int[] parentGenotype) {
        super(parentGenotype);
    }
    public SurprisingHypothesis(){
        for (int i = 0; i < Constants.BITSIZE; i++){
            genotype[i] = random.nextInt(Constants.SUPRISING_SYMBOL_SIZE);
        }
        this.phenotype = new int[Constants.BITSIZE];
    }

    public void calculateFitness() {

        int violations = 0;

        if (Constants.GLOBAL) {
            Map<Integer, Map<Integer, Set<Integer>>> map = new HashMap<>();
            for (int i = 0; i < phenotype.length; i++) {
                for (int j = i+1; j < phenotype.length; j++) {
                    int distance = j - i - 1;
                    if (map.get(phenotype[i]) == null){
                        Set<Integer> hashSet = new HashSet<Integer>();
                        Map<Integer, Set<Integer>> innerHash = new HashMap<>();
                        hashSet.add(distance);
                        innerHash.put(phenotype[j], hashSet);
                        map.put(phenotype[i], innerHash);
                    } else if (map.get(phenotype[i]).get(phenotype[j]) == null){
                        Set<Integer> hashSet = new HashSet<Integer>();
                        hashSet.add(distance);
                        map.get(phenotype[i]).put(phenotype[j], hashSet);
                    } else {
                        if (map.get(phenotype[i]).get(phenotype[j]).contains(distance)) {
                            if (distance == 0 && Constants.GLOBAL) {
                                violations++;
                            }
                            violations++;
                        } else {
                            map.get(phenotype[i]).get(phenotype[j]).add(distance);
                        }

                    }
                }
            }
        } else {
            Map<Integer, Set<Integer>> map = new HashMap<>();
            for (int i = 0; i < phenotype.length-1; i++) {
                if (map.get(phenotype[i]) == null) {
                    Set<Integer> hashSet = new HashSet<Integer>();
                    hashSet.add(phenotype[i+1]);
                    map.put(phenotype[i], hashSet);
                } else if (map.get(phenotype[i]).contains(phenotype[i+1])){
                    violations++;
                } else {
                    map.get(phenotype[i]).add(phenotype[i+1]);
                }
            }
        }
        fitness = 1.0 / (1.0 + violations);
    }



    public void mutate() {
        if (random.nextDouble() < Constants.MUTATION_RATE_ALL) {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                this.getGenotype()[i] = random.nextInt(Constants.SUPRISING_SYMBOL_SIZE);
            }
        } else {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                if (random.nextDouble() < Constants.MUTATION_RATE) {
                    this.getGenotype()[i] = random.nextInt(Constants.SUPRISING_SYMBOL_SIZE);
                }
            }
        }
    }

    public void development(){
        this.phenotype = this.genotype;
    }

    public Hypothesis getNewChild(int[] childGenotype) {
        return new SurprisingHypothesis(childGenotype);
    }
}
