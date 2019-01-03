package tm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class OntologyReader {
    private Model model;
    private Map<String, Property> properties = null;

    public OntologyReader(String path) throws FileNotFoundException {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(path);
    }

    public Map<String, String> getSameAsIndividuals() {
        String propEntity1 = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1";
        String propEntity2 = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2";

        String entity1 = null;
        String entity2 = null;

        StmtIterator itr = model.listStatements();
        Map<String, String> m = new HashMap<>();
        while (itr.hasNext()) {
            Statement st = itr.nextStatement();

            String sub = st.getSubject().toString();
            String pred = st.getPredicate().toString();
            String obj = st.getObject().toString();

            if (pred.equalsIgnoreCase(propEntity1)) {
                entity1 = obj;
            }
            if (pred.equalsIgnoreCase(propEntity2)) {
                entity2 = obj;
            }

            if (entity1 != null && entity2 != null) {
                m.put(entity1, entity2);
                entity1 = null;
                entity2 = null;
            }

        }
        return m;
    }

    public Map<String, Property> getFunctionalProperties(double threshold) {
        if (this.properties == null) {
            this.properties = this.getAllProperties();
        }
        return filterFunctionalProps(this.properties, threshold);
    }

    public Map<String, Property> getAllProperties() {
        StmtIterator itr = model.listStatements();
        Map<String, Property> m = new HashMap<>();
        while (itr.hasNext()) {
            Statement st = itr.nextStatement();

            String sub = st.getSubject().toString();
            String pred = st.getPredicate().toString();

            Property prop = m.get(pred);
            if (prop == null) {
                prop = new Property(pred);
                m.put(pred, prop);
            }
            prop.addSubject(sub);
        }

        return m;
    }

    private Map<String, Property> filterFunctionalProps(Map<String, Property> mp, double threshold) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Property prop = (Property) pair.getValue();
            prop.computeFunctionalDegree();
        }

        Map<String, Property> maps = mp.entrySet()
                .stream()
                .filter(x -> x.getValue().getFunctionalDegree() > threshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return maps;
    }

}
