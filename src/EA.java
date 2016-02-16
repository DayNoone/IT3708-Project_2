import java.util.ArrayList;

public class EA implements EvolutionaryCycle{

    int generationNumber;
    Boolean running;
    ArrayList<Hypothesis> hypothesises;

    public EA(ArrayList<Hypothesis> initialGeneration){
        hypothesises = initialGeneration;
        running = true;

        generationNumber = 0;
    }
    public void stopCycle() {
        running = false;
    }
    public void restartCycle(ArrayList<Hypothesis> initialGeneration) {
        // Could be replaced by just creating new EA
        hypothesises = initialGeneration;
        generationNumber = 0;
        running = true;
    }

    public void iteration() {
        System.out.println("Generation: " + generationNumber);
        if (generationNumber != 0){
            newGeneration();
        }
        development();
        adultSelection();
        retainAdults();
        parentSelection();
        reproduction();
        generationNumber++;
    }


    @Override
    public void newGeneration() {

    }

    @Override
    public void development() {

    }

    @Override
    public void adultSelection() {

    }

    @Override
    public void retainAdults() {

    }

    @Override
    public void parentSelection() {

    }

    @Override
    public void reproduction() {

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
