package tm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class PropertyMapping {
    private Model model;
    private Map<String, Property> maps;

    public PropertyMapping(Model m) {
        this.model = m;
        this.maps = this.extractProp();
        computeFunctionalDegree(maps);
    }

    private Map<String, Property> extractProp() {
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

    private void computeFunctionalDegree(Map<String, Property> mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property) pair.getValue();
            prop.computeFunctionalDegree();
        }
    }

    public Map<String, Property> getMaps() {
        return maps;
    }
}
