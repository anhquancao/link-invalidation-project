import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import tm.Property;
import tm.OntologyReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public class Detection {

    private static final String DATA_PATH = "/home/quan/dataset/";

    private static final double THRESHOLD = 0.9;

    private void run() throws FileNotFoundException {
        String path0 = DATA_PATH + "data/000/onto.owl";
        OntologyReader p0 = new OntologyReader(path0);
        Map<String, Property> functionProperties = p0.getFunctionalProperties(THRESHOLD);


        System.out.println("Extracted " + functionProperties.size() + " function properties");

        for (Object o : functionProperties.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Property prop = (Property) pair.getValue();
            System.out.println(prop);
        }




    }

    public static void main(String[] args) {
        Detection detection = new Detection();
        try {
            detection.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
