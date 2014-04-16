package ie.gmit.Fuzzy;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MarkLink {

	private String[] keywords = {};
	private String title = "";
	private String url = "";
	private String subTitles = "";
	private String text = "";

	public MarkLink(String[] keywords, String URL) {
		this.keywords = keywords;
		try {
			Document doc = Jsoup.connect(URL).get();
			this.title = doc.title();
			this.text = doc.text();
			this.url = URL;
			// System.out.println(doc.getElementsByTag("h2").get(2).text());
			Elements hTags = doc.select("h1, h2, h3, h4, h5, h6");
			// System.out.println(hTags.text());
			this.subTitles = hTags.text();
		} catch (Exception e) {
			// System.out.println("URL not valid:" + e.getMessage());
		}
	}

	private double getPosition() {
		double score = 0.0;
		int count = 0;
		for (int i = 0; i < keywords.length; i++) {
			if (title.contains(keywords[i])) {
				score += 8.0;
				count++;
			}

			if (text.contains(keywords[i])) {
				score += 2.0;
				count++;
			}

			if (subTitles.contains(keywords[i])) {
				score += 4.0;
				count++;
			}

		}
		score /= count == 0 ? 1 : count;
		return score > 14.0 ? 14.0 : score;
	}

	private double getFrequency() {
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
		// score /= keywords.length == 0 ? 1 : keywords.length;
		return score > 1000.0 ? 1000.0 : score;
	}

	private double getDistance() {
		double score = 0.0;
		int count = 0;
		if (keywords.length > 1) {
			PriorityQueue<Integer> total = new PriorityQueue<Integer>(1000,
					new Comparator<Integer>() {
						@Override
						public int compare(Integer o1, Integer o2) {
							// TODO Auto-generated method stub
							return Integer.compare(o1, o2);
						}
					});

			for (int i = 0; i < keywords.length - 1; i++) {

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

			while (!total.isEmpty() && count < 6) {
				score += total.poll();
				count++;
			}

		}

		return score / (count > 0 ? count : 1);
	}

	public double getScore() {
		FIS fis = FIS.load("conf/rules.fcl", true); // Load and parse the FCL
		FunctionBlock fb = fis.getFunctionBlock("WebSpider");
		// JFuzzyChart.get().chart(fis);// Display the linguistic variables and
		// terms
		fis.setVariable("position", this.getPosition()); // Apply a
															// value
															// to a
		// variable
		fis.setVariable("frequency", this.getFrequency());
		fis.setVariable("distance", this.getDistance());
		fis.evaluate(); // Execute the fuzzy inference engine
		Variable tip = fb.getVariable("score");
		// System.out.println(tip.getValue());
		// JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		return fis.getVariable("score").getValue();
	}

	public void showCretia() {
		FIS fis = FIS.load("conf/rules.fcl", true); // Load and parse the FCL
		// FunctionBlock fb = fis.getFunctionBlock("WebSpider");
		JFuzzyChart.get().chart(fis);// Display the linguistic variables and
	}

	public static void main(String[] args) {
		MarkLink ml = new MarkLink(null, null);
		ml.showCretia();
	}
	// }

}
