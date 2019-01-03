import tm.Property;
import tm.OntologyReader;

import java.io.FileNotFoundException;
import java.util.Map;

public class Detection {

    private static final String DATA_PATH = "/home/quan/dataset/";

    private static final double THRESHOLD = 0.9;

    private void run() throws FileNotFoundException {
//        String path0 = DATA_PATH + "data/000/onto.owl";
//        OntologyReader p0 = new OntologyReader(path0);
//        Map<String, Property> functionProperties = p0.getFunctionalProperties(THRESHOLD);
//
//        System.out.println("Extracted " + functionProperties.size() + " function properties");
//
//        for (Object o : functionProperties.entrySet()) {
//            Map.Entry pair = (Map.Entry) o;
//            Property prop = (Property) pair.getValue();
//            System.out.println(prop);
//        }


        // Extract sameAs individuals
        String refalignPath = DATA_PATH + "data/001/refalign.rdf";
        OntologyReader refalignReader = new OntologyReader(refalignPath);
        Map<String, String> individualsPairs = refalignReader.getSameAsIndividuals();


        for (Object o : individualsPairs.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            String entity1 = (String) pair.getKey();
            String entity2 = (String) pair.getValue();
            System.out.println("===============");
            System.out.println(entity1);
            System.out.println(entity2);
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
