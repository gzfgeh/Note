package com.gzfgeh.data;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class OperationSQLiteItem {
	private static final String URI = "content://com.gzfgeh.providers.itemdataprovider/items";
	private Context context; 
	
	public OperationSQLiteItem(Context context) {
		super();
		this.context = context;
	}
	
	public List<ListItemData> getProviderData(int tabNum){
    	List<ListItemData> textItems = new ArrayList<ListItemData>();
    	List<ListItemData> voiceItems = new ArrayList<ListItemData>();
    	List<ListItemData> photoItems = new ArrayList<ListItemData>();
    	List<ListItemData> videoItems = new ArrayList<ListItemData>();
    	
    	ContentResolver resolver = context.getContentResolver();
    	Uri uri = Uri.parse(URI);
    	Cursor cursor = resolver.query(uri, null, null, null, null);
    	if ((null != cursor) && (cursor.getCount() > 0)){
    		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
    			ListItemData data = new ListItemData(
            			cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ID)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT_URI)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.DATE)),
            			cursor.getInt(cursor.getColumnIndexOrThrow(DBOpenHelper.ALARM)),
            			cursor.getLong(cursor.getColumnIndexOrThrow(DBOpenHelper.ENCRYPT)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.RING_NAME)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.RING_DATE)),
            			cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.RING_URI))
            			);
    			
    			if ("音频文件".equals(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT)))){
    				voiceItems.add(data);
    			}else if ("图片文件".equals(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT)))){
    				photoItems.add(data);
    			}else if ("视频文件".equals(cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT)))){
    				videoItems.add(data);
    			}else 
    				textItems.add(data);
    		}
    	}
    	cursor.close();
		switch (tabNum) {
		case 0:
			return textItems;
		case 1:
			return voiceItems;
		case 2:
			return photoItems;
		case 3:
			return videoItems;
		default:
			return textItems;
		}
    }
	
	@SuppressLint("SimpleDateFormat") public void addItemData(String content, String contentUrl,int alarm,
			long entry,String ringName,String ringDate,String ringUri ){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
		Date currentDate = new Date(System.currentTimeMillis());
		String date = format.format(currentDate);
		
		Uri uri = Uri.parse(URI);
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		
		values.put(DBOpenHelper.CONTENT, content);
		values.put(DBOpenHelper.CONTENT_URI, contentUrl);
		values.put(DBOpenHelper.DATE,date);
		values.put(DBOpenHelper.ALARM, alarm);
		values.put(DBOpenHelper.ENCRYPT, entry);
		values.put(DBOpenHelper.RING_NAME, ringName);
		values.put(DBOpenHelper.RING_DATE, ringDate);
		values.put(DBOpenHelper.RING_URI, ringUri);
		
		resolver.insert(uri, values);
	}
	
	public void deleteItemData(int id){
		Uri uri = Uri.parse(URI + "/" + id);
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(uri, null, null);
	}
	
	public void updateItemContent(int id, String content, String content_uri, String date, 
				int alarm, long encryption, String ringName, String ringDate, String ringUri){
		
		Uri uri = Uri.parse(URI + "/" + id);
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		if (content != null)
			values.put(DBOpenHelper.CONTENT, content);
		if (content_uri != null)
			values.put(DBOpenHelper.CONTENT_URI, content_uri);
		if (date != null)
			values.put(DBOpenHelper.DATE, date);
		if (alarm != 0)
			values.put(DBOpenHelper.ALARM, alarm);
		if (encryption != 0)
			values.put(DBOpenHelper.ENCRYPT, encryption);
		if (ringName != null)
			values.put(DBOpenHelper.RING_NAME, ringName);
		if (ringDate != null)
			values.put(DBOpenHelper.RING_DATE, ringDate);
		if (ringUri != null)
			values.put(DBOpenHelper.RING_URI, ringUri);
		
		resolver.update(uri, values, null, null);
	}
	
	public String queryContentUri(int id){
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse(URI + "/" + id);
    	Cursor cursor = resolver.query(uri, null, null, null, null);
    	cursor.moveToFirst();
    	String contentUriString = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.CONTENT_URI));
    	cursor.close();
    	return contentUriString;
	}
}
