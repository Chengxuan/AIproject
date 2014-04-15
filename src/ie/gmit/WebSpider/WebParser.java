package ie.gmit.WebSpider;

import ie.gmit.Fuzzy.MarkLink;
import ie.gmit.Fuzzy.MarkSchema;
import ie.gmit.WebNode.WebNode;

public class WebParser {
	public static void main(String[] args) {
		MarkLink ml = MarkLink.getInstance();
		MarkSchema ms = ml
				.generateMarkSchema("https://www.jcp.org/ja/introduction/overview");
		WebNode wn = new WebNode("https://www.jcp.org/ja/introduction/overview");
		wn.setScore(ml.getScore(ms));
		WebSpider ws = WebSpider.getInstance(80, 3, 5);
		ws.search(wn);
	}
}
