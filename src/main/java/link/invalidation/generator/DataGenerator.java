package link.invalidation.generator;

import link.invalidation.models.Pair;
import link.invalidation.utils.Constant;
import link.invalidation.utils.OntologyReader;

import java.io.FileNotFoundException;
import java.util.*;


public class DataGenerator {
    public List<Pair> loadCorrectPairs() {
        // Extract sameAs individuals
        String refalignPath = Constant.REF_PATH + "refalign.rdf";
        OntologyReader refalignReader = null;
        try {
            refalignReader = new OntologyReader(refalignPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<Pair> individualsPairs = refalignReader.getSameAsIndividuals();
        return individualsPairs;
    }

    public List<Pair> generateIncorrectPairs(List<Pair> correctPairs, int numberInvalidLinks) {
        Random random = new Random();

        List<String> l1 = new LinkedList<>();
        List<String> l2 = new LinkedList<>();

        Map<String, String> map = new HashMap<>();

        for (Pair pair : correctPairs) {
            String entity1 = pair.getEntity1();
            String entity2 = pair.getEntity2();

            l1.add(entity1);
            l2.add(entity2);

            map.put(entity1, entity2);
        }

        for (int i = 0; i < numberInvalidLinks; i++) {
            int idx1 = random.nextInt(l1.size());
            String entity1 = l1.get(idx1);

            int idx2 = random.nextInt(l2.size());
            String entity2 = l2.get(idx2);

            Pair incorrectPair = new Pair(entity1, entity2);
            incorrectPair.setValid(false);

            correctPairs.add(incorrectPair);
        }


        return correctPairs;
    }
}