package ie.gmit.WebSpider;

import java.util.LinkedList;
import java.util.Queue;

public class WebParser {

	public static void main(String[] args) {
		if (args.length > 1) {
			String url = args[0];
			Queue<String> keywordsList = new LinkedList<String>();
			for (int i = 1; i < args.length; i++) {
				System.out.println(args[i]);
				keywordsList.add(args[i].toLowerCase().trim());
			}
			if (!keywordsList.isEmpty()) {
				String[] keywords = keywordsList
						.toArray(new String[keywordsList.size()]);
				MarkLink ml = new MarkLink(keywords, url);
				WebNode wn = new WebNode(url);
				wn.setDepth(0);
				wn.setScore(ml.getScore());
				WebSpider ws = WebSpider.getInstance(keywords, 85, 5, 3);
				ws.search(wn);
			}
		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("showfuz")) {
				MarkLink ml = new MarkLink(null, null);
				ml.showCretia();
			} else {
				System.out.println("Invalid Input!");
			}
		}

	}
}
