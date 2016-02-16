import java.util.ArrayList;
import java.util.List;

public class Hypothesis {

    int bitstring;
    List<Hypothesis> parents = new ArrayList<Hypothesis>();


    public Hypothesis(int bitstring, List<Hypothesis> parents){
        this.bitstring = bitstring;
        this.parents = parents;


    }
}
