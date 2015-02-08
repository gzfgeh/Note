package com.gzfgeh.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ItemDataProvider extends ContentProvider {
	private DBOpenHelper dbOpenHelper;
	
	private static final int ITEM = 1;
	private static final int ITEMS = 2;
	private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static{
		uriMatcher.addURI("com.gzfgeh.providers.itemdataprovider", "items", ITEMS);
		uriMatcher.addURI("com.gzfgeh.providers.itemdataprovider", "items/#", ITEM);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int result;
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			result = db.delete(DBOpenHelper.TABLE_NAME, selection, selectionArgs);
			break;
			
		case ITEM:
			long id = ContentUris.parseId(uri);
			String where = DBOpenHelper.ID + "=" + id;
			if (selection != null && !"".equals(selection.trim())){
				where += " and " + selection;
			}
			result = db.delete(DBOpenHelper.TABLE_NAME, where, selectionArgs);
			break;
		default:
			 throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
		}
		return result;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		String result;
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			result = "com.gzfgeh.providers.itemdataprovider" + "/" + "items";
			break;
			
		case ITEM:
			result = "com.gzfgeh.providers.itemdataprovider" + "/" + "items/#";
			break;
		default:
			 throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
		}
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		long id;
		switch (uriMatcher.match(uri)) {
		case ITEMS:
			id = db.insert(DBOpenHelper.TABLE_NAME, DBOpenHelper.ID, values);
			return ContentUris.withAppendedId(uri, id);
			
		case ITEM:
			id = db.insert(DBOpenHelper.TABLE_NAME, DBOpenHelper.ID, values);
			String uriPath = uri.toString();
			String path = uriPath.substring(0,uriPath.lastIndexOf("/")) + id;
			return Uri.parse(path);
		default:
			 throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
		}
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbOpenHelper = new DBOpenHelper(this.getContext());
		dbOpenHelper.getReadableDatabase();
		dbOpenHelper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
        case ITEMS:
                 return db.query(DBOpenHelper.TABLE_NAME, projection, selection, selectionArgs,
                                    null, null, sortOrder);
        case ITEM:
                 long id = ContentUris.parseId(uri);
                 String where = "_id=" + id;
                 if (selection != null && !"".equals(selection)) {
                          where = where + " and " + selection;
                 }
                 return db.query(DBOpenHelper.TABLE_NAME, projection, where, selectionArgs, null,
                                    null, sortOrder);
        default:
                 throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
        }
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        int result;
        switch (uriMatcher.match(uri)) {
        case ITEMS:
                 result = db.update(selection, values, selection, selectionArgs);
                 break;
        case ITEM:
                 long id = ContentUris.parseId(uri);
                 String where = "_id=" + id;
                 if (selection != null && !"".equals(selection)) {
                          where = where + " and " + selection;
                 }
                 result = db.update(DBOpenHelper.TABLE_NAME, values, where, selectionArgs);
                 break;
        default:
                 throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
        }
        return result;
	}
}
