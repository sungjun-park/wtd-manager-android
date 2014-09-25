package com.jaydi.wtd.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("intent://")) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					if (isAvailable(getActivity(), intent))
						startActivity(intent);
					return true;
				} else
					return false;
			}

			public boolean isAvailable(Context ctx, Intent intent) {
				final PackageManager mgr = ctx.getPackageManager();
				List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
				return list.size() > 0;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				rootView.findViewById(R.id.progressbar_link_loading).setVisibility(View.GONE);
			}

		});
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				return (event.getAction() == MotionEvent.ACTION_MOVE);
			}

		});
		view.loadUrl(link.getUrl());

		return rootView;
	}
}
