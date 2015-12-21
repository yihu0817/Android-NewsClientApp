package com.warmtel.news.bean;

import java.util.ArrayList;
import java.util.List;

public class NewsTextBean {
	private String digest;
	private String title;
	private String showPic;
	private String docId;
	private List<String> imgArray = new ArrayList<String>();

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShowPic() {
		return showPic;
	}

	public void setShowPic(String showPic) {
		this.showPic = showPic;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public List<String> getImgArray() {
		return imgArray;
	}

	public void setImgArray(List<String> imgArray) {
		this.imgArray = imgArray;
	}

}
