import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JsoupTest {
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://juejin.cn/post/7306832876381437991").get();

        String title = doc.title();
        System.out.println("********************" + title);
        Element article = doc.getElementById("article-root");
        MutableDataSet options = new MutableDataSet();
        String markdown = FlexmarkHtmlConverter.builder(options).build().convert(article);
        System.out.println("********************" + markdown);
        Elements tags = doc.getElementsByAttributeValue("itemprop", "keywords");
        tags.forEach(item -> {
            System.out.println("XXXXXXXXXXX" + item.attr("content"));
        });

    }
}
