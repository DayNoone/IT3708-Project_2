import java.util.ArrayList;
import java.util.List;

public class Hypothesis {

    int[] genotypeBitstring;
    List<Hypothesis> parents = new ArrayList<Hypothesis>();


    public Hypothesis(int[] genotypeBitstring, List<Hypothesis> parents){
        this.genotypeBitstring = genotypeBitstring;
        this.parents = parents;
    }
    public Hypothesis(){

    }

}
