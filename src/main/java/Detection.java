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

    private static final String DATA_PATH = "/home/quan/dataset/";

    private static final double THRESHOLD = 0.9;

    private void run() throws FileNotFoundException {
        String path0 = DATA_PATH + "data/000/onto.owl";
        Model m0 = ModelFactory.createDefaultModel();
        m0.read(new FileInputStream(path0), "");
        PropertyMapping p0 = new PropertyMapping(m0, THRESHOLD);
        Map<String, Property> map0 = p0.getMaps();


        System.out.println("Extracted " + map0.size() + " properties");

        for (Object o : map0.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
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
            detection.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
