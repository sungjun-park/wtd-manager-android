package com.jaydi.wtd.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.appspot.wtd_manager.wtd.model.Link;
import com.jaydi.wtd.fragments.LinkFragment;

public class LinkPagerAdapter extends FragmentStatePagerAdapter {
	private List<Link> links;
	private Link currentLink;

	public LinkPagerAdapter(FragmentManager fm, List<Link> links) {
		super(fm);
		this.links = links;
	}

	public Link getCurrentLink() {
		return currentLink;
	}

	@Override
	public Fragment getItem(int position) {
		currentLink = links.get(position);
		LinkFragment f = new LinkFragment();
		f.setLink(currentLink);
		return f;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public int getCount() {
		return links.size();
	}

}
