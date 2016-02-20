public class Constants {

    public static final double SCENE_HEIGHT = 800;
    public static final double SCENE_WIDTH = 1820;

    public static int GENOTYPE_ONEMAX_SIZE = 40;

    public static int GENERATION_SIZE = 20;
    public static int ADULTS_SIZE = 10;
    public static final int ELITISM_SIZE = 1;
    public static int PARENTS_SIZE = GENERATION_SIZE - ELITISM_SIZE;
    public static boolean MUTATION = true;
    public static boolean CROSSOVER = true;
    public static double MUTATION_RATE = 0.01;
    public static double CROSSOVER_RATE = 0.8;
    public static int TOURNAMENT_GROUP_SIZE = 5;

    public static double TOURNAMENT_PROBABILITY = 0.5;

    public static adultSelection ADULT_SELECTION = adultSelection.FULL_GENERATION;
    public static parentSelection PARENT_SELECTION = parentSelection.FITNESS_PROPORTIONATE;

    public enum adultSelection{
        FULL_GENERATION, OVER_PRODUCTION, GENERATIONAL_MIXING;
    }
    public enum parentSelection{
        FITNESS_PROPORTIONATE, SIGMA_SCALING, TOURNAMENT_SELECTION, UNIFORM_SELECTION;
    }
    public enum algorithms{
        ONE_MAX, LOLZ, SURPRISE;
    }
}
