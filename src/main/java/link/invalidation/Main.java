package link.invalidation;

import link.invalidation.detector.LogicalDetector;
import link.invalidation.generator.DataGenerator;
import link.invalidation.metrics.Evaluator;
import link.invalidation.models.PropertyWrapper;
import link.invalidation.models.Pair;
import link.invalidation.utils.Constant;
import link.invalidation.utils.OntologyReader;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        HashSet<String> blackList = new HashSet<>();
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/amount");
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/calling_code");
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/currency");

        OntologyReader p0 = new OntologyReader(Constant.SRC_PATH);
        Map<String, PropertyWrapper> functionProperties = p0.getFunctionalProperties(Constant.THRESHOLD, blackList);

        String path1 = Constant.REF_PATH + "onto.owl";
        OntologyReader p1 = new OntologyReader(path1);

        DataGenerator generator = new DataGenerator();

        List<Pair> correctPairs = generator.loadCorrectPairs();

        // We generate the amout of incorrect pairs equal to the amount of correct pairs
        // So, if we predict every pairs are correct or every pairs are incorrect,
        // we will have a baseline of 50%
        List<Pair> pairs = generator.generateIncorrectPairs(correctPairs, correctPairs.size());

        LogicalDetector logicalDetection = new LogicalDetector();
        List<Pair> predictions = logicalDetection.detect(pairs, functionProperties, p0, p1, false);

        Evaluator evaluator = new Evaluator();
        evaluator.evaluate(pairs, predictions);

        System.out.println("Accuracy: " + evaluator.accuracy());
        System.out.println("Precision: " + evaluator.precision());
        System.out.println("Recall: " + evaluator.recall());
        System.out.println("F1 score: " + evaluator.F1Score());
    }
}
