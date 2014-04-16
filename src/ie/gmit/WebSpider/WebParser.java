package ie.gmit.WebSpider;

import ie.gmit.Fuzzy.MarkLink;

public class WebParser {

	public static void main(String[] args) {
		if (/* args.length > 1 */1 == 1) {
			String[] keywords = { "Python", "Hollywood", "1982" };

			MarkLink ml = new MarkLink(keywords,
					"https://en.wikipedia.org/wiki/Main_Page");
			WebNode wn = new WebNode("https://en.wikipedia.org/wiki/Main_Page");
			wn.setDepth(0);
			wn.setScore(ml.getScore());
			WebSpider ws = WebSpider.getInstance(keywords, 85, 4, 6);
			ws.search(wn);
		} else {
			System.out.println("Invalid Input!");
		}

	}
}
