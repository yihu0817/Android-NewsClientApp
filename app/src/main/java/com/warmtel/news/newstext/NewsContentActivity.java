/**
 * @author viktor
 * @version v1.0
 * TODO: 2015/12/11
 */
package com.warmtel.news.newstext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyErrorHelper;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.warmtel.news.R;
import com.warmtel.news.RequestManager;
import com.warmtel.news.bean.NewsContentBean;
import com.warmtel.news.bean.NewsImageBean;
import com.warmtel.news.widget.TitleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsContentActivity extends Activity {
    public static final String DOC_ID = "DOC_ID";
    private TitleBar mTitleBar;
    private TextView mNewsTitleTxt, mNewsSourceTxt, mNewsCount, mNewsContentTxt;
    private ImageView mNewsImg, mLoadImg;
    private RelativeLayout mLoadLayout;
    private int mShortAnimationDuration = 1000;
    private String mNewsContentTitle; //用于图片浏览界面
    private ArrayList<NewsImageBean> mNewsImageLists;//用于图片浏览界面

    public static void onStartActivityAction(Context context, String docId) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra(DOC_ID, docId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news_content_layout);
        mTitleBar = (TitleBar) findViewById(R.id.web_title_layout);
        mNewsTitleTxt = (TextView) findViewById(R.id.web_news_title);
        mNewsSourceTxt = (TextView) findViewById(R.id.web_news_source);
        mNewsCount = (TextView) findViewById(R.id.web_news_img_count);
        mNewsContentTxt = (TextView) findViewById(R.id.web_news_infoTxt);
        mNewsImg = (ImageView) findViewById(R.id.web_news_img);
        mLoadImg = (ImageView) findViewById(R.id.web_news_load_img);
        mLoadLayout = (RelativeLayout) findViewById(R.id.web_news_load_layout);

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

        mNewsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsImageActivity.onStartActivityAction(NewsContentActivity.this,mNewsContentTitle,mNewsImageLists);
            }
        });

        setLoadImgAnim();
        getNewsContentData();
    }

    /**
     * 加载动画
     */
    public void setLoadImgAnim() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.news_info_load_rorate);
        mLoadImg.startAnimation(animation);
    }

    /**
     * 获取新闻内容数据
     */
    public void getNewsContentData() {
        final String docId = getIntent().getStringExtra(DOC_ID);
        String url = getString(R.string.news_content_url, docId);

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                NewsContentBean newsInfoUseObj = null;
                try {
                    newsInfoUseObj = parserJsonNewsContent(response, docId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setNewsView(newsInfoUseObj);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        NewsContentActivity.this,
                        VolleyErrorHelper.getMessage(error,
                                NewsContentActivity.this), Toast.LENGTH_SHORT).show();
            }
        });

        //TODO: 2015/12/10  :解决缓存和网络请求加载两次数据问题, 实现只缓存第一页数据
        request.setShouldCache(false);
        RequestManager.getRequestQueue().add(request);
    }

    /**
     * 新闻内容JSON解析
     *
     * @param response
     * @param docId
     * @return
     * @throws JSONException
     */
    public NewsContentBean parserJsonNewsContent(String response, String docId) throws JSONException {
        JSONObject rootObj = new JSONObject(response);
        JSONObject localObj = rootObj.getJSONObject(docId);// 得到以这个端口命名的Json对象

        List<NewsImageBean> imageLists = new ArrayList<NewsImageBean>(); //存储图片
        String picUrl; //内容界面显示的图片
        int picCount = 0;  //图片个数

        if (localObj.isNull("img")) {
            picUrl = null;
        } else {
            JSONArray jsonArrayImg = localObj.getJSONArray("img");
            picCount = jsonArrayImg.length();
            if (picCount <= 0) {
                picUrl = null;
            } else {
                picUrl = jsonArrayImg.getJSONObject(0).getString("src");
                parserJsonImage(jsonArrayImg,imageLists);
            }
        }

        NewsContentBean newsContent = new NewsContentBean();
        newsContent.setBody(localObj.getString("body"));
        newsContent.setTitle(localObj.getString("title"));
        newsContent.setSource(localObj.getString("source"));
        newsContent.setPtime(localObj.getString("ptime"));
        newsContent.setPicCount(picCount);
        newsContent.setPicUrl(picUrl);
        newsContent.setImg(imageLists);
        return newsContent;
    }

    public void parserJsonImage(JSONArray jsonArrayImg,List<NewsImageBean> imageLists) throws JSONException {
        int picCount = jsonArrayImg.length();
        for (int i = 0; i < picCount; i++) {
            NewsImageBean imgObj = new NewsImageBean();

            JSONObject jsonObject = jsonArrayImg.getJSONObject(i);
            if (jsonObject.has("ref")) {
                imgObj.setRef(jsonObject.get("ref").toString());
            } else {
                imgObj.setRef("");
            }

            if (jsonObject.has("pixel")) {
                imgObj.setPixel(jsonObject.get("pixel").toString());
            } else {
                imgObj.setPixel("");
            }

            if (jsonObject.has("alt")) {
                imgObj.setAlt(jsonObject.get("alt").toString());
            } else {
                imgObj.setAlt("");
            }
            if (jsonObject.has("src")) {
                imgObj.setSrc(jsonObject.get("src").toString());
            } else {
                imgObj.setSrc("");
            }

            imageLists.add(imgObj);
        }
    }

    public void setNewsView(NewsContentBean newsContent) {
        if(newsContent == null){
            return;
        }
        mNewsContentTitle = newsContent.getTitle();
        mNewsImageLists = (ArrayList<NewsImageBean>) newsContent.getImg();

        mLoadLayout.setVisibility(View.GONE);
        mNewsTitleTxt.setText(newsContent.getTitle());
        mNewsSourceTxt.setText(String.format(getString(R.string.news_content_source), newsContent.getSource(), newsContent.getPtime()));
        if ((newsContent.getPicUrl()) != null) {
            Picasso.with(this).load(newsContent.getPicUrl()).into(mNewsImg);
            mNewsCount.setVisibility(View.VISIBLE);
            mNewsCount.setText(getString(R.string.news_content_count, newsContent.getPicCount()));
            mNewsCount.getBackground().setAlpha(100);
        } else {
            mNewsImg.setVisibility(View.GONE);
            mNewsCount.setVisibility(View.GONE);
        }
        mNewsContentTxt.setText(Html.fromHtml(newsContent.getBody()));
//        crossfade();
    }

    /**
     * View渐变动画
     */
    private void crossfade() {

        // 设置内容View为0%的不透明度，但是状态为“可见”，
        // 因此在动画过程中是一直可见的（但是为全透明）。
        mNewsContentTxt.setAlpha(0f);
        mNewsContentTxt.setVisibility(View.VISIBLE);

        // 开始动画内容View到100%的不透明度，然后清除所有设置在View上的动画监听器。
        mNewsContentTxt.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // 加载View开始动画逐渐变为0%的不透明度，
        // 动画结束后，设置可见性为GONE（消失）作为一个优化步骤
        //（它将不再参与布局的传递等过程）
        mLoadLayout.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoadLayout.setVisibility(View.GONE);
                    }
                });
    }

}
