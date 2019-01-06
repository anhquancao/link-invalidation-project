package tm;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Standardizer {


    private static final String GENDER_PROP = "http://oaei.ontologymatching.org/2010/IIMBTBOX/gender";
    private static final String DOB_PROP = "http://oaei.ontologymatching.org/2010/IIMBTBOX/date_of_birth";
    private static final String NAME_PROP = "http://oaei.ontologymatching.org/2010/IIMBTBOX/name";
    private String refDateFormat;

    public Standardizer(String refDateFormat) {
        this.refDateFormat = refDateFormat;
    }

    public String standardize(String s, String prop) {

        if (prop.equalsIgnoreCase(NAME_PROP)) {
            String filteredS = s.replace(".", " ");
            String[] arr = filteredS.split("\\s+");
            StringBuilder res = new StringBuilder();

            for (int i = 0; i < arr.length - 1; i++) {
                res.append(arr[i], 0, 1);
            }
            res.append(arr[arr.length - 1]);
            return res.toString();

        }

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
            String pattern = "\\d{4}-\\d{2}-\\d{2}";

            // Create a Pattern object
            Pattern r = Pattern.compile(pattern);

            // Now create matcher object.
            Matcher m = r.matcher(s);

            if (m.find()) {
                return s;
            }

            DateFormat df = new SimpleDateFormat(this.refDateFormat);
            Date result = null;
            try {
                result = df.parse(s);
            } catch (ParseException e) {
//                e.printStackTrace();
                return s;
            }
            return resFormat.format(result);


        }

        return s;
    }

}
