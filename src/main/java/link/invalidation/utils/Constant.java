package link.invalidation.utils;

public class Constant {
    /**
     * 001 => ontologyNumber = 1
     * 002 => ontologyNumber = 2
     */
    public static final int ontologyNumber = 2;

    public static final String SRC_PATH = "/home/quan/dataset/data/000/onto.owl";

    public static final String REF_PATH = "/home/quan/dataset/data/00" + ontologyNumber + "/";

    public static final double THRESHOLD = 0.9;

    public static final double SIM_THRESHOLD = 0.7;

    public static final String[] DATE_FORMATS = new String[]{"M/d/yy", "MMM d, yyyy"};

}
