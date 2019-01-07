package link.invalidation.encoder;

import com.wcohen.ss.JaroWinkler;
import link.invalidation.models.Pair;
import link.invalidation.models.PropertyWrapper;
import link.invalidation.utils.OntologyReader;
import link.invalidation.utils.Standardizer;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CSVEncoder {

    public List<Row> encode(
            List<Pair> individualsPairs,
            Map<String, PropertyWrapper> properties,
            OntologyReader p0,
            OntologyReader p1,
            boolean debug
    ) throws FileNotFoundException {

        List<Row> data = new LinkedList<>();

        Standardizer standardier = new Standardizer();

        for (Pair pair : individualsPairs) {
            String entity1 = pair.getEntity1();
            String entity2 = pair.getEntity2();

            Row row = new Row();
            row.setTarget(pair.isValid() ? 1 : 0);

            for (Object o1 : properties.entrySet()) {
                Map.Entry pair1 = (Map.Entry) o1;
                String prop = (String) pair1.getKey();

                List<RDFNode> l0 = p0.getPropertyValue(entity1, prop);
                List<RDFNode> l1 = p1.getPropertyValue(entity2, prop);

                double similarity = 0;

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


                        if (literal0.getValue() instanceof String) {
                            // If the value is String, we use JaroWinkler
                            JaroWinkler jaroWinkler = new JaroWinkler();
                            String val0 = standardier.standardize(literal0.getString(), prop);
                            String val1 = standardier.standardize(literal1.getString(), prop);

                            similarity = jaroWinkler.score(val0, val1);

                        } else {
                            double val0 = literal0.getDouble();
                            double val1 = literal1.getDouble();

                            similarity = val0 == val1 ? 1 : 0;

                        }


                    }
                }
                row.add(similarity);
            }

            data.add(row);
        }

        return data;
    }

}
