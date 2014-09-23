package com.jaydi.wtd.connection.database;

import java.io.IOException;

import android.content.Context;
import android.os.Handler;

import com.appspot.wtd_manager.wtd.model.Link;
import com.jaydi.wtd.threading.ThreadManager;
import com.jaydi.wtd.threading.Work;

public class DatabaseInter {

	public static <T> void addLink(final Context context, final Link link) {
		ThreadManager.execute(new Work<T>() {

			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				dbHandler.addLink(link);
				return null;
			}

		}, null);
	}

	public static <T> void getLinks(final Context context, Handler handler) {
		ThreadManager.execute(new Work<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				return (T) dbHandler.getLinks();
			}

		}, handler);
	}

	public static <T> void deleteLink(final Context context, Handler handler, final String url) {
		ThreadManager.execute(new Work<T>() {

			@Override
			public T work() throws IOException {
				DatabaseHandler dbHandler = new DatabaseHandler(context);
				dbHandler.deleteLink(url);
				return null;
			}

		}, handler);
	}

}
