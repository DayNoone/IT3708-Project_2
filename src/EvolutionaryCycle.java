import java.util.ArrayList;

public interface EvolutionaryCycle {

    // genotypes -> needs bitstring
    // phenotypes -> solutions


    // converting genotypes to phenotypes
    void development();

    void adultSelection();

    // based on fitness
    void parentSelection();

    // Crossovers, mutation
    void reproduction();

}
