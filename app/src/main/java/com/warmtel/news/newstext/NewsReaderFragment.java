/**
 * @author viktor
 * @version v1.0
 */
package com.warmtel.news.newstext;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyErrorHelper;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.scxh.slider.library.Indicators.PagerIndicator.IndicatorVisibility;
import com.scxh.slider.library.SliderLayout;
import com.scxh.slider.library.SliderLayout.PresetIndicators;
import com.scxh.slider.library.SliderTypes.BaseSliderView;
import com.scxh.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.scxh.slider.library.SliderTypes.TextSliderView;
import com.warmtel.android.xlistview.XListView;
import com.warmtel.android.xlistview.XListView.IXListViewListener;
import com.warmtel.news.R;
import com.warmtel.news.RequestManager;
import com.warmtel.news.bean.NewsTextBean;
import com.warmtel.news.utils.Constans;
import com.warmtel.news.utils.Logs;

import java.util.ArrayList;
import java.util.List;

public class NewsReaderFragment extends Fragment implements IXListViewListener,
        AdapterView.OnItemClickListener {
    private static final String NEWS_HEADLINE_URL = "http://c.m.163.com/nc/article/headline/";// 头条URL
    private static final String NEWS_OTHERS_URL = "http://c.m.163.com/nc/article/list/";// 其他URL
    private static final String NEWS_TYPE = "NEWS_TYPE";
    private static final int PAGE_SIZE = 20; // 第页20条数据

    private String[] mNewsKeys = {Constans.NewsTextKey.HEADER_LINE,
            Constans.NewsTextKey.FUN, Constans.NewsTextKey.SPORTS,
            Constans.NewsTextKey.ECONOMICS,
            Constans.NewsTextKey.SCIENCE
    };

    private XListView mListView;
    private SliderLayout mSliderLayout;
    private ReadNewsAdapter mReadNewsAdapter;

    private int mCurrentPage = 0;
    private int mNewsType; /* 新闻类型 如:头条,娱乐... */

    // 封装的构造方法
    public static NewsReaderFragment newInstance(int newsType) {
        NewsReaderFragment readnewsFrag = new NewsReaderFragment();
        Bundle args = new Bundle();
        args.putInt(NEWS_TYPE, newsType);
        readnewsFrag.setArguments(args);

        return readnewsFrag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsType = getArguments() != null ? getArguments().getInt(NEWS_TYPE) : 0;
    }

    // 第三个执行
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootContainerView = inflater.inflate(R.layout.fragment_newstext_layout, container, false);

        View listEmpleyLayout = rootContainerView.findViewById(R.id.listview_frame_layout);

        View sliderHeaderView = inflater.inflate(R.layout.view_newstext_slider_header_layout, null);

        mListView = (XListView) rootContainerView.findViewById(R.id.xlistview);
        mSliderLayout = (SliderLayout) sliderHeaderView.findViewById(R.id.read_news_slider);

        mListView.addHeaderView(sliderHeaderView);
        mListView.setEmptyView(listEmpleyLayout);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(this);
        mReadNewsAdapter = new ReadNewsAdapter(getActivity());
        mListView.setAdapter(mReadNewsAdapter);

        return rootContainerView;
    }

    // 第四个执行
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurrentPage = 0;
        getDataByVolley(mCurrentPage);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSliderLayout != null) {
            mSliderLayout.stopAutoCycle();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getUrl(int currentPage) {

        String pagerUrl = "/" + currentPage * PAGE_SIZE + "-" + PAGE_SIZE + ".html";

        switch (mNewsType) {
            case Constans.NewsTextType.HEADER_LINE:
                return NEWS_HEADLINE_URL + Constans.NewsTextKey.HEADER_LINE
                        + pagerUrl;
            case Constans.NewsTextType.FUN:
                return NEWS_OTHERS_URL + Constans.NewsTextKey.FUN
                        + pagerUrl;
            case Constans.NewsTextType.SPORTS:
                return NEWS_OTHERS_URL + Constans.NewsTextKey.SPORTS
                        + pagerUrl;
            case Constans.NewsTextType.ECONOMICS:
                return NEWS_OTHERS_URL + Constans.NewsTextKey.ECONOMICS
                        + pagerUrl;
            case Constans.NewsTextType.SCIENCE:
                return NEWS_OTHERS_URL + Constans.NewsTextKey.SCIENCE
                        + pagerUrl;
            default:
                return null;
        }
    }

    /**
     * @param currentPage 页号
     * @param pageSize    页大小
     */
    public void getDataByVolley(int currentPage) {

        StringRequest request = new StringRequest(getUrl(currentPage),
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        List<NewsTextBean> readList = parseNewsJson(response, mNewsKeys[mNewsType]);

                        mListView.stopRefresh();
                        mListView.setRefreshTime("刚刚");
                        showReadPic(readList);
                        mReadNewsAdapter.addDatas(mCurrentPage, readList);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getActivity(),
                        VolleyErrorHelper.getMessage(error,
                                getActivity()), Toast.LENGTH_SHORT).show();

            }
        });

        if (currentPage == 0) {
            request.setShouldCache(true);
            RequestManager.getRequestQueue().add(request);
        } else {
            //TODO: 2015/12/10  :解决缓存和网络请求加载两次数据问题, 实现只缓存第一页数据
            request.setShouldCache(false);
            RequestManager.getRequestQueue().add(request);
        }

    }

    @Override
    public void onRefresh() {
        mCurrentPage = 0;
        getDataByVolley(mCurrentPage);
    }

    @Override
    public void onLoadMore() {
        mCurrentPage++;
        getDataByVolley(mCurrentPage);
    }

    public void showReadPic(List<NewsTextBean> readList) {
        if (mCurrentPage == 0) {
            mSliderLayout.removeAllSliders();// 移除所有的Slider
            int length = readList.size() > 3 ? 3 : readList.size();
            for (int i = 0; i <= length; i++) {
                TextSliderView textSlider = new TextSliderView(getActivity());
                textSlider.description(readList.get(i).getTitle())
                        .image(readList.get(i).getShowPic())
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop);
                final String docId = readList.get(i).getDocId();
                textSlider.setOnSliderClickListener(new OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        NewsContentActivity.onStartActivityAction(getActivity(), docId);
                    }
                });// 实现TextSliderView的点击监听
                mSliderLayout.addSlider(textSlider); // 添加进SliderLayout
            }
            // 设置指示器位置
            mSliderLayout.setPresetIndicator(PresetIndicators.Right_Bottom);
            // 指示符是否显示
            mSliderLayout.setIndicatorVisibility(IndicatorVisibility.Visible);
        }

    }

    /**
     * 解析json字符串
     *
     * @param response
     * @param newsKey
     * @return
     */
    public List<NewsTextBean> parseNewsJson(String response, String newsKey) {
        List<NewsTextBean> newsLists = new ArrayList<NewsTextBean>();

        JSONObject rootObj = JSONObject.parseObject(response);

        JSONArray newsArrayObj = rootObj.getJSONArray(newsKey);
        for (int i = 1; i < newsArrayObj.size() - 1; i++) {
            JSONObject newsObj = newsArrayObj.getJSONObject(i);
            String digest = newsObj.getString("digest");
            String title = newsObj.getString("title");
            String showPic = newsObj.getString("imgsrc");
            String docId = newsObj.getString("docid");

            List<String> imgArrayList = new ArrayList<String>();
            if (newsObj.containsKey("imgextra")) {
                JSONArray imgArray = newsObj.getJSONArray("imgextra");
                if (imgArray != null && imgArray.size() > 0) {
                    for (int k = 0; k < imgArray.size(); k++) {
                        JSONObject obj0 = (JSONObject) imgArray.getJSONObject(k);
                        imgArrayList.add(obj0.getString("imgsrc"));
                    }
                }
            }

            NewsTextBean readObj = new NewsTextBean();
            readObj.setDigest(digest);
            readObj.setTitle(title);
            readObj.setShowPic(showPic);
            readObj.setDocId(docId);
            readObj.setImgArray(imgArrayList);

            newsLists.add(readObj);
        }
        return newsLists;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//      ReadNewsAdapter adapter = (ReadNewsAdapter) parent.getAdapter(); // TODO: 2015/12/10 报错 类型转换异常
//      NewsTextBean newItem = (NewsTextBean) adapter.getItem(position);
        NewsTextBean newItem = (NewsTextBean) parent.getAdapter().getItem(position);
        String docId = newItem.getDocId();
        NewsContentActivity.onStartActivityAction(getActivity(), docId);
    }

    /**
     * 新闻适配器
     */
    class ReadNewsAdapter extends BaseAdapter {
        List<NewsTextBean> readListAll = new ArrayList<NewsTextBean>();
        LayoutInflater inflater;
        Context mContext;
        private static final int TYPE_TXT = 0;
        private static final int TYPE_IMG = 1;

        public ReadNewsAdapter(Context context) {
            mContext = context;
            inflater = LayoutInflater.from(context);
        }

        // 封装方法
        public void addDatas(int CurrentPage, List<NewsTextBean> list) {
            // 用mCurrentPage判断是set数据 还是添加数据
            if (CurrentPage == 0) {
                setData(list);
            } else {
                addData(list);
            }
        }

        public void setData(List<NewsTextBean> list) {
            readListAll = list;
            notifyDataSetChanged();
        }

        public void addData(List<NewsTextBean> list) {
            if (list != null) {
                readListAll.addAll(list);
            }
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return readListAll.size();
        }

        @Override
        public Object getItem(int position) {
            return readListAll.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (readListAll.get(position).getImgArray().isEmpty()) {
                return TYPE_TXT;
            } else {
                return TYPE_IMG;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            if (type == TYPE_TXT) {
                return getOneView(position, convertView, parent);
            } else {
                return getTwoView(position, convertView, parent);
            }
        }

        public View getOneView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_newstext_text_item, null);
                viewHolder.descriptionTxt = (TextView) convertView.findViewById(R.id.read_news_show_digest);
                viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.read_news_show_title);
                viewHolder.newsImg = (NetworkImageView) convertView.findViewById(R.id.read_news_show_pic);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            NewsTextBean readObj = (NewsTextBean) getItem(position);

            viewHolder.descriptionTxt.setText(readObj.getDigest());
            viewHolder.titleTxt.setText(readObj.getTitle());
            onRequestByNetWorkImageView(readObj.getShowPic(), viewHolder.newsImg);
            return convertView;
        }

        public View getTwoView(int position, View convertView, ViewGroup parent) {
            ViewHolder_IMG viewHolder_IMG = null;
            if (convertView == null) {
                viewHolder_IMG = new ViewHolder_IMG();
                convertView = inflater.inflate(R.layout.view_newstext_img_item, null);
                viewHolder_IMG.newsTitleTxt = (TextView) convertView.findViewById(R.id.read_news_img_title);
                viewHolder_IMG.newsOneImg = (NetworkImageView) convertView.findViewById(R.id.read_news_img_pic1);
                viewHolder_IMG.newsTwoImg = (NetworkImageView) convertView.findViewById(R.id.read_news_img_pic2);
                viewHolder_IMG.newsThreeImg = (NetworkImageView) convertView.findViewById(R.id.read_news_img_pic3);
                convertView.setTag(viewHolder_IMG);
            } else {
                viewHolder_IMG = (ViewHolder_IMG) convertView.getTag();
            }
            NewsTextBean readObj = (NewsTextBean) getItem(position);
            viewHolder_IMG.newsTitleTxt.setText(readObj.getTitle());

            onRequestByNetWorkImageView(readObj.getShowPic(), viewHolder_IMG.newsOneImg);

            if (readObj.getImgArray().size() >= 2) {
                onRequestByNetWorkImageView(readObj.getImgArray().get(0), viewHolder_IMG.newsTwoImg);
                onRequestByNetWorkImageView(readObj.getImgArray().get(1), viewHolder_IMG.newsThreeImg);
            }
            return convertView;
        }

        /**
         * Volley 获取网络图片方法之 NetWorkImageView
         *
         * @param imageUrl
         * @param imageView
         */
        private void onRequestByNetWorkImageView(String imageUrl,
                                                 NetworkImageView imageView) {
            imageView.setDefaultImageResId(R.drawable.loading_pin2);// 默认图片
            imageView.setErrorImageResId(android.R.drawable.ic_input_delete);// 出错时的图片
            imageView.setImageUrl(imageUrl, RequestManager.getImageLoader());// 请求图片
        }

        class ViewHolder {
            NetworkImageView newsImg;
            TextView titleTxt;
            TextView descriptionTxt;
        }

        class ViewHolder_IMG {
            NetworkImageView newsOneImg;
            NetworkImageView newsTwoImg;
            NetworkImageView newsThreeImg;
            TextView newsTitleTxt;
        }

    }
}
