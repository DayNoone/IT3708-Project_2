public class Constants {

    public static final double SCENE_HEIGHT = 800;
    public static final double SCENE_WIDTH = 1800;
    public static final int TOURNAMENT_GROUP_SIZE = 5;
    public static final double TOURNAMENT_PROBABILITY = 0.5;


    public static double FITNESS_THRESHOLD = 10;

    public static boolean MUTATION = true;
    public static boolean CROSSOVER = true;
    public static double MUTATION_RATE = 0.01;
    public static double CROSSOVER_RATE = 0.8;
    public static int GENERATION_SIZE = 20;
    public static final int ADULTS_SIZE = 10;
    public static final int ELITISM_SIZE = 1;
    public static final int PARENTS_SIZE = GENERATION_SIZE - ELITISM_SIZE;

    public static int GENOTYPE_ONEMAX_SIZE = 40;

    public static adultSelection ADULT_SELECTION = adultSelection.GENERATIONAL_MIXING;
    public static parentSelection PARENT_SELECTION = parentSelection.SIGMA_SCALING;

    public enum adultSelection{
        FULL_GENERATION, OVER_PRODUCTION, GENERATIONAL_MIXING;
    }
    public enum parentSelection{
        FITNESS_PROPORTIONATE, SIGMA_SCALING, TOURNAMENT_SELECTION, UNIFORM_SELECTION;
    }
}
