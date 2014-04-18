package ie.gmit.WebSpider;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Fuzzy logic used to mark a link
 * 
 * @author Chengxuan Xing
 * 
 */
public class MarkLink {
	private static FIS fis; // fuzzy logic block
	private String[] keywords = {}; // set of search keywords
	private String title = ""; // title of the web page
	private String subTitles = ""; // h1-h6 headings
	private String text = ""; // body text
	private boolean got = false; // flag of whether the website has been
									// successfully parsed

	/**
	 * Constructor
	 * 
	 * @param keywords
	 *            --keywords used in search
	 * @param URL
	 *            --target URL
	 */
	public MarkLink(String[] keywords, String URL) {
		// Load and parse the FCL
		if (fis == null) {
			fis = FIS.load("conf/rules.fcl", true);
		}

		// get keywords need to be searched
		this.keywords = keywords;
		try {
			// try to fetching the website
			Document doc = Jsoup.connect(URL).get(); // get the DOM of page
			this.title = doc.title().toLowerCase(); // get title
			this.text = doc.text().toLowerCase(); // get text
			Elements hTags = doc.select("h1, h2, h3, h4, h5, h6"); // fetching
																	// headings
			// System.out.println(hTags.text());
			this.subTitles = hTags.text().toLowerCase(); // get headings
			got = true;
		} catch (Exception e) {
			/*
			 * Just skip this Node if URL is not valid
			 */

			// System.out.println("URL not valid:" + e.getMessage());
		}
	}

	private double getPosition() {
		double score = 0.0;
		// calculating the position of keywords
		for (int i = 0; i < keywords.length; i++) {
			if (title.contains(keywords[i]) || subTitles.contains(keywords[i])) {
				score += 8.0;
			}

			if (text.contains(keywords[i])) {
				score += 2.0;
			}

		}

		// 2 --- in text
		// 8 --- in title
		// 10 -- in both

		// get average position of the keywords
		score /= (keywords.length == 0 ? 1 : keywords.length);
		return score > 10.0 ? 10.0 : score; // shouldn't exceed max 10
	}

	private double getFrequency() {
		// get frequency of keywords appearing in the text
		double score = 0.0;
		for (int i = 0; i < keywords.length; i++) {
			int index = text.indexOf(keywords[i]);
			int count = 0;
			while (index != -1) {
				index = text.indexOf(keywords[i], index + 1);
				count++;
			}
			score += count;
		}
		// calculate average frequency
		score /= (keywords.length == 0 ? 1 : keywords.length);
		return score > 100.0 ? 100.0 : score; // if frequency reach 100 it's
												// extremely high.
	}

	private double getDistance() {
		// get minimum distance between words
		double score = 0.0;
		int count = 0;
		if (keywords.length > 1) {
			// this queue used to get the minimum value of the distance which
			// make the judgment optimal
			PriorityQueue<Integer> total = new PriorityQueue<Integer>(1000,
					new Comparator<Integer>() {
						@Override
						public int compare(Integer o1, Integer o2) {
							// greater one should behind
							return Integer.compare(o1, o2);
						}
					});

			for (int i = 0; i < keywords.length - 1; i++) {
				// get each words
				int index = text.indexOf(keywords[i]);
				while (index != -1) {
					index = text.indexOf(keywords[i], index + 1);
					for (int j = i + 1; j < keywords.length; j++) {
						if (text.indexOf(keywords[j], index + 1) - index >= 0) {
							total.offer(text.indexOf(keywords[j], index + 1)
									- index);
						}
					}
				}

			}
			// get at most 6 valuable data
			while (!total.isEmpty() && count < 6) {
				score += total.poll();
				count++;
			}

		}
		// if distance exceed 60 then it's too far
		return (score / (count > 0 ? count : 1)) > 60 ? 60
				: (score / (count > 0 ? count : 1));
	}

	/**
	 * Get the heuristic score of the URL passed in. If no URL has been passed
	 * in, the value is 0.
	 * 
	 * @return score
	 */
	public double getScore() {

		if (got) { // if have successfully fetched data from URL
			// do judgment
			fis.setVariable("position", this.getPosition());
			fis.setVariable("frequency", this.getFrequency());
			fis.setVariable("distance", this.getDistance());
			fis.evaluate(); // Execute the fuzzy inference engine
			return fis.getVariable("score").getValue();
		} else {
			return 0; // get 0 heuristic score which is extremely low
		}
	}

	/**
	 * Display the fuzzy chart of this FCL
	 */
	public void showCretia() {
		JFuzzyChart.get().chart(fis);// Display the fuzzy chart
	}

}
