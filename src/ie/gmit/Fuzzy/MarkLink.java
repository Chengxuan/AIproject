package ie.gmit.Fuzzy;

import java.util.Comparator;
import java.util.PriorityQueue;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class MarkLink {
	public static double getScore(MarkSchema ms) {

		FIS fis = FIS.load("conf/rules.fcl", true); // Load and parse the FCL
		FunctionBlock fb = fis.getFunctionBlock("WebSpider");
		JFuzzyChart.get().chart(fis);// Display the linguistic variables and
										// terms
		fis.setVariable("position", ms.getPosition()); // Apply a value to a
														// variable
		fis.setVariable("frequency", ms.getFrequency());
		fis.setVariable("distance", ms.getDistance());
		fis.evaluate(); // Execute the fuzzy inference engine
		Variable tip = fb.getVariable("score");
		JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);
		return fis.getVariable("score").getValue();
	}

	public static void main(String[] args) {
		MarkSchema ms = new MarkSchema(4, 20, 0);
		// System.out.println(MarkLink.getScore(ms));
		PriorityQueue<Double> abd = new PriorityQueue<Double>(10,
				new Comparator<Double>() {

					@Override
					public int compare(Double o1, Double o2) {
						// TODO Auto-generated method stub
						return Double.compare(o1, o2);
					}
				});
		abd.add(125.21);
		abd.add(123.02);
		abd.add(123.03);
		abd.add(124.01);
		abd.add(125.01);
		abd.add(123.21);
		while (!abd.isEmpty()) {
			System.out.println(abd.poll());
		}
	}

}
