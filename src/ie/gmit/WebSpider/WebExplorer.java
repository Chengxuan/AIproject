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

/**
 * Web search class Best first search with depth and breadth limitation
 * 
 * @author Chengxuan Xing
 * 
 */
public class WebExplorer {
	private static final int NTHREDS = 50; // amount of threads in thread pool
	private static ExecutorService executor; // thread executor
	private static String[] keywords; // search keywords
	private static double threshold; // threshold expecting goal score
	private static int depth; // depth of searching
	private static int breadth; // breadth of searching
	// node need to be visited, Best first based on highest score
	private static Queue<WebNode> queue = new PriorityQueue<WebNode>();
	// history of visited URL, this map should be shared by threads
	private static volatile Map<String, WebNode> hisMap = new HashMap<String, WebNode>();
	// flag that mark there're node under processing to get children
	private static boolean up = false;
	private static WebNode highest; // the highest score node have been visited

	// private constructor
	private WebExplorer() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param keywords
	 *            --keywords used to search
	 * @param threshold
	 *            --expecting goal node score
	 * @param depth
	 *            --depth of searching
	 * @param breadth
	 *            --breadth of searching
	 * @return Instance of WebExplorer
	 */
	public static WebExplorer getInstance(String[] keywords, double threshold,
			int depth, int breadth) {
		// initialise executor
		WebExplorer.executor = Executors.newFixedThreadPool(NTHREDS);
		// set keywords and other factors
		WebExplorer.keywords = keywords;
		WebExplorer.threshold = threshold;
		WebExplorer.depth = depth;
		WebExplorer.breadth = breadth;
		// return a new instance of the class
		return new WebExplorer();
	}

	/**
	 * Best first search base on a start node, children will be added during
	 * searching
	 * 
	 * @param node
	 *            --start node
	 */
	public void search(WebNode node) {
		// use start node as initialization
		WebExplorer.highest = node;
		queue.add(node);
		// search deploy here
		// search should keep running when queue is not empty or
		// there's still child adding into the queue
		while (!queue.isEmpty() || up) {
			// this for loop here prevent system stop this thread
			for (int i = 0; i < 3; i++)
				;
			// if queue is not empty do search
			if (!queue.isEmpty()) {
				// get the best node
				WebNode tmpNode = queue.poll();
				// nodes have extreme low score and in the deepest layer should
				// be skipped
				while (tmpNode.getScore() < 10
						&& tmpNode.getDepth() > WebExplorer.depth - 1
						&& !queue.isEmpty()) {
					tmpNode = queue.poll();
				}
				// record the node if it has higher score
				if (WebExplorer.highest.getScore() < tmpNode.getScore()) {
					WebExplorer.highest = tmpNode;
				}
				// if node satisfy threshold AND all keywords in its URL which
				// means highly possible, return it as goal node
				if (tmpNode.isGoalNode(WebExplorer.threshold)
						&& tmpNode.getScore() / 100 >= keywords.length) {
					System.out.println("Reached goal node: "
							+ tmpNode.getNodeURL());
					// print out it's path
					while (tmpNode.getParent() != null) {
						tmpNode = tmpNode.getParent();
						System.out.println("From: " + tmpNode.getNodeURL());
					}
					// exit programme
					System.exit(0);
				} else {
					// deploy this node
					System.out.println("visiting - >" + tmpNode.getNodeURL());
					// if haven't reach the depth limitation analyse and add
					// children to it
					if (tmpNode.getDepth() < WebExplorer.depth) {
						// use a separate thread to analyse and add children
						// nodes
						Runnable worker = new ChildrenParser(tmpNode);
						// make flag is under processing
						up = true;
						WebExplorer.executor.execute(worker);
					} else if (queue.isEmpty())
						// when there's no more node need to analyse shut down
						// thread pool
						WebExplorer.executor.shutdown();
				}
			}
		}
		// double check to ensure thread pool has been shut down
		if (!WebExplorer.executor.isShutdown()) {
			WebExplorer.executor.shutdown();
		}
		// report result to user if no extreme like node have been found
		// if the highest score node is satisfy the threshold but without all
		// keywords in its URL, report it as compromised result
		if (WebExplorer.highest.isGoalNode(WebExplorer.threshold)) {
			System.out.println("Reached goal node "
					+ WebExplorer.highest.getNodeURL());
			while (WebExplorer.highest.getParent() != null) {
				WebExplorer.highest = WebExplorer.highest.getParent();
				System.out.println("From: " + WebExplorer.highest.getNodeURL());
			}
		} else {
			// no result found, report the highest score node as a possibility
			System.out.println("No proper result");
			System.out.println("Most possible:"
					+ WebExplorer.highest.getNodeURL());
			while (WebExplorer.highest.getParent() != null) {
				WebExplorer.highest = WebExplorer.highest.getParent();
				System.out.println("From: " + WebExplorer.highest.getNodeURL());
			}
		}
	}

	class ChildrenParser implements Runnable {
		private WebNode wbn; // web node need to be processed

		/**
		 * Constructor
		 * 
		 * @param wbn
		 *            --Web Node need to be parsed
		 */
		public ChildrenParser(WebNode wbn) {
			this.wbn = wbn;
		}

		@Override
		public void run() {
			// Queue used to select better node based on keywords frequency in
			// URL
			PriorityQueue<WebNode> urlQue = new PriorityQueue<WebNode>();
			try {
				Document doc = Jsoup.connect(wbn.getNodeURL()).get();
				// get all child links
				Elements links = doc.getElementsByTag("a");
				for (Element link : links) {
					// get absolute address
					String linkHref = link.attr("abs:href").trim();
					// validate links and ensure it's not repeat
					if (!linkHref.isEmpty() && !hisMap.containsKey(linkHref)
							&& link.hasText() && linkHref.contains("http")) {
						hisMap.put(linkHref, null);
						WebNode wn = new WebNode(linkHref);
						// score URL
						wn.scoreURL(keywords);
						wn.setParent(wbn);
						wn.setDepth(wbn.getDepth() + 1);
						urlQue.add(wn);
					}
				}

			} catch (Exception e) {
				/*
				 * If node can't be fetch skip it and report reason
				 */
				System.out.println("Can't get children of "
						+ this.wbn.getNodeURL());
				System.out.println(e.getStackTrace());
			}
			// get better children base on queue and breadth
			for (int i = 0; i < WebExplorer.breadth && i < urlQue.size(); i++) {
				if (!urlQue.isEmpty()) {
					WebNode wn = urlQue.poll();
					MarkLink ml = new MarkLink(keywords, wn.getNodeURL());
					wn.setScore(ml.getScore());
					wbn.addChildNode(wn);
					queue.add(wn);
				}
			}
			// mark children processing have finish
			WebExplorer.up = false;

		}

	}

}
