public class Constants {

    public static final double SCENE_HEIGHT = 1000;
    public static final double SCENE_WIDTH = 800;
    public static final int TOURNAMENT_GROUP_SIZE = 5;
    public static final double TOURNAMENT_PROBABILITY = 0.4;


    public static double FITNESS_THRESHOLD = 10;

    public static boolean MUTATION = false;
    public static boolean CROSSOVER = true;
    public static double MUTATION_RATE = 0.2;
    public static double GENERATION_SIZE = 100;
    public static final int ADULTS_SIZE = 20;
    public static final int PARENTS_SIZE = 10;
    public static double CROSSOVER_RATE = 0.8;

    public static int GENOTYPE_ONEMAX_SIZE = 40;

    public static adultSelection ADULT_SELECTION = adultSelection.GENERATIONAL_MIXING;
    public static parentSelection PARENT_SELECTION = parentSelection.TOURNAMENT_SELECTION;

    public enum adultSelection{
        FULL_GENERATION, OVER_PRODUCTION, GENERATIONAL_MIXING;
    }
    public enum parentSelection{
        FITNESS_PROPORTIONATE, SIGMA_SCALING, TOURNAMENT_SELECTION, UNIFORM_SELECTION;
    }
}
