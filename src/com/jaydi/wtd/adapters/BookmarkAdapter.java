package com.jaydi.wtd.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.appspot.wtd_manager.wtd.model.Link;
import com.jaydi.wtd.R;

public class BookmarkAdapter extends BaseAdapter {
	private Context context;
	private List<Link> bookmarks;
	private BookmarkInter inter;
	private boolean editable;

	public interface BookmarkInter {
		public abstract void onDeleteBookmark(Link bookmark);
	}

	public BookmarkAdapter(Context context, List<Link> bookmarks, BookmarkInter inter, boolean editable) {
		super();
		this.context = context;
		this.bookmarks = bookmarks;
		this.inter = inter;
		this.editable = editable;
	}

	@Override
	public int getCount() {
		return bookmarks.size();
	}

	@Override
	public Object getItem(int position) {
		return bookmarks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.valueOf(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Link bookmark = (Link) getItem(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.adapted_bookmark_layout, parent, false);

		TextView title = (TextView) view.findViewById(R.id.text_adapted_bookmark_title);
		title.setText(bookmark.getTitle());

		View delete = view.findViewById(R.id.image_adapted_bookmark_delete);
		if (editable) {
			delete.setVisibility(View.VISIBLE);
			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					inter.onDeleteBookmark(bookmark);
				}

			});
		} else {
			delete.setVisibility(View.GONE);
		}

		return view;
	}

}
