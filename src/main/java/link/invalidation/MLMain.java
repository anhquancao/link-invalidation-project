package link.invalidation;

import link.invalidation.encoder.CSVEncoder;
import link.invalidation.encoder.Row;
import link.invalidation.generator.DataGenerator;
import link.invalidation.models.Pair;
import link.invalidation.models.PropertyWrapper;
import link.invalidation.utils.Constant;
import link.invalidation.utils.OntologyReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class MLMain {
    public static void main(String[] args) throws FileNotFoundException {
        OntologyReader p0 = new OntologyReader(Constant.SRC_PATH);
        Map<String, PropertyWrapper> properties = p0.getAllProperties(null);

        System.out.println("Number of properties: " + properties.size());

        String path1 = Constant.REF_PATH + "onto.owl";
        OntologyReader p1 = new OntologyReader(path1);

        DataGenerator generator = new DataGenerator();

        List<Pair> correctPairs = generator.loadCorrectPairs();

        // We generate the amout of incorrect pairs equal to the amount of correct pairs
        // So, if we predict every pairs are correct or every pairs are incorrect,
        // we will have a baseline of 50%
        List<Pair> pairs = generator.generateIncorrectPairs(correctPairs, 5000);

        CSVEncoder csvEncoder = new CSVEncoder();
        List<Row> data = csvEncoder.encode(pairs, properties, p0, p1, false);

        System.out.println(data.size());
        System.out.println(data.get(0).getPropertyValues().size());

        PrintWriter pw = new PrintWriter(new File("data.csv"));
        StringBuilder sb = new StringBuilder();
        for (Row r : data) {
            sb.append(r.toString());
            sb.append('\n');
        }
        pw.write(sb.toString());
        pw.close();
        System.out.println("done!");
    }
}
