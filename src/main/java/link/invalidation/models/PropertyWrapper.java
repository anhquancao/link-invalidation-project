package link.invalidation.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyWrapper {
    /**
     * is functional degree
     */
    private float functionalDegree;

    private String name;

    private Map<String, Integer> subjectCount;

    public PropertyWrapper(String name) {
        this.name = name;
        subjectCount = new HashMap<String, Integer>();
        functionalDegree = 0;
    }

    public void computeFunctionalDegree() {
        Iterator it = subjectCount.entrySet().iterator();
        float totalSubjects = 0;
        float uniqueSubjects = 0;
        while (it.hasNext()) {
            totalSubjects += 1;
            Map.Entry pair = (Map.Entry) it.next();
            int value = (int) pair.getValue();
            if (value == 1) {
                uniqueSubjects += 1;
            }
        }
        functionalDegree = uniqueSubjects / totalSubjects;
    }

    public void addSubject(String subject) {
        Integer count = subjectCount.getOrDefault(subject, 0);
        subjectCount.put(subject, count + 1);
    }

    public Map<String, Integer> getSubjectCount() {
        return subjectCount;
    }

    public PropertyWrapper(float functionalDegree) {
        this.functionalDegree = functionalDegree;
    }

    public float getFunctionalDegree() {
        return functionalDegree;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "MyProperty{" +
                "functionalDegree=" + functionalDegree +
                ", name='" + name + '\'' +
                ", subjectCount=" + subjectCount +
                '}';
    }
}
