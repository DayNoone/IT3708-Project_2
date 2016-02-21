import java.util.ArrayList;
import java.util.Random;

public class EA implements EvolutionaryCycle{
    private int generationNumber;
    private Boolean running;
    private ArrayList<Hypothesis> population;
    private ArrayList<Hypothesis> adults = new ArrayList<Hypothesis>();
    private ArrayList<Hypothesis> parents = new ArrayList<Hypothesis>();

    private Boolean solutionFound = false;

    private Random random = new Random();
    private boolean solution;

    public EA(ArrayList<Hypothesis> initialGeneration){
        population = initialGeneration;
        running = true;

        generationNumber = 0;
    }

    public void development() {
        for(Hypothesis hyp: population) {
            hyp.development();
            hyp.calculateFitness();
            System.out.println("");
        }
    }

    @Override
    public void adultSelection() {
        ArrayList<Hypothesis> tempPopulation = new ArrayList<Hypothesis>(population);
        switch (Constants.ADULT_SELECTION){
            case FULL_GENERATION:
                adults.clear();
                adults.addAll(population);
                break;
            case OVER_PRODUCTION:
                adults.clear();
                while(adults.size() <= Constants.ADULTS_SIZE) {
                    Hypothesis hyp = fitnessRoulette(tempPopulation);
                    adults.add(hyp);
                    tempPopulation.remove(hyp);
                }
                break;
            case GENERATIONAL_MIXING:
                ArrayList<Hypothesis> selectionPopulation = new ArrayList<Hypothesis>();
                selectionPopulation.addAll(population);
                selectionPopulation.addAll(adults);
                adults.clear();
                while(adults.size() <= Constants.ADULTS_SIZE) {
                    Hypothesis hyp = fitnessRoulette(selectionPopulation);
                    adults.add(hyp);
                    tempPopulation.remove(hyp);
                }
                break;
        }
    }

    private Hypothesis fitnessRoulette(ArrayList<Hypothesis> population) {
        double totalFitness = getTotalFitness(population);
        double x = random.nextDouble() * totalFitness;
        for (Hypothesis hypothesis : population) {
            x -= hypothesis.getFitness();
            if (x <= 0) {
                return hypothesis;
            }
        }
        throw new NullPointerException("No blank piece found!");
    }

    @Override
    public void parentSelection() {
        parents.clear();
        Hypothesis parent1;
        Hypothesis parent2;
        ArrayList<Hypothesis> tempAdults = new ArrayList<>();
        tempAdults.addAll(adults);
        switch (Constants.PARENT_SELECTION){
            case FITNESS_PROPORTIONATE:
                while(parents.size() < Constants.PARENTS_SIZE) {
                    parent1 = fitnessRoulette(adults);
                    adults.remove(parent1);
                    parent2 = fitnessRoulette(adults);
                    adults.add(parent1);
                    parents.add(parent1);
                    parents.add(parent2);
                }
                break;
            case SIGMA_SCALING:
                while(parents.size() < Constants.PARENTS_SIZE) {
                    parent1 = sigmaRoulette(tempAdults);
                    tempAdults.remove(parent1);
                    parent2 = sigmaRoulette(tempAdults);
                    tempAdults.add(parent1);
                    parents.add(parent1);
                    parents.add(parent2);
                }
                break;
            case TOURNAMENT_SELECTION:
                while(parents.size() < Constants.PARENTS_SIZE) {
                    tempAdults.clear();
                    tempAdults.addAll(adults);
                    ArrayList<Hypothesis> tournamentGroup = new ArrayList<Hypothesis>();
                    for (int i = 0; i < Constants.TOURNAMENT_GROUP_SIZE; i++){
                        Hypothesis chosen = adults.get(random.nextInt(adults.size()));
                        tournamentGroup.add(chosen);
                        tempAdults.remove(chosen);
                    }
                    tempAdults.addAll(tournamentGroup);
                    parents.add(findTournamentWinner(tournamentGroup));
                    parents.add(findTournamentWinner(tournamentGroup));
                }
                break;
            case UNIFORM_SELECTION:
                while(parents.size() < Constants.PARENTS_SIZE) {
                    parent1 = adults.get(random.nextInt(adults.size()));
                    adults.remove(parent1);
                    parent2 = adults.get(random.nextInt(adults.size()));
                    adults.add(parent1);
                    parents.add(parent1);
                    parents.add(parent2);
                }
                break;
        }
    }

    private Hypothesis findTournamentWinner(ArrayList<Hypothesis> tournamentGroup) {
        if(random.nextDouble() < Constants.TOURNAMENT_PROBABILITY) {
            return getFittest(tournamentGroup);
        } else {
            return tournamentGroup.get(random.nextInt(tournamentGroup.size()));
        }
    }

    private Hypothesis sigmaRoulette(ArrayList<Hypothesis> population) {
        double averageFitness = getAverageFitness(population);
        double standardDeviation = getStandardDeviation(population, averageFitness);
        for (Hypothesis hypothesis: population){
            calculateSigma(hypothesis, averageFitness, standardDeviation);
        }
        double totalSigma = getTotalSigma(population);
        double x = random.nextDouble() * totalSigma;
        for (Hypothesis hypothesis : population) {
            x -= hypothesis.getSigma();
            if (x <= 0) {
                return hypothesis;
            }
        }
        throw new NullPointerException("Sigma roulette not returning hypothesis");
    }

    @Override
    public void reproduction() {
        // Elitism
        ArrayList<Hypothesis> fittestGroup = new ArrayList<Hypothesis>();
        Hypothesis fittest;
        for (int i = 0; i < Constants.ELITISM_SIZE; i++) {
            fittest = getFittest(population);
            fittestGroup.add(fittest);
            population.remove(fittest);
        }
        population.clear();
        population.addAll(fittestGroup);



        if (Constants.CROSSOVER) {
            for (int i = 0; i < parents.size(); i = i + 2) {
                if (random.nextDouble() < Constants.CROSSOVER_RATE) {
                    Hypothesis parent1 = parents.get(i);
                    Hypothesis parent2 = parents.get(i+1);
                    ArrayList<Hypothesis> children = crossover(parent1, parent2);
                    if (Constants.MUTATION) {
                        for (Hypothesis child: children){
                            mutate(child);
                        }
                    }
                    population.addAll(children);
                } else {
                    population.add(parents.get(i));
                    population.add(parents.get(i + 1));
                }
            }
        }
    }

    public ArrayList<Hypothesis> crossover(Hypothesis parent1, Hypothesis parent2) {
        int crosspoint = random.nextInt(Constants.BITSIZE);
        int[] childGenotype1 = new int[Constants.BITSIZE];
        int[] childGenotype2 = new int[Constants.BITSIZE];
        for (int i = 0; i < Constants.BITSIZE; i++){
            if(i > crosspoint){
                childGenotype1[i] = parent1.getGenotype()[i];
                childGenotype2[i] = parent2.getGenotype()[i];
            }
            else {
                childGenotype1[i] = parent2.getGenotype()[i];
                childGenotype2[i] = parent1.getGenotype()[i];
            }
        }
        ArrayList<Hypothesis> children = new ArrayList<Hypothesis>();
        children.add(parent1.getNewChild(childGenotype1));
        children.add(parent1.getNewChild(childGenotype2));
        return children;
    }

    public void mutate(Hypothesis hypothesis) {
        if (random.nextDouble() < Constants.MUTATION_RATE_ALL) {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                hypothesis.getGenotype()[i] = hypothesis.getGenotype()[i] == 0 ? 1 : 0;
            }
        } else {
            for (int i = 0; i < Constants.BITSIZE; i++) {
                if (random.nextDouble() < Constants.MUTATION_RATE) {
                    hypothesis.getGenotype()[i] = hypothesis.getGenotype()[i] == 0 ? 1 : 0;
                }
            }
        }

    }
    public Hypothesis getFittest(ArrayList<Hypothesis> population) {
        Hypothesis bestHypothesis = population.get(0);
        for (Hypothesis hypothesis: population){
            if (hypothesis.getFitness() > bestHypothesis.getFitness()){
                bestHypothesis = hypothesis;
            }
        }
        return bestHypothesis;
    }
    public double getTotalFitness(ArrayList<Hypothesis> population) {
        double total = 0;
        for (Hypothesis hypothesis: population){
            total += hypothesis.getFitness();
        }
        return total;
    }
    private void calculateSigma(Hypothesis hypothesis, double averageFitness, double standardDeviation){
        hypothesis.setSigma(1 + ((hypothesis.getFitness() - averageFitness) / (2 * standardDeviation)));
    }
    private double getTotalSigma(ArrayList<Hypothesis> population) {
        double total = 0;
        for (Hypothesis hypothesis: population){
            total += hypothesis.getSigma();
        }
        return total;
    }
    public double getStandardDeviation(ArrayList<Hypothesis> population, double averageFitness) {
        double standardDeviation = 0.0;
        for(Hypothesis adult: population){
            standardDeviation += Math.pow((adult.getFitness() - averageFitness), 2);
        }
        return Math.sqrt(standardDeviation / population.size());
    }
    public double getAverageFitness(ArrayList<Hypothesis> population) {
        return getTotalFitness(population) / population.size();
    }
    public ArrayList<Hypothesis> getPopulation(){
        return this.population;
    }
    public int getGenerationNumber() {
        return generationNumber;
    }
    public boolean isRunning() {
        return running;
    }
    public void setRunning(boolean running) {
        this.running = running;
    }
    public boolean getSolution() {
        return solutionFound;
    }

    public void setSolution(boolean solution) {
        this.solution = solution;
    }

    public void incrementGenerationNumber() {
        generationNumber++;
    }
}
