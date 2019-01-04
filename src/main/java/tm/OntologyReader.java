package tm;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import javax.swing.plaf.nimbus.State;

public class OntologyReader {
    private Model model;
    private Map<String, MyProperty> properties = null;

    public OntologyReader(String path) throws FileNotFoundException {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(path);
    }

    /**
     * Return a list of values wrt the subject and proeprty
     * @param subject
     * @param property
     * @return
     */
    public List<RDFNode> getPropertyValue(String subject, String property) {
        Resource resource = this.model.getResource(subject);
        Property prop = this.model.getProperty(property);
        Selector selector = new SimpleSelector(resource, prop, (Object) null);
        StmtIterator iter = this.model.listStatements(selector);

        List<RDFNode> lists = new LinkedList<>();

        while (iter.hasNext()) {
            Statement r = iter.nextStatement();
            RDFNode obj = r.getObject();
            lists.add(obj);
        }

        return lists;

    }

    /**
     * Extract the sameAs objects
     * @return
     */
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

    /**
     * Get the functional properties base on the provided threshold
     * @param threshold
     * @return
     */
    public Map<String, MyProperty> getFunctionalProperties(double threshold) {
        if (this.properties == null) {
            this.properties = this.getAllProperties();
        }
        return filterFunctionalProps(this.properties, threshold);
    }

    public Map<String, MyProperty> getAllProperties() {
        StmtIterator itr = model.listStatements();
        Map<String, MyProperty> m = new HashMap<>();
        while (itr.hasNext()) {
            Statement st = itr.nextStatement();

            String sub = st.getSubject().toString();
            String pred = st.getPredicate().toString();

            MyProperty prop = m.get(pred);
            if (prop == null) {
                prop = new MyProperty(pred);
                m.put(pred, prop);
            }
            prop.addSubject(sub);
        }

        return m;
    }

    private Map<String, MyProperty> filterFunctionalProps(Map<String, MyProperty> mp, double threshold) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            MyProperty prop = (MyProperty) pair.getValue();
            prop.computeFunctionalDegree();
        }

        Map<String, MyProperty> maps = mp.entrySet()
                .stream()
                .filter(x -> x.getValue().getFunctionalDegree() > threshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return maps;
    }

}
