import com.wcohen.ss.JaroWinkler;
import org.apache.jena.rdf.model.RDFNode;
import tm.Standardier;
import tm.MyProperty;
import tm.OntologyReader;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Detection {

    private static final String DATA_PATH = "/home/quan/dataset/";

    private static final double THRESHOLD = 0.9;

    private void run() throws FileNotFoundException {

        Standardier standardier = new Standardier();

        String path0 = DATA_PATH + "data/000/onto.owl";
        OntologyReader p0 = new OntologyReader(path0);
        Map<String, MyProperty> functionProperties = p0.getFunctionalProperties(THRESHOLD);

        String path1 = DATA_PATH + "data/001/onto.owl";
        OntologyReader p1 = new OntologyReader(path1);

//        System.out.println("Extracted " + functionProperties.size() + " function properties");
//
//        for (Object o : functionProperties.entrySet()) {
//            Map.Entry pair = (Map.Entry) o;
//            MyProperty prop = (MyProperty) pair.getValue();
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
//            System.out.println("===============");
//            System.out.println("entity1: " + entity1);
//            System.out.println("entity2: " + entity2);

            for (Object o1 : functionProperties.entrySet()) {
                Map.Entry pair1 = (Map.Entry) o1;
                String prop = (String) pair1.getKey();

                List<RDFNode> l0 = p0.getPropertyValue(entity1, prop);
                List<RDFNode> l1 = p1.getPropertyValue(entity2, prop);

                // If there are more than 1 value, we cannot decide
                if (l0.size() == 1 || l1.size() == 1) {
                    RDFNode node0 = l0.get(0);
                    RDFNode node1 = l1.get(0);


                    String val0 = standardier.standardize(node0.toString(), prop);
                    String val1 = standardier.standardize(node1.toString(), prop);

                    // We cannot decide if the value is not literal
                    if (node0.isLiteral() && node1.isLiteral()) {

                        JaroWinkler jaroWinkler = new JaroWinkler();
                        double similarity = jaroWinkler.score(val0, val1);


                        if (similarity < 0.7) {
                            System.out.println("===============");
                            System.out.println("Invalid sameAs: ");
                            System.out.println("entity1: " + entity1);
                            System.out.println("entity2: " + entity2);
                            System.out.println("Property: " + prop);
                            System.out.println("Property of entity 0: " + val0);
                            System.out.println("Property of entity 1: " + val1);
                            System.out.println("Similarity: " + similarity);
                        }
                    }
                }

            }

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
