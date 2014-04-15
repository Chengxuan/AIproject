package ie.gmit.WebNode;

import java.util.PriorityQueue;
import java.util.Queue;

public class WebNode implements Comparable<WebNode> {
	private String nodeURL;
	private WebNode parent;
	private Queue<WebNode> children = new PriorityQueue<WebNode>();
	private boolean visited = false;
	private double score;

	public WebNode(String name) {
		this.nodeURL = name;
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

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public boolean isGoalNode(double threshold) {
		return this.score >= threshold ? true : false;
	}

	public String toString() {
		return this.nodeURL;
	}

	@Override
	public int compareTo(WebNode o) {
		// TODO Auto-generated method stub
		return Double.compare(o.getScore(), this.score);
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
		this.score = score;
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

}
