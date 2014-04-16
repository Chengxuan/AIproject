package ie.gmit.WebSpider;

import ie.gmit.Fuzzy.MarkLink;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebSpider {
	private static final int NTHREDS = 100;
	private static ExecutorService executor;
	private static String[] keywords;
	private static double threshold;
	private static int depth;
	private static int breadth;
	private static Queue<WebNode> queue = new PriorityQueue<WebNode>();
	private static volatile Map<String, WebNode> hisMap = new HashMap<String, WebNode>();
	private static boolean up = false;
	private static WebNode highest;

	private WebSpider() {
		super();
	}

	public static WebSpider getInstance(String[] keywords, double threshold,
			int depth, int breadth) {
		WebSpider.executor = Executors.newFixedThreadPool(NTHREDS);
		WebSpider.keywords = keywords;
		WebSpider.threshold = threshold;
		WebSpider.depth = depth;
		WebSpider.breadth = breadth;
		return new WebSpider();
	}

	public void search(WebNode node) {
		WebSpider.highest = node;
		queue.add(node);
		while (!queue.isEmpty() || up) {
			if (!up) {
				WebNode tmpNode = queue.poll();
				if (WebSpider.highest.getScore() < tmpNode.getScore()) {
					WebSpider.highest = tmpNode;
				}
				if (tmpNode.isGoalNode(WebSpider.threshold)
						&& tmpNode.getScore() / 100 >= keywords.length) {
					System.out.println("Reached goal node "
							+ tmpNode.getNodeURL());
					while (tmpNode.getParent() != null) {
						tmpNode = tmpNode.getParent();
						System.out.println(tmpNode.getNodeURL());
					}

					System.exit(0);
				} else {

					System.out.println("visiting - >" + tmpNode.getNodeURL()
							+ tmpNode.getDepth() + "--------"
							+ tmpNode.getScore());

					if (tmpNode.getDepth() < WebSpider.depth) {
						Runnable worker = new ChildrenParser(tmpNode);
						up = true;
						WebSpider.executor.execute(worker);
					} else if (queue.isEmpty())
						WebSpider.executor.shutdown();

					// generate children

				}

			}

		}
		if (queue.isEmpty() && !up) {
			if (WebSpider.highest.isGoalNode(WebSpider.threshold)) {
				System.out.println("Reached goal node "
						+ WebSpider.highest.getNodeURL());
				while (WebSpider.highest.getParent() != null) {
					WebSpider.highest = WebSpider.highest.getParent();
					System.out.println(WebSpider.highest.getNodeURL());
				}
			} else {
				System.out.println("No proper result");
				System.out.println("Most possible:"
						+ WebSpider.highest.getNodeURL());
				while (WebSpider.highest.getParent() != null) {
					WebSpider.highest = WebSpider.highest.getParent();
					System.out.println(WebSpider.highest.getNodeURL());
				}
			}

		}

	}

	class ChildrenParser implements Runnable {

		private WebNode wbn;

		public ChildrenParser(WebNode wbn) {
			this.wbn = wbn;
		}

		@Override
		public void run() {
			PriorityQueue<WebNode> urlQue = new PriorityQueue<WebNode>();
			try {
				Document doc = Jsoup.connect(wbn.getNodeURL()).get();
				// System.out.println(doc.getElementsByTag("title"));
				// System.out.println(doc.getElementsByTag("h2").get(2).text());
				Elements links = doc.getElementsByTag("a");

				for (Element link : links) {
					String linkHref = link.attr("abs:href").trim();
					if (!linkHref.isEmpty() && !hisMap.containsKey(linkHref)
							&& link.hasText() && linkHref.contains("http")) {

						hisMap.put(linkHref, null);
						WebNode wn = new WebNode(linkHref);
						wn.scoreURL(keywords);
						wn.setParent(wbn);
						wn.setDepth(wbn.getDepth() + 1);
						urlQue.add(wn);
					}
				}

			} catch (Exception e) {
				System.out.println("URL not valid:" + e.getMessage());
			}
			for (int i = 0; i < WebSpider.breadth; i++) {
				// for (int i = children.length - 1; i >= 0; i--) {
				// System.out.println(node.getNextChildren());
				if (!urlQue.isEmpty()) {
					WebNode wn = urlQue.poll();
					MarkLink ml = new MarkLink(keywords, wn.getNodeURL());
					// System.out.println(wn.getNodeURL() + wn.getScore());
					// System.out.println(ml.getScore());
					wn.setScore(ml.getScore());
					wbn.addChildNode(wn);
					queue.add(wn);
				}
			}
			WebSpider.up = false;

		}

	}

}
