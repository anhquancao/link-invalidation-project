package link.invalidation;

import link.invalidation.models.MyProperty;
import link.invalidation.models.Pair;
import link.invalidation.tm.OntologyReader;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        OntologyReader p0 = new OntologyReader(Constant.SRC_PATH);
        Map<String, MyProperty> functionProperties = p0.getFunctionalProperties(Constant.THRESHOLD);

        String path1 = Constant.REF_PATH + "onto.owl";
        OntologyReader p1 = new OntologyReader(path1);

        DataGenerator generator = new DataGenerator();

        List<Pair> correctPairs = generator.loadCorrectPairs();

        // We generate the amout of incorrect pairs equal to the amount of correct pairs
        // So, if we predict every pairs are correct or every pairs are incorrect,
        // we will have a baseline of 50%
        List<Pair> pairs = generator.generateIncorrectPairs(correctPairs, correctPairs.size());

        LogicalDetection logicalDetection = new LogicalDetection();
        logicalDetection.detect(pairs, functionProperties, p0, p1);

    }
}
