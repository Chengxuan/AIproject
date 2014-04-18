package ie.gmit.WebSpider;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Node contains search node data
 * 
 * @author Chengxuan Xing
 * 
 */
public class WebNode implements Comparable<WebNode> {
	private String nodeURL; // URL ID
	private WebNode parent; // parent node
	// children node arrange by there priorities
	private Queue<WebNode> children = new PriorityQueue<WebNode>();
	private double score; // heuristic score
	private int depth; // depth of this node

	/**
	 * Constructor
	 * 
	 * @param name
	 *            --URL of the node
	 */
	public WebNode(String name) {
		this.nodeURL = name;
	}

	/**
	 * Score node by keywords frequency in URL
	 * 
	 * @param keywords
	 *            --searching keywords
	 */
	public void scoreURL(String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			if (this.nodeURL.toLowerCase().trim()
					.contains(keywords[i].toLowerCase().trim()))
				this.score += 100.0;
		}
	}

	/**
	 * Get next children
	 * 
	 * @return Current highest score children node
	 */
	public WebNode getNextChildren() {
		return this.children.poll();
	}

	/**
	 * Get the amount of child nodes
	 * 
	 * @return Amount of children
	 */
	public int getChildNodeCount() {
		return children.size();
	}

	/**
	 * Add a child to the node
	 * 
	 * @param child
	 *            --child node for adding
	 */
	public void addChildNode(WebNode child) {
		children.add(child);
	}

	/**
	 * Get the URL of the node
	 * 
	 * @return URL
	 */
	public String getNodeURL() {
		return nodeURL;
	}

	/**
	 * Set the URL of the node
	 * 
	 * @param nodeName
	 *            --URL
	 */
	public void setNodeURL(String nodeName) {
		this.nodeURL = nodeName;
	}

	/**
	 * Judge whether current node satisfy threshold or not
	 * 
	 * @param threshold
	 *            --expecting goal node score
	 * @return true -- satisfy<br/>
	 *         false -- not satisfy
	 */
	public boolean isGoalNode(double threshold) {
		return (this.score % 100) >= threshold ? true : false;
	}

	/**
	 * Return the heuristic score of the node
	 * 
	 * @return score of node
	 */
	public double getScore() {
		return score;
	}

	/**
	 * Set score of the node
	 * 
	 * @param score
	 *            --score to set
	 */
	public void setScore(double score) {
		this.score += score;
	}

	/**
	 * Get the parent of the node
	 * 
	 * @return parent
	 */
	public WebNode getParent() {
		return parent;
	}

	/**
	 * Set the parent of the node
	 * 
	 * @param parent
	 *            --parent node
	 */
	public void setParent(WebNode parent) {
		this.parent = parent;
	}

	/**
	 * Get the depth of the node
	 * 
	 * @return depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * 
	 * Set the depth of the node
	 * 
	 * @param depth
	 *            --depth
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public int compareTo(WebNode o) {
		return Double.compare(o.getScore(), this.score);
	}

}
