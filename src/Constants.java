public class Constants {

    public static final double SCENE_HEIGHT = 1000;
    public static final double SCENE_WIDTH = 1300;
    public static final double MUTATION_RATE_ALL = 0.01;

    public static int BITSIZE = 40;
    public static int LOLZ_THRESHOLD = 30;

    public static int GENERATION_SIZE = 40;
    public static int ADULTS_SIZE = 20;
    public static final int ELITISM_SIZE = (int) (GENERATION_SIZE*0.05);
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
