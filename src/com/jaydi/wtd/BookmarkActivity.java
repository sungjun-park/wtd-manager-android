package com.jaydi.wtd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.appspot.wtd_manager.wtd.model.Link;
import com.jaydi.wtd.adapters.BookmarkAdapter;
import com.jaydi.wtd.adapters.BookmarkAdapter.BookmarkInter;
import com.jaydi.wtd.connection.ResponseHandler;
import com.jaydi.wtd.connection.database.DatabaseInter;

public class BookmarkActivity extends Activity implements BookmarkInter {
	private List<Link> bookmarks;
	private ListView listBookmarks;
	private BookmarkAdapter bookmarkAdapter;

	private boolean editable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bookmark);

		bookmarks = new ArrayList<Link>();
		listBookmarks = (ListView) findViewById(R.id.list_bookmark_bookmarks);
		listBookmarks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Link bookmark = (Link) bookmarkAdapter.getItem(position);
				goToView(bookmark);
			}

		});
		setAdapter();

		loadBookmarks();
	}

	private void setAdapter() {
		bookmarkAdapter = new BookmarkAdapter(this, bookmarks, this, editable);
		listBookmarks.setAdapter(bookmarkAdapter);
	}

	private void loadBookmarks() {
		DatabaseInter.getLinks(this, new ResponseHandler<List<Link>>() {

			@Override
			protected void onResponse(List<Link> res) {
				bookmarks.clear();
				bookmarks.addAll(res);
				bookmarkAdapter.notifyDataSetChanged();
			}

		});
	}

	public void toggleEditable(View view) {
		if (editable)
			editable = false;
		else
			editable = true;
		setAdapter();
	}

	@Override
	public void onDeleteBookmark(Link bookmark) {
		DatabaseInter.deleteLink(this, new ResponseHandler<Void>() {

			@Override
			protected void onResponse(Void res) {
				loadBookmarks();
			}

		}, bookmark.getUrl());
	}

	protected void goToView(Link bookmark) {
		Intent intent = new Intent(this, WebViewActivity.class);
		intent.putExtra(WebViewActivity.EXTRA_WEB_URL, bookmark.getUrl());
		startActivity(intent);
	}

}
