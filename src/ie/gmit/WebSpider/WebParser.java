package ie.gmit.WebSpider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebParser {
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.connect("http://jsoup.org/cookbook/extracting-data/working-with-urls").get();
			System.out.println(doc.getElementsByTag("title"));
			System.out.println(doc.getElementsByTag("h2").get(2).text());
			Elements links = doc.getElementsByTag("a");
			for (Element link : links) {
				String linkHref = link.attr("abs:href");
			//	System.out.println(linkHref);
			}
		} catch (Exception e) {
			System.out.println("URL not valid:" + e.getMessage());
		}
	}
}
