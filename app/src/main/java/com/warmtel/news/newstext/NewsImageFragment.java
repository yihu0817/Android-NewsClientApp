/**
 * @author viktor
 * @version v1.0
 *  TODO: 2015/12/11
 */
package com.warmtel.news.newstext;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.warmtel.imagecache.AsyncMemoryFileCacheImageLoader;
import com.warmtel.news.R;

public class NewsImageFragment extends Fragment {
    private static final String PIC_URL = "PIC_URL";
    private static final String TEXT_CONTENT = "TEXT_CONTENT";
    private static final String PIC_COUNT = "PIC_COUNT";
    private static final String ALT = "ALT";
    private ProgressBar mProgressBar;
    private TextView mContentTxt,mPageTxt;
    private ImageView mImageView;
    private String mPicUrl;
    private String mTextContent;
    private int mTotalPage; //页数
    private String mCurrentPage; //当前页号

    public static NewsImageFragment newInstance(String picUrl, String textContent,String alt,int count) {
        NewsImageFragment fragment = new NewsImageFragment();
        Bundle args = new Bundle();
        args.putString(PIC_URL, picUrl);
        args.putString(TEXT_CONTENT, textContent);
        args.putString(ALT, alt);
        args.putInt(PIC_COUNT, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPicUrl = getArguments().getString(PIC_URL);
            mTextContent = getArguments().getString(TEXT_CONTENT);
            mTotalPage = getArguments().getInt(PIC_COUNT);
            mCurrentPage = getArguments().getString(ALT); //Alt :<!--IMG#1-->
            int startIndex = mCurrentPage.indexOf("#");
            mCurrentPage = mCurrentPage.substring(startIndex);
            int index = mCurrentPage.indexOf("-");
            mCurrentPage = mCurrentPage.substring(1,index);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_image_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressBar = (ProgressBar) getView().findViewById(R.id.news_image_progressbar);
        mImageView = (ImageView) getView().findViewById(R.id.news_image_content_img);
        mContentTxt = (TextView) getView().findViewById(R.id.news_image_content_txt);
        mPageTxt = (TextView) getView().findViewById(R.id.news_image_page_txt);
        mProgressBar.setVisibility(View.VISIBLE);
        /*Picasso.with(getActivity()).load(mPicUrl).into(mImageView, new Callback() {
            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mImageView.setImageResource(R.drawable.loading_pin2);
                mProgressBar.setVisibility(View.GONE);
            }
        });*/

        AsyncMemoryFileCacheImageLoader.getInstance(getActivity()).loadBitmap(getResources(),mPicUrl, mImageView, new AsyncMemoryFileCacheImageLoader.Callback() {

            @Override
            public void onSuccess() {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                mImageView.setImageResource(R.drawable.loading_pin2);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        mContentTxt.setText(mTextContent);
        mPageTxt.setText((Integer.parseInt(mCurrentPage) + 1) + "/" + mTotalPage);
    }
}
