package com.warmtel.news.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.warmtel.news.R;

public class TitleBar extends RelativeLayout implements OnClickListener{
	private String mTitleTxt;
	private Drawable mLeftIcon;
	private Drawable mRightIcon;
	private TextView mTitleView;
	private ImageView mLeftView, mRightView;
	private int mTitleBgColor;

	private OnClickListener onClickListener;
	public void registOnClickListener(OnClickListener l) {
		this.onClickListener = l;
	}
	public TitleBar(Context context) {
		super(context, null);
		init(context);
	}
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TitleBar, 0, 0);
		try {
			mTitleTxt = a.getString(R.styleable.TitleBar_middle_txt_cust);
			mLeftIcon = a.getDrawable(R.styleable.TitleBar_left_icon_cust);
			mRightIcon = a.getDrawable(R.styleable.TitleBar_right_icon_cust);
			mTitleBgColor = a.getColor(R.styleable.TitleBar_titlebar_title_bg, Color.RED);
			if (mTitleTxt != null) {
				mTitleView.setText(mTitleTxt);
			}
			if (mLeftIcon != null) {
				setLeftIcon(mLeftIcon);
			}
			if (mRightIcon != null) {
				setRightIcon(mRightIcon);
			}

			setmTitleBgColor(mTitleBgColor);

		} finally {
			a.recycle();
		}
	}
	
	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		//注意这个构造方法要用this，不能为null，因为这个是(可选View作为布局的父控件)Optional view to be the parent of the generated hierarchy.
		//如果需要此布局在指定的父控件上显示就必须要指定成this。
		inflater.inflate(R.layout.widget_titlebar_layout, this, true);
		mTitleView = (TextView) findViewById(R.id.titlebar_middle_Title);
	}

	public String getTitleTxt() {
		return mTitleTxt;
	}

	public void setTitleTxt(String titleTxt) {
		mTitleTxt = titleTxt;
		mTitleView.setText(mTitleTxt);
	}

	public void setmTitleBgColor(int color){
		findViewById(R.id.main_title_layout).setBackgroundColor(color);
	}

	public Drawable getLeftIcon() {
		return mLeftIcon;
	}

	public void setLeftIcon(Drawable leftIcon) {
		mLeftIcon = leftIcon;
		mLeftView = (ImageView) findViewById(R.id.titlebar_left_img);
		mLeftView.setVisibility(VISIBLE);
		mLeftView.setImageDrawable(leftIcon);
		mLeftView.setOnClickListener(this);
	}

	public Drawable getRightIcon() {
		return mRightIcon;
	}

	public void setRightIcon(Drawable rightIcon) {
		mRightIcon = rightIcon;
		mRightView = (ImageView) findViewById(R.id.titlebar_left_img);
		mRightView.setVisibility(VISIBLE);
		mRightView.setImageDrawable(rightIcon);
		mRightView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(onClickListener != null){
			onClickListener.onClick(v);
		}
	}
}
