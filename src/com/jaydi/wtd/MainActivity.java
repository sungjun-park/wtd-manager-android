package com.jaydi.wtd;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appspot.wtd_manager.wtd.model.Code;
import com.appspot.wtd_manager.wtd.model.Link;
import com.appspot.wtd_manager.wtd.model.LinkCol;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.jaydi.wtd.adapters.LinkPagerAdapter;
import com.jaydi.wtd.connection.ResponseHandler;
import com.jaydi.wtd.connection.database.DatabaseInter;
import com.jaydi.wtd.connection.network.NetworkInter;
import com.jaydi.wtd.constants.Codes;
import com.jaydi.wtd.fragments.CodeSelectDialog;
import com.jaydi.wtd.utils.DialogUtils;
import com.jaydi.wtd.utils.ResourceUtils;
import com.jaydi.wtd.utils.ToastUtils;

public class MainActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener {
	private boolean lcAvailable;
	private LocationClient locationClient;

	private List<Link> links;
	private ViewPager linkPager;
	private LinkPagerAdapter linkPagerAdapter;

	private int locCode = 0;
	private int catACode = 0;
	private int catBCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		links = new ArrayList<Link>();
		linkPager = (ViewPager) findViewById(R.id.pager_main_links);
		linkPagerAdapter = new LinkPagerAdapter(getSupportFragmentManager(), links);
		linkPager.setAdapter(linkPagerAdapter);

		lcAvailable = servicesConnected();
		initLocation();
	}

	private boolean servicesConnected() {
		// check if google play service is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		if (ConnectionResult.SUCCESS == resultCode)
			return true;
		else
			return false;
	}

	private void initLocation() {
		Log.i("LOC", "INIT");
		if (lcAvailable) {
			locationClient = new LocationClient(this, this, this);
			locationClient.connect();
		} else
			getLinks(0, 0);
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i("LOC", "CON");
		loadLinks(null);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i("LOC", "CON FAIL");
		locationClient.connect();
	}

	@Override
	public void onDisconnected() {
	}

	public void loadLinks(View view) {
		Location location = locationClient.getLastLocation();
		if (location != null)
			getLinks(location.getLatitude(), location.getLongitude());
		else
			getLinks(0, 0);
	}

	private void getLinks(double lat, double lng) {
		Log.i("LINKS", "send request");
		NetworkInter.getLinks(new ResponseHandler<LinkCol>(DialogUtils.getWaitingDialog(this.getSupportFragmentManager())) {

			@Override
			protected void onResponse(LinkCol res) {
				Log.i("LINKS", "get response");
				if (res != null && res.getLinks() != null)
					refresh(res);
			}

		}, lat, lng, locCode, catACode, catBCode);
	}

	private void refresh(LinkCol linkCol) {
		Log.i("LINKS", "link count: " + linkCol.getLinks().size());
		locCode = linkCol.getLocCode();
		catACode = linkCol.getCatACode();
		catBCode = linkCol.getCatBCode();
		refreshMenus();

		links.clear();
		links.addAll(linkCol.getLinks());
		refreshLinks();
	}

	private void refreshMenus() {
		Button locMenu = (Button) findViewById(R.id.button_main_loc);
		Button catAMenu = (Button) findViewById(R.id.button_main_cat_a);

		locMenu.setText(Codes.getLocCodeString(locCode));
		catAMenu.setText(Codes.getCatACodeString(catACode));
	}

	private void refreshLinks() {
		linkPagerAdapter.notifyDataSetChanged();
	}

	public void setLoc(View view) {
		DialogUtils.showCodeSelectDialog(getSupportFragmentManager(), Codes.getLocCodes(), new CodeSelectDialog.CodeSelectInter() {

			@Override
			public void onCodeSelect(Code code) {
				locCode = code.getCode().intValue();
				refreshMenus();
			}
		});
	}

	public void setCatA(View view) {
		DialogUtils.showCodeSelectDialog(getSupportFragmentManager(), Codes.getCatACodes(), new CodeSelectDialog.CodeSelectInter() {

			@Override
			public void onCodeSelect(Code code) {
				catACode = code.getCode().intValue();
				refreshMenus();
			}
		});
	}

	public void bookmark(View view) {
		Link link = linkPagerAdapter.getCurrentLink();
		DatabaseInter.addLink(this, link);
		ToastUtils.show(ResourceUtils.getString(R.string.bookmarked));
	}

	public void goToBookmark(View view) {
		Intent intent = new Intent(this, BookmarkActivity.class);
		startActivity(intent);
	}

}
