/**
 * @author viktor
 * @version v1.0
 * TODO: 2015/12/11
 */

package com.warmtel.news.newstext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.warmtel.news.R;
import com.warmtel.news.bean.NewsImageBean;
import com.warmtel.news.utils.Logs;
import com.warmtel.news.widget.TitleBar;

import java.util.ArrayList;


public class NewsImageActivity extends FragmentActivity {
    private static final String NEW_TITLE = "new_title"; //新闻标题
    private static final String NEW_IMAGE_LISTS = "new_image_lists"; //新闻图片集
    private ViewPager mViewPager;
    private TitleBar mTitleBar;

    public static void onStartActivityAction(Context context, String newsTitle, ArrayList<NewsImageBean> newsImageBeans) {
        Intent intent = new Intent(context, NewsImageActivity.class);
        intent.putExtra(NEW_TITLE, newsTitle);
        intent.putParcelableArrayListExtra(NEW_IMAGE_LISTS, newsImageBeans);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_image_layout);
        mViewPager = (ViewPager) findViewById(R.id.news_image_viewpager);
        mTitleBar = (TitleBar) findViewById(R.id.news_image_titlebar);

        String newsTitle = getIntent().getStringExtra(NEW_TITLE);
        ArrayList<NewsImageBean> mNewsImageBeans = getIntent().getParcelableArrayListExtra(NEW_IMAGE_LISTS);

        NewImageAdapter adapter = new NewImageAdapter(getSupportFragmentManager(), newsTitle, mNewsImageBeans);
        mViewPager.setAdapter(adapter);

        mTitleBar.setTitleTxt(newsTitle);
        mTitleBar.registOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.titlebar_left_img:
                        finish();
                        break;
                }
            }
        });

//        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    public static class NewImageAdapter extends FragmentStatePagerAdapter {
        public ArrayList<NewsImageBean> mNewsImageBeans = new ArrayList<>();
        public String mNewTitle;

        public NewImageAdapter(FragmentManager fm, String title, ArrayList<NewsImageBean> mNewsImages) {
            super(fm);
            mNewsImageBeans = mNewsImages;
            mNewTitle = title;
        }

        @Override
        public Fragment getItem(int position) {
            Logs.e("ref :" + mNewsImageBeans.get(position).getRef());
            Logs.e("Pixel :" + mNewsImageBeans.get(position).getPixel());
            Logs.e("Alt :" + mNewsImageBeans.get(position).getAlt());
            Logs.e("src :" + mNewsImageBeans.get(position).getSrc());
            return NewsImageFragment.newInstance(mNewsImageBeans.get(position).getSrc(), mNewsImageBeans.get(position).getRef(), mNewsImageBeans.get(position).getAlt(), getCount());
        }

        @Override
        public int getCount() {
            return mNewsImageBeans.size();
        }
    }


}
