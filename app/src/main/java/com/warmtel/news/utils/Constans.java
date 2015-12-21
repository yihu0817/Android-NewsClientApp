package com.warmtel.news.utils;

public class Constans {
	public interface NewsTextType {
		public static final int HEADER_LINE = 0;// 头条
		public static final int FUN = 1;// 娱乐
		public static final int SPORTS = 2;// 体育
		public static final int ECONOMICS = 3;// 财经
		public static final int SCIENCE = 4;// 科技
	}
	
	// 新闻类型标识
	public interface NewsTextKey {
		public static final String HEADER_LINE = "T1348647909107";//此处表示头条新闻
		public static final String FUN = "T1348648517839";// 娱乐新闻
		public static final String SPORTS = "T1348649079062";// 体育新闻
		public static final String ECONOMICS = "T1348648756099";// 财经新闻
		public static final String SCIENCE = "T1348649580692";// 科技新闻
	}

	public interface NewsTextTitle {
		public static final String HEADER_LINE = "头条";
		public static final String FUN = "娱乐";
		public static final String SPORTS = "体育";
		public static final String ECONOMICS = "财经";
		public static final String SCIENCE = "科技";
	}
}
