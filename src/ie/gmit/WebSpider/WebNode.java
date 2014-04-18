package ie.gmit.WebSpider;

import java.util.PriorityQueue;
import java.util.Queue;

public class WebNode implements Comparable<WebNode> {
	private String nodeURL;
	private WebNode parent;
	private Queue<WebNode> children = new PriorityQueue<WebNode>();
	private double score;
	private int depth;

	public WebNode(String name) {
		this.nodeURL = name;
	}

	public void scoreURL(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			if (this.nodeURL.toLowerCase().trim()
					.contains(keywords[i].toLowerCase().trim()))
				this.score += 100.0;
		}
	}

	public WebNode getNextChildren() {
		return this.children.poll();
	}

	public boolean isLeaf() {
		if (children.size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	public int getChildNodeCount() {
		return children.size();
	}

	public void addChildNode(WebNode child) {
		children.add(child);
	}

	public void removeChild(WebNode child) {
		children.remove(child);
	}

	public String getNodeURL() {
		return nodeURL;
	}

	public void setNodeURL(String nodeName) {
		this.nodeURL = nodeName;
	}

	public boolean isGoalNode(double threshold) {
		return (this.score % 100) >= threshold ? true : false;
	}

	/**
	 * @return the score
	 */
	public double getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(double score) {
		this.score += score;
	}

	/**
	 * @return the parent
	 */
	public WebNode getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(WebNode parent) {
		this.parent = parent;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth
	 *            the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public int compareTo(WebNode o) {
		return Double.compare(o.getScore(), this.score);
	}

}
