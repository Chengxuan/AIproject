package ie.gmit.Fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MarkLink {

	private MarkLink() {

	}

	public static MarkLink getInstance() {
		return new MarkLink();
	}

	public MarkSchema generateMarkSchema(String URL) {
		WebModel wm = new WebModel();
		try {
			Document doc = Jsoup.connect(URL).get();
			wm.setTitle(doc.getElementsByTag("title").get(0).text());
			wm.setURL(URL);
			// System.out.println(doc.getElementsByTag("h2").get(2).text());
			Elements links = doc.getElementsByTag("a");
			for (Element link : links) {

			}
		} catch (Exception e) {
			System.out.println("URL not valid:" + e.getMessage());
		}
		return new MarkSchema(2, 15, 7);
	}

	public double getScore(MarkSchema ms) {
		new MarkLink();
		FIS fis = FIS.load("conf/rules.fcl", true); // Load and parse the FCL
		FunctionBlock fb = fis.getFunctionBlock("WebSpider");
		// JFuzzyChart.get().chart(fis);// Display the linguistic variables and
		// terms
		fis.setVariable("position", ms.getPosition()); // Apply a value to a
														// variable
		fis.setVariable("frequency", ms.getFrequency());
		fis.setVariable("distance", ms.getDistance());
		fis.evaluate(); // Execute the fuzzy inference engine
		Variable tip = fb.getVariable("score");
		// System.out.println(tip.getValue());
		// JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		return fis.getVariable("score").getValue();
	}

	// public static void main(String[] args) {
	// MarkSchema ms = new MarkSchema(4, 20, 0);
	// // System.out.println(MarkLink.getScore(ms));
	// PriorityQueue<Double> abd = new PriorityQueue<Double>(10,
	// new Comparator<Double>() {
	//
	// @Override
	// public int compare(Double o1, Double o2) {
	// // TODO Auto-generated method stub
	// return Double.compare(o1, o2);
	// }
	// });
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
