package ie.gmit.Fuzzy;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
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
			System.out.println("URL not valid:" + e.getMessage());
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
		PriorityQueue<Double> abd = new PriorityQueue<Double>(10,
				new Comparator<Double>() {
					@Override
					public int compare(Double o1, Double o2) {
						// TODO Auto-generated method stub
						return Double.compare(o1, o2);
					}
				});
		return score;
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

	// public static void main(String[] args) {
	// MarkSchema ms = new MarkSchema(4, 20, 0);
	// // System.out.println(MarkLink.getScore(ms));

	// abd.add(125.21);
	// abd.add(123.02);
	// abd.add(123.03);
	// abd.add(124.01);
	// abd.add(125.01);
	// abd.add(123.21);
	// while (!abd.isEmpty()) {
	// System.out.println(abd.poll());
	// }
	// }

}
