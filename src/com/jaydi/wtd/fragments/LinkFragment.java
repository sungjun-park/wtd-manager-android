package com.jaydi.wtd.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appspot.wtd_manager.wtd.model.Link;
import com.jaydi.wtd.R;

@SuppressLint("SetJavaScriptEnabled")
public class LinkFragment extends Fragment {
	private Link link;

	public void setLink(Link link) {
		this.link = link;
	}

	public Link getLink() {
		return link;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_link, container, false);

		WebView view = (WebView) rootView.findViewById(R.id.web_link_blog);
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
					rootView.findViewById(R.id.progressbar_link_loading).setVisibility(View.GONE);
			}

		});
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}

		});
		view.loadUrl(link.getUrl());

		return rootView;
	}
}
