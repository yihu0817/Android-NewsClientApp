/**
 * @author viktor
 * @version v1.0
 */
package com.warmtel.news.newstext;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.warmtel.news.R;

public class NewsReaderActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_newstext_layout);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.news_reader_main_layout,
						NewsReaderMainFragment.newInstance()).commit();
	}
}
