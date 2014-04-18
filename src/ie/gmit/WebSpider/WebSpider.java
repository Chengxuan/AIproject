package ie.gmit.WebSpider;

public class WebSpider {

	public static void main(String[] args) {
		String notice = "Invalid Arguments:\n\t-t   change threshold of expecting goal Node"
				+ "\n\t-d   change depth of searching"
				+ "\n\t-b   change breadth of expecting goal Node";
		double threshold = 80;
		int depth = 4;
		int breadth = 2;
		if (args.length > 1) {
			int i;
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
			System.out.println(threshold);
			System.out.println(depth);
			System.out.println(breadth);
			String url = args[i];
			System.out.println(args[i]);
			String[] keywords = new String[args.length - i - 1];
			int count = 0;
			for (i = i + 1; i < args.length; i++) {
				keywords[count++] = args[i].toLowerCase().trim();
				System.out.println(keywords[count - 1]);
			}
			System.out.println(keywords);
			MarkLink ml = new MarkLink(keywords, url);
			WebNode wn = new WebNode(url);
			wn.setDepth(0);
			wn.setScore(ml.getScore());
			WebExplorer ws = WebExplorer.getInstance(keywords, threshold, depth,
					breadth);
			ws.search(wn);

		} else {
			if (args.length == 1 && args[0].equalsIgnoreCase("showfuz")) {
				MarkLink ml = new MarkLink(null, null);
				ml.showCretia();
			} else {
				System.out.println(notice);
			}
		}

	}
}
