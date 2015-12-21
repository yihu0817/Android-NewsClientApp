package com.warmtel.news.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsImageBean implements Parcelable{
	private String ref;
	private String pixel;
	private String alt;
	private String src;//图片网址
	
	public NewsImageBean()
	{
		
	}
	public NewsImageBean(Parcel in)
	{
		ref=in.readString();
		pixel=in.readString();
		alt=in.readString();
		src=in.readString();
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getPixel() {
		return pixel;
	}
	public void setPixel(String pixel) {
		this.pixel = pixel;
	}
	public String getAlt() {
		return alt;
	}
	public void setAlt(String alt) {
		this.alt = alt;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(alt);
		dest.writeString(pixel);
		dest.writeString(ref);
		dest.writeString(src);
	}
	public static final Creator<NewsImageBean> CREATOR=new Creator<NewsImageBean>() {

		@Override
		public NewsImageBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new NewsImageBean(source);
		}

		@Override
		public NewsImageBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new NewsImageBean[size];
		}
	};
	
}
