package tm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Standardier {

    private static final String GENDER_PROP = "http://oaei.ontologymatching.org/2010/IIMBTBOX/gender";
    private static final String DOB_PROP = "http://oaei.ontologymatching.org/2010/IIMBTBOX/date_of_birth";

    public String standardize(String s, String prop) {
        if (prop.equalsIgnoreCase(GENDER_PROP)) {
            if (s.equalsIgnoreCase("M") || s.equalsIgnoreCase("Male")) {
                return "Male";
            }
            if (s.equalsIgnoreCase("F") || s.equalsIgnoreCase("Female")) {
                return "Female";
            }
        }

        if (prop.equalsIgnoreCase(DOB_PROP)) {
            DateFormat resFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (s.contains("/")) {
                DateFormat df = new SimpleDateFormat("M/d/yy");
                Date result = null;
                try {
                    result = df.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return resFormat.format(result);
            }

            return s;
        }

        return s;
    }

}
