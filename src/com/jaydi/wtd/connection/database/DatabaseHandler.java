package com.jaydi.wtd.connection.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appspot.wtd_manager.wtd.model.Link;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "LinkDB";

	private static final String TABLE_LINKS = "links";
	private static final String COL_URL = "url";
	private static final String COL_TITLE = "title";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createMessagePinTable = "CREATE TABLE " + TABLE_LINKS + " (" + COL_URL + " TEXT, " + COL_TITLE + " TEXT)";
		db.execSQL(createMessagePinTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINKS);
			onCreate(db);
		}
	}

	public void addLink(Link link) {
		if (link == null)
			return;
		
		deleteLink(link.getUrl());

		ContentValues values = new ContentValues();
		values.put(COL_URL, link.getUrl());
		values.put(COL_TITLE, link.getTitle());

		SQLiteDatabase db = getWritableDatabase();
		db.insert(TABLE_LINKS, null, values);
		db.close();
	}

	public List<Link> getLinks() {
		List<Link> links = new ArrayList<Link>();

		String[] cols = new String[] { COL_URL, COL_TITLE };

		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TABLE_LINKS, cols, null, null, null, null, null);
		if (cursor == null)
			return links;
		if (cursor.isAfterLast())
			return links;

		System.out.println("cursor exists");

		if (cursor.moveToFirst())
			do {
				Link link = new Link();
				link.setUrl(cursor.getString(0));
				link.setTitle(cursor.getString(1));
				links.add(link);
			} while (cursor.moveToNext());

		cursor.close();
		db.close();

		return links;
	}

	public void deleteLink(String url) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_LINKS, COL_URL + " = ?", new String[] { url });
		db.close();
	}

}
