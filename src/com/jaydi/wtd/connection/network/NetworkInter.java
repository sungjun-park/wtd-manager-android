package com.jaydi.wtd.connection.network;

import java.io.IOException;

import android.os.Handler;

import com.appspot.wtd_manager.wtd.Wtd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.jaydi.wtd.threading.ThreadManager;
import com.jaydi.wtd.threading.Work;

public class NetworkInter {
	private static Wtd service;

	static {
		Wtd.Builder builder = new Wtd.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		service = builder.build();
	}

	public static <T> void getCodes(Handler handler) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.codes().get().list().execute();
			}

		}, handler);
	}

	public static <T> void getLinks(Handler handler, final double lat, final double lng, final int locCode, final int catACode, final int catBCode) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				return (T) service.links().get().list(catACode, catBCode, lat, lng, locCode).execute();
			}

		}, handler);
	}
}
