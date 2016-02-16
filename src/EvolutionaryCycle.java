import java.util.ArrayList;

public interface EvolutionaryCycle {

    // genotypes -> needs bitstring
    // phenotypes -> solutions

    // only new generation or new generation + parents
    void newGeneration();

    // converting genotypes to phenotypes
    void development();

    void adultSelection();

    // Aging & death
    void retainAdults();

    // based on fitness
    void parentSelection();

    // Crossovers, mutation
    void reproduction();

}
