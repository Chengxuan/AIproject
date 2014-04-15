package ie.gmit.Fuzzy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class WebModel {
	private String URL;
	private String title; // 2 *2
	private List<String> subTitles; // 2
	private List<String> textList; // 4

	public WebModel() {
		this.URL = "invalid";
		this.title = "no";
		this.subTitles = new ArrayList<String>();
		this.textList = new ArrayList<String>();

	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the subTitles
	 */
	public List<String> getSubTitles() {
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(this.subTitles);
		this.subTitles.clear();
		this.subTitles.addAll(hs);
		return subTitles;
	}

	/**
	 * @param subTitles
	 *            the subTitles to set
	 */
	public void setSubTitles(List<String> subTitles) {
		this.subTitles = subTitles;
	}

	/**
	 * @return the textList
	 */
	public List<String> getTextList() {
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(this.textList);
		this.textList.clear();
		this.textList.addAll(hs);
		return textList;
	}

	/**
	 * @param textList
	 *            the textList to set
	 */
	public void setTextList(List<String> textList) {
		this.textList = textList;
	}

}
