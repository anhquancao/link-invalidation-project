import com.wcohen.ss.JaroWinkler;
import models.Pair;
import org.apache.jena.rdf.model.RDFNode;
import tm.Standardizer;
import tm.MyProperty;
import tm.OntologyReader;

import java.io.FileNotFoundException;

import java.util.List;
import java.util.Map;

public class Detection {

    private String[] dateFormats = {"M/d/yy", "MMM d, yyyy"};

    private static final String SRC_PATH = "/home/quan/dataset/data/000/onto.owl";
    private static final String REF_PATH = "/home/quan/dataset/data/002/";

    private static final double THRESHOLD = 0.9;

    private void run() throws FileNotFoundException {

        /**
         * use dateFormats[0] for 001
         * use dateFormats[1] for 002
         */
        Standardizer standardier = new Standardizer(dateFormats[1]);


        OntologyReader p0 = new OntologyReader(SRC_PATH);
        Map<String, MyProperty> functionProperties = p0.getFunctionalProperties(THRESHOLD);

        String path1 = REF_PATH + "onto.owl";
        OntologyReader p1 = new OntologyReader(path1);

//        System.out.println("Extracted " + functionProperties.size() + " function properties");
//
//        for (Object o : functionProperties.entrySet()) {
//            Map.Entry pair = (Map.Entry) o;
//            MyProperty prop = (MyProperty) pair.getValue();
//            System.out.println(prop);
//        }


        // Extract sameAs individuals
        String refalignPath = REF_PATH + "refalign.rdf";
        OntologyReader refalignReader = new OntologyReader(refalignPath);
        List<Pair> individualsPairs = refalignReader.getSameAsIndividuals();


        for (Pair pair : individualsPairs) {
            String entity1 = pair.getEntity1();
            String entity2 = pair.getEntity2();
//            System.out.println("===============");
//            System.out.println("entity1: " + entity1);
//            System.out.println("entity2: " + entity2);

            for (Object o1 : functionProperties.entrySet()) {
                Map.Entry pair1 = (Map.Entry) o1;
                String prop = (String) pair1.getKey();

                List<RDFNode> l0 = p0.getPropertyValue(entity1, prop);
                List<RDFNode> l1 = p1.getPropertyValue(entity2, prop);


                // If there are more than 1 value, we cannot decide
                // open world assumption
                if (l0.size() == 1 && l1.size() == 1) {
                    RDFNode node0 = l0.get(0);
                    RDFNode node1 = l1.get(0);

//                    System.out.println("Property: " + prop);


                    String val0 = standardier.standardize(node0.toString(), prop);
                    String val1 = standardier.standardize(node1.toString(), prop);

                    if (prop.equalsIgnoreCase("http://oaei.ontologymatching.org/2010/IIMBTBOX/name")) {
                        int a = 2;
                    }

                    boolean flag = true;
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