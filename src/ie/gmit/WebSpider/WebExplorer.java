package ie.gmit.WebSpider;

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

public class WebExplorer {
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

	private WebExplorer() {
		super();
	}

	public static WebExplorer getInstance(String[] keywords, double threshold,
			int depth, int breadth) {
		WebExplorer.executor = Executors.newFixedThreadPool(NTHREDS);
		WebExplorer.keywords = keywords;
		WebExplorer.threshold = threshold;
		WebExplorer.depth = depth;
		WebExplorer.breadth = breadth;
		return new WebExplorer();
	}

	public void search(WebNode node) {
		WebExplorer.highest = node;
		queue.add(node);
		while (!queue.isEmpty() || up) {
			for (int i = 0; i < 3; i++)
				;
			if (!up) {
				WebNode tmpNode = queue.poll();
				while (tmpNode.getScore() < 10
						&& tmpNode.getDepth() > WebExplorer.depth - 1
						&& !queue.isEmpty()) {
					tmpNode = queue.poll();
				}
				if (WebExplorer.highest.getScore() < tmpNode.getScore()) {
					WebExplorer.highest = tmpNode;
				}
				if (tmpNode.isGoalNode(WebExplorer.threshold)
						&& tmpNode.getScore() / 100 >= keywords.length) {
					System.out.println("Reached goal node: "
							+ tmpNode.getNodeURL());
					while (tmpNode.getParent() != null) {
						tmpNode = tmpNode.getParent();
						System.out.println("From: " + tmpNode.getNodeURL());
					}

					System.exit(0);
				} else {
					System.out.println("visiting - >" + tmpNode.getNodeURL());

					if (tmpNode.getDepth() < WebExplorer.depth) {
						Runnable worker = new ChildrenParser(tmpNode);
						up = true;
						WebExplorer.executor.execute(worker);
					} else if (queue.isEmpty())
						WebExplorer.executor.shutdown();
					// generate children

				}

			}

		}
		if (!WebExplorer.executor.isShutdown()) {
			WebExplorer.executor.shutdown();
		}
		if (queue.isEmpty() && !up) {
			if (WebExplorer.highest.isGoalNode(WebExplorer.threshold)) {
				System.out.println("Reached goal node "
						+ WebExplorer.highest.getNodeURL());
				while (WebExplorer.highest.getParent() != null) {
					WebExplorer.highest = WebExplorer.highest.getParent();
					System.out.println("From: "
							+ WebExplorer.highest.getNodeURL());
				}
			} else {
				System.out.println("No proper result");
				System.out.println("Most possible:"
						+ WebExplorer.highest.getNodeURL());
				while (WebExplorer.highest.getParent() != null) {
					WebExplorer.highest = WebExplorer.highest.getParent();
					System.out.println("From: "
							+ WebExplorer.highest.getNodeURL());
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
			for (int i = 0; i < WebExplorer.breadth && i < urlQue.size(); i++) {
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
			WebExplorer.up = false;

		}

	}

}
