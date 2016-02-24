public class LolzHypothesis extends Hypothesis {
    public LolzHypothesis(int[] parentGenotype){
        super(parentGenotype);
    }
    public LolzHypothesis() {
        super();
    }

    public void calculateFitness() {
        int firstNumber = getPhenotype()[0];
        double tempFitness = 1.0;

        for (int i = 1; i < Constants.BITSIZE; i++) {
            if (firstNumber == 0 && i + 1 > Constants.LOLZ_THRESHOLD){
                break;
            }
            if (getPhenotype()[i] == firstNumber){
                tempFitness += 1;
            }else{
                break;
            }
        }
        this.fitness = (tempFitness/getPhenotype().length);
    }

    public void development(){
        this.phenotype = this.genotype;
    }

    public void calculateFitness2(){
        double leadingBit = getPhenotype()[0];
        double leadingCount = 0.0;
        for(int bit: getPhenotype()){
            if (leadingBit == 0 && leadingCount >= Constants.LOLZ_THRESHOLD) {
                break;
            }
            if (bit == leadingBit) {
                leadingCount++;
            } else {
                break;
            }
        }
        fitness = leadingCount / getPhenotype().length;
    }
    @Override
    public LolzHypothesis getNewChild(int[] childGenotype){
        return new LolzHypothesis(childGenotype);
    }
}
