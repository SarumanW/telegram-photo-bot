import java.util.ArrayList;
import java.util.List;

public class QueryParser {
    public static String[] parseQuery(String query){
        String[] result = new String[2];
        int lastSpace = query.lastIndexOf(' '); // check another time
        result[0] = query.substring(0, lastSpace);
        result[1] = query.substring(lastSpace);
        return result;
    }
}
