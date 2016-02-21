public class SurprisingHypothesis extends Hypothesis {
    public SurprisingHypothesis(int[] parentGenotype) {
        super(parentGenotype);
    }
    public SurprisingHypothesis(){

    }

    public void calculateFitness() {

    }

    public double getFitness() {
        return 0;
    }

    public void development() {

    }

    public double getSigma() {
        return 0;
    }

    public int[] getGenotype() {
        return new int[0];
    }

    public int[] getPhenotype() {
        return new int[0];
    }

    @Override
    public Hypothesis getNewChild(int[] childGenotype) {
        return null;
    }
}
