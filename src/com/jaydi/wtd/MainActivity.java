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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.jaydi.wtd.adapters.LinkPagerAdapter;
import com.jaydi.wtd.connection.ResponseHandler;
import com.jaydi.wtd.connection.database.DatabaseInter;
import com.jaydi.wtd.connection.network.NetworkInter;
import com.jaydi.wtd.constants.Codes;
import com.jaydi.wtd.fragments.CodeSelectDialog;
import com.jaydi.wtd.utils.DialogUtils;
import com.jaydi.wtd.utils.ResourceUtils;
import com.jaydi.wtd.utils.ToastUtils;

public class MainActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	// constants for location updates
	public static final long UPDATE_INTERVAL = 10 * 1000;
	public static final long FASTEST_INTERVAL = 5 * 1000;

	// location service variables
	private boolean gsAvailable;
	private LocationClient locationClient;
	private Location location;
	private boolean unsureLocation = false;

	// ui contents variables
	private List<Link> links;
	private ViewPager linkPager;
	private LinkPagerAdapter linkPagerAdapter;

	// request codes, initially 0
	private int locCode = 0;
	private int catACode = 0;
	private int catBCode = 0;

	// activity on screen
	private boolean onScreen;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ready link list and view pager
		links = new ArrayList<Link>();
		linkPager = (ViewPager) findViewById(R.id.pager_main_links);
		linkPagerAdapter = new LinkPagerAdapter(getSupportFragmentManager(), links);
		linkPager.setAdapter(linkPagerAdapter);

		// check if google service is available
		gsAvailable = servicesConnected();
		// init location tracking
		initLocation();
	}

	@Override
	public void onResume() {
		super.onResume();
		onScreen = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		onScreen = false;
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
		if (gsAvailable) { // if google service available start fuse location service
			locationClient = new LocationClient(this, this, this);
			locationClient.connect();
		} else
			// else load links on temporary location
			getLinks(0, 0);
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.i("LOC", "CON");
		// create location request object
		LocationRequest locationRequest = LocationRequest.create();
		// use power accuracy balanced priority
		locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		// set interval
		locationRequest.setInterval(UPDATE_INTERVAL);
		// set fastest interval
		locationRequest.setFastestInterval(FASTEST_INTERVAL);
		locationClient.requestLocationUpdates(locationRequest, this);
		// load new contents links from server
		loadLinks(null);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i("LOC", "CON FAIL");
		// retry
		locationClient.connect();
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public void onLocationChanged(Location location) {
		// in here gets location updates
		this.location = location;
		// if link loaded on temporary location before, reload links
		if (unsureLocation && onScreen)
			loadLinks(null);
	}

	public Location getLastLocation() {
		// if updated location exists return it
		if (location != null)
			return location;
		// else get new last location
		else
			return locationClient.getLastLocation();
	}

	public void loadLinks(View view) {
		// get current location
		Location location = getLastLocation();
		if (location != null)
			getLinks(location.getLatitude(), location.getLongitude());
		else { // if location null, get links on temporary location and report it
			getLinks(0, 0);
			unsureLocation = true;
		}
	}

	private void getLinks(double lat, double lng) {
		Log.i("LINKS", "send request");
		// get contents links from server
		NetworkInter.getLinks(new ResponseHandler<LinkCol>(DialogUtils.getWaitingDialog(this.getSupportFragmentManager())) {

			@Override
			protected void onResponse(LinkCol res) {
				Log.i("LINKS", "get response");
				// result null check, refresh contents view
				if (res != null && res.getLinks() != null)
					refresh(res);
			}

		}, lat, lng, locCode, catACode, catBCode);
	}

	private void refresh(LinkCol linkCol) {
		Log.i("LINKS", "link count: " + linkCol.getLinks().size());
		// save code values
		locCode = linkCol.getLocCode();
		catACode = linkCol.getCatACode();
		catBCode = linkCol.getCatBCode();
		// refresh top menu
		refreshMenus();

		// refresh links
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
		// show location code select dialog
		DialogUtils.showCodeSelectDialog(getSupportFragmentManager(), Codes.getLocCodes(), new CodeSelectDialog.CodeSelectInter() {

			@Override
			public void onCodeSelect(Code code) {
				// save code value
				locCode = code.getCode().intValue();
				// refresh top menu
				refreshMenus();
				// reload contents links
				loadLinks(null);
			}
		});
	}

	public void setCatA(View view) {
		DialogUtils.showCodeSelectDialog(getSupportFragmentManager(), Codes.getCatACodes(), new CodeSelectDialog.CodeSelectInter() {

			@Override
			public void onCodeSelect(Code code) {
				// save code value
				catACode = code.getCode().intValue();
				// refresh top menu
				refreshMenus();
				// reload contents links
				loadLinks(null);
			}
		});
	}

	public void bookmark(View view) {
		// get current view's contents link
		Link link = links.get(linkPager.getCurrentItem());
		// save link
		DatabaseInter.addLink(this, link);
		// toast bookmark saved
		ToastUtils.show(ResourceUtils.getString(R.string.bookmarked));
	}

	public void goToBookmark(View view) {
		Intent intent = new Intent(this, BookmarkActivity.class);
		startActivity(intent);
	}

}
