import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GoogleSearch {
    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";

    public List<String> run(String inputTerm, int inputNum) throws IOException {
        String querySearch = inputTerm.replaceAll("\\s", "+");
        String url = GOOGLE_SEARCH_URL + "search?q=" + querySearch;
        Document mainPage = Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .get();

        Element picBlock = mainPage.getElementsByClass("q qs").first();
        String picPageUrl = GOOGLE_SEARCH_URL + picBlock.attr("href");

        Document picPage = Jsoup.connect(picPageUrl)
                .userAgent(USER_AGENT)
                .get();

        Elements pictures = picPage.getElementsByClass("rg_meta notranslate");

        List<String> hrefs = new ArrayList<>();

        for(int i = 0; i<inputNum; i++){
            JSONObject jsonObject = new JSONObject(pictures.get(i).ownText());
            hrefs.add(jsonObject.getString("ou"));
        }

        return hrefs;
    }
}
