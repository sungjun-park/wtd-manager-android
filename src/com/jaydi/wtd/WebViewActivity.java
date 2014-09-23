package com.jaydi.wtd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {
	public static final String EXTRA_WEB_URL = "wtd.extra.webview.url";
	
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);

		if (savedInstanceState == null)
			url = getIntent().getStringExtra(EXTRA_WEB_URL);

		if (url != null && !url.isEmpty())
			loadWebPage();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void loadWebPage() {
		WebView view = (WebView) findViewById(R.id.web_web_view_page);
		view.getSettings().setJavaScriptEnabled(true);
		view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		view.setWebViewClient(new WebViewClient() {
			private int running = 0;

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				running++;
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				running = Math.max(running, 1);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (--running == 0)
					findViewById(R.id.progressbar_web_view_loading).setVisibility(View.GONE);
			}

		});
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}

		});
		view.loadUrl(url);
	}

}
