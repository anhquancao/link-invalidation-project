package link.invalidation.utils;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import link.invalidation.models.PropertyWrapper;
import link.invalidation.models.Pair;
import org.apache.jena.rdf.model.*;

public class OntologyReader {
    private Model model;
    private Map<String, PropertyWrapper> properties = null;
    private Set<String> blackList = new HashSet<>();

    public OntologyReader(String path) throws FileNotFoundException {
        this.model = ModelFactory.createDefaultModel();
        this.model.read(path);
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/amount");
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/calling_code");
        blackList.add("http://oaei.ontologymatching.org/2010/IIMBTBOX/currency");
    }

    /**
     * Return a list of values wrt the subject and proeprty
     *
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
     *
     * @return
     */
    public List<Pair> getSameAsIndividuals() {
        String propEntity1 = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity1";
        String propEntity2 = "http://knowledgeweb.semanticweb.org/heterogeneity/alignment#entity2";

        String entity1 = null;
        String entity2 = null;

        StmtIterator itr = model.listStatements();
        List<Pair> pairs = new LinkedList<>();

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
                Pair pair = new Pair(entity1, entity2);
                pairs.add(pair);
                entity1 = null;
                entity2 = null;
            }

        }
        return pairs;
    }

    /**
     * Get the functional properties base on the provided threshold
     *
     * @param threshold
     * @return
     */
    public Map<String, PropertyWrapper> getFunctionalProperties(double threshold) {
        if (this.properties == null) {
            this.properties = this.getAllProperties();
        }
        return filterFunctionalProps(this.properties, threshold);
    }

    public Map<String, PropertyWrapper> getAllProperties() {
        StmtIterator itr = model.listStatements();
        Map<String, PropertyWrapper> m = new HashMap<>();
        while (itr.hasNext()) {
            Statement st = itr.nextStatement();

            String sub = st.getSubject().toString();
            String pred = st.getPredicate().toString();

            if (!blackList.contains(pred)) {
                PropertyWrapper prop = m.get(pred);
                if (prop == null) {
                    prop = new PropertyWrapper(pred);
                    m.put(pred, prop);
                }
                prop.addSubject(sub);
            }
        }

        return m;
    }

    private Map<String, PropertyWrapper> filterFunctionalProps(Map<String, PropertyWrapper> mp, double threshold) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            PropertyWrapper prop = (PropertyWrapper) pair.getValue();
            prop.computeFunctionalDegree();
        }

        Map<String, PropertyWrapper> maps = mp.entrySet()
                .stream()
                .filter(x -> x.getValue().getFunctionalDegree() > threshold)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return maps;
    }

}
