import com.wcohen.ss.JaroWinkler;
import models.Pair;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import tm.Standardizer;
import tm.MyProperty;
import tm.OntologyReader;

import java.io.FileNotFoundException;

import java.util.List;
import java.util.Map;

public class LogicalDetection {

    private String[] dateFormats = {"M/d/yy", "MMM d, yyyy"};

    private static final int ontologyNumber = 2;

    private static final String SRC_PATH = "/home/quan/dataset/data/000/onto.owl";
    private static final String REF_PATH = "/home/quan/dataset/data/00" + ontologyNumber + "/";

    private static final double THRESHOLD = 0.9;

    private static final double SIM_THRESHOLD = 0.7;

    private void run() throws FileNotFoundException {

        /**
         * use dateFormats[0] for 001
         * use dateFormats[1] for 002
         */
        Standardizer standardier = new Standardizer(dateFormats[ontologyNumber - 1]);


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


                    // We cannot decide if the value is not literal
                    if (node0.isLiteral() && node1.isLiteral()) {

                        Literal literal0 = node0.asLiteral();
                        Literal literal1 = node1.asLiteral();

                        double similarity;

                        if (literal0.getValue() instanceof String) {
                            JaroWinkler jaroWinkler = new JaroWinkler();
                            String val0 = standardier.standardize(literal0.getString(), prop);
                            String val1 = standardier.standardize(literal1.getString(), prop);

                            similarity = jaroWinkler.score(val0, val1);

                            if (similarity < SIM_THRESHOLD) {
                                System.out.println("Property of entity 0: " + val0);
                                System.out.println("Property of entity 1: " + val1);
                            }
                        } else {
                            double val0 = literal0.getDouble();
                            double val1 = literal1.getDouble();

                            similarity = val0 == val1 ? 1 : 0;

                            if (similarity < SIM_THRESHOLD) {
                                System.out.println("Property of entity 0: " + val0);
                                System.out.println("Property of entity 1: " + val1);
                            }

                        }


                        if (similarity < SIM_THRESHOLD) {

                            System.out.println("===============");
                            System.out.println("Invalid sameAs: ");
                            System.out.println("entity1: " + entity1);
                            System.out.println("entity2: " + entity2);
                            System.out.println("Property: " + prop);

                            System.out.println("Similarity: " + similarity);

                        }
                    }
                }

            }

        }
    }


    public static void main(String[] args) {
        LogicalDetection detection = new LogicalDetection();
        try {
            detection.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}