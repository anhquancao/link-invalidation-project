import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import tm.Property;
import tm.PropertyMapping;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Detection {

    public static final String dataPath = "/home/quan/dataset/";

    private void run(String dataPath) throws FileNotFoundException {
        String path0 = dataPath + "data/000/onto.owl";
        Model m0 = ModelFactory.createDefaultModel();
        m0.read(new FileInputStream(path0), "");
        PropertyMapping p0 = new PropertyMapping(m0);
        Map<String, Property> map0 = p0.getMaps();

        Iterator it = map0.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Property prop = (Property) pair.getValue();
            System.out.println(prop);
        }

//        String path1 = dataPath + "data/001/onto.owl";
//        Model m1 = ModelFactory.createDefaultModel();
//        m1.read(new FileInputStream(path1), "");
//        PropertyMapping p1 = new PropertyMapping(m1);
//        Map<String, Property> map1 = p1.getMaps();


        // TODO Iterate two maps, get similarity
        // TODO Get links
    }

    public static void main(String[] args) {
        Detection detection = new Detection();
        try {
            detection.run(dataPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
