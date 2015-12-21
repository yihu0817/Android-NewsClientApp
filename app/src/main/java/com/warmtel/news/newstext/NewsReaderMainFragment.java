package com.warmtel.news.newstext;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warmtel.news.R;
import com.warmtel.news.utils.Constans;
import com.warmtel.news.widget.PagerSlidingTabStrip;

/**
 * @author viktor
 * 
 */
public class NewsReaderMainFragment extends Fragment {
	private PagerSlidingTabStrip mPagerSlidingTabStrip;
	private ViewPager mViewPager;

	private String[] mNewsTitles = { Constans.NewsTextTitle.HEADER_LINE,
			Constans.NewsTextTitle.FUN, Constans.NewsTextTitle.SPORTS,
			Constans.NewsTextTitle.ECONOMICS,
			Constans.NewsTextTitle.SCIENCE
	};
	private int[] mNewsType = {
			Constans.NewsTextType.HEADER_LINE,
			Constans.NewsTextType.FUN, Constans.NewsTextType.SPORTS,
			Constans.NewsTextType.ECONOMICS,
			Constans.NewsTextType.SCIENCE 
	};
	
	public static Fragment newInstance() {
		NewsReaderMainFragment fragment = new NewsReaderMainFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_newstext_main_layout,container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mPagerSlidingTabStrip = (PagerSlidingTabStrip) getView().findViewById(R.id.pagerSlidingTabstrip);
		mViewPager = (ViewPager) getView().findViewById(R.id.main_viewpager);

		NewsReaderFragmentPagerAdapter adapter = new NewsReaderFragmentPagerAdapter(
				getActivity().getSupportFragmentManager(), mNewsType, mNewsTitles);
		mViewPager.setAdapter(adapter);

		mPagerSlidingTabStrip.setViewPager(mViewPager);

		setPagerSlidingValue();

	}

	/**
	 * 设置PagerSlidingTabStrip控件属性
	 */
	public void setPagerSlidingValue() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		// 设置为true 均匀分配title位置
		mPagerSlidingTabStrip.setShouldExpand(true);
		// 设置分割线为透明
		mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
		mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
		// 设置选中字体颜色和指示器颜色相同
		mPagerSlidingTabStrip.setIndicatorColor(0);
		mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#FFFFFF"));
	}

	private class NewsReaderFragmentPagerAdapter extends FragmentStatePagerAdapter {
		private int mSize;
		private int[] mNewsTypes;
		private String[] mNewsTitles;

		public NewsReaderFragmentPagerAdapter(FragmentManager fm,int[] newsType, String[] newsTitle) {
			super(fm);
			mNewsTypes = newsType;
			mNewsTitles = newsTitle;
			mSize = newsType.length;
		}

		@Override
		public int getCount() {
			return mSize;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTitles.length > 0 ? mNewsTitles[position] : "";
		}

		@Override
		public Fragment getItem(int position) {
			return NewsReaderFragment.newInstance(mNewsTypes[position]);

		}
	}
}
