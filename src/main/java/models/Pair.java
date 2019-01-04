package models;

public class Pair {
    private String entity1;
    private String entity2;

    public Pair(String left, String right) {
        this.entity1 = left;
        this.entity2 = right;
    }

    public String getEntity1() {
        return entity1;
    }

    public void setEntity1(String entity1) {
        this.entity1 = entity1;
    }

    public String getEntity2() {
        return entity2;
    }

    public void setEntity2(String entity2) {
        this.entity2 = entity2;
    }
}
