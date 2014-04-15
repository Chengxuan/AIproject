package ie.gmit.WebSpider;

import ie.gmit.Fuzzy.MarkLink;
import ie.gmit.Fuzzy.MarkSchema;
import ie.gmit.WebNode.WebNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebSpider {
	private static double threshold;
	private static int depth;
	private static int breadth;
	private static Queue<WebNode> queue = new LinkedList<WebNode>();
	private static Map<String, WebNode> hisMap = new HashMap<String, WebNode>();

	private WebSpider() {
		super();
	}

	public static WebSpider getInstance(double threshold, int depth, int breadth) {
		WebSpider.threshold = threshold;
		WebSpider.depth = depth;
		WebSpider.breadth = breadth;
		return new WebSpider();
	}

	public void search(WebNode node) {
		queue.add(node);
		while (!queue.isEmpty()) {
			WebNode tmpNode = queue.poll();
			while ((hisMap.containsKey(tmpNode.getNodeURL().trim()) || tmpNode == null)
					&& !queue.isEmpty()) {
				tmpNode = queue.poll();
			}
			if (tmpNode.isGoalNode(WebSpider.threshold)
					|| tmpNode.getNodeURL().equalsIgnoreCase(
							"http://www.youtube.com/oracle/")) {
				System.out.println("Reached goal node " + tmpNode.getNodeURL());
				while (tmpNode.getParent() != null) {
					tmpNode = tmpNode.getParent();
					System.out.println(tmpNode.getNodeURL());
				}

				System.exit(0);
			} else {

				if (!hisMap.containsKey(tmpNode.getNodeURL().trim())) {
					System.out.println("visiting" + tmpNode.getNodeURL());
					hisMap.put(tmpNode.getNodeURL().toString(), tmpNode);
					this.getChildren(tmpNode);
				}
				// generate children

			}
		}
		if (!queue.isEmpty()) {
			node = queue.poll();
		} else {
			System.out.println("No result");
		}
	}

	public void getChildren(WebNode node) {
		System.out.println("here");
		try {
			Document doc = Jsoup.connect(node.getNodeURL()).get();
			// System.out.println(doc.getElementsByTag("title"));
			// System.out.println(doc.getElementsByTag("h2").get(2).text());
			Elements links = doc.getElementsByTag("a");
			for (Element link : links) {

				String linkHref = link.attr("abs:href").trim();
				if (!linkHref.isEmpty() && !hisMap.containsKey(linkHref)) {
					MarkLink ml = MarkLink.getInstance();
					MarkSchema ms = ml.generateMarkSchema(linkHref);
					WebNode wn = new WebNode(linkHref);
					wn.setScore(ml.getScore(ms));
					wn.setParent(node);
					node.addChildNode(wn);
				}
			}
		} catch (Exception e) {
			System.out.println("URL not valid:" + e.getMessage());
		}
		for (int i = 0; i < WebSpider.breadth; i++) {
			// for (int i = children.length - 1; i >= 0; i--) {
			// System.out.println(node.getNextChildren());
			if (node.getNextChildren() != null)
				queue.add(node.getNextChildren());
		}
	}

}
