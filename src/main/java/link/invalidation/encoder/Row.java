package link.invalidation.encoder;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Row {
    private List<Double> propertyValues;
    private int target;

    public Row() {
        propertyValues = new LinkedList<>();
    }

    public void add(double similarity) {
        this.propertyValues.add(similarity);
    }

    public List<Double> getPropertyValues() {
        return this.propertyValues;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    @Override
    public String toString() {
        String res = String.join(",", this.propertyValues.stream().map(x -> x + "").collect(Collectors.toList()));
        res += "," + target;
        
        return res;
    }
}
