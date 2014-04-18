package ie.gmit.WebSpider;

/**
 * Main class to run the webspider
 * 
 * @author Chengxuan Xing
 * 
 */
public class WebSpider {
	public static void main(String[] args) {
		// error message when use input invalid arguments
		String notice = "Invalid Arguments:\n\t-t   change threshold of expecting goal Node"
				+ "\n\t-d   change depth of searching"
				+ "\n\t-b   change breadth of expecting goal Node"
				+ "\nSettings should annouced before URL otherwise it will be regard as keywords!"
				+ "\nYou can use 'SFC' to see the fuzzy chart";
		double threshold = 80; // default threshold
		int depth = 4; // default search depth
		int breadth = 2; // default search breadth
		if (args.length > 1) {
			// if have more than two arguments
			int i;
			// first check whether user has input settings
			for (i = 0; i < args.length; i += 2) {
				if (args[i].startsWith("-") && args[i].length() == 2
						&& i < args.length) {
					if (args[i + 1].trim().matches("\\d+")) {
						switch (args[i].substring(1)) {
						case "t":
							threshold = Double.valueOf(args[i + 1]);
							break;
						case "d":
							depth = Integer.valueOf(args[i + 1]);
							break;
						case "b":
							breadth = Integer.valueOf(args[i + 1]);
							break;
						default:
							System.out.println(notice);
							return;
						}
					} else {
						System.out.println(notice);
						return;
					}
				} else {
					break;
				}
			}
			// get URL and validate it
			String url = args[i];
			if (!url.toLowerCase().startsWith("http://")
					&& !url.toLowerCase().startsWith("https://")) {
				// invalid URL
				System.out.println(notice);
				return;
			}
			// get keywords
			String[] keywords = new String[args.length - i - 1];
			int count = 0;
			for (i = i + 1; i < args.length; i++) {
				keywords[count++] = args[i].toLowerCase().trim();
				// System.out.println(keywords[count - 1]);
			}
			// System.out.println(keywords);
			// report user settings of current search
			System.out.println("Threshold: "
					+ (threshold == 80 ? "80(default)" : threshold));
			System.out.println("Depth: " + (depth == 4 ? "4(default)" : depth));
			System.out.println("Breadth: "
					+ (breadth == 2 ? "2(default)" : breadth));
			System.out.print("Keywords: ");
			for (int j = 0; j < keywords.length; j++) {
				System.out.print(keywords[j] + " ");
			}
			System.out.println();
			// start searching
			MarkLink ml = new MarkLink(keywords, url);
			WebNode wn = new WebNode(url);
			wn.setDepth(0);
			wn.setScore(ml.getScore());
			WebExplorer ws = WebExplorer.getInstance(keywords, threshold,
					depth, breadth);
			ws.search(wn);

		} else {
			// show fuzzy chart to user by sfc command
			if (args.length == 1 && args[0].equalsIgnoreCase("sfc")) {
				MarkLink ml = new MarkLink(null, null);
				ml.showCretia();
			} else {
				// invalid input
				System.out.println(notice);
			}
		}

	}
}
