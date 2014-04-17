package ie.gmit.WebSpider;

import java.util.LinkedList;
import java.util.Queue;

public class WebParser {

	public static void main(String[] args) {
		if (args.length > 1) {
			String url = args[0];
			Queue<String> keywordsList = new LinkedList<String>();
			for (int i = 1; i < args.length; i++) {
				keywordsList.add(args[i].toLowerCase().trim());
			}
			if (!keywordsList.isEmpty()) {
				String[] keywords = (String[]) keywordsList.toArray();

				MarkLink ml = new MarkLink(keywords, url);
				WebNode wn = new WebNode(url);
				wn.setDepth(0);
				wn.setScore(ml.getScore());
				WebSpider ws = WebSpider.getInstance(keywords, 85, 5, 3);
				ws.search(wn);
			}
		} else {
			System.out.println("Invalid Input!");
		}

	}
}
