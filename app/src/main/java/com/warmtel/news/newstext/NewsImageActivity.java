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
                switch (v.getId()){
                    case R.id.titlebar_left_img:
                        finish();
                        break;
                }
            }
        });

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
            Logs.e("ref :"+mNewsImageBeans.get(position).getRef());
            Logs.e("Pixel :"+mNewsImageBeans.get(position).getPixel());
            Logs.e("Alt :"+mNewsImageBeans.get(position).getAlt());
            Logs.e("src :"+mNewsImageBeans.get(position).getSrc());
            return NewsImageFragment.newInstance(mNewsImageBeans.get(position).getSrc(), mNewsImageBeans.get(position).getRef(),mNewsImageBeans.get(position).getAlt(),getCount());
        }

        @Override
        public int getCount() {
            return mNewsImageBeans.size();
        }
    }
}
