package com.gzfgeh.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 10;				//版本号
	private static final String DB_NAME = "NoteText.db";	//数据库名称
	public static final String TABLE_NAME = "items";		//数据库表的名称
	public static final String ID = "_id";					//表中主键的名称
	public static final String CONTENT = "content";			//文字说明
	public static final String CONTENT_URI = "content_uri";	//附件地址
	public static final String DATE = "date";				//添加日期
	public static final String ALARM = "alarm";				//是否加闹钟
	public static final String RING_DATE = "ring_date";		//闹钟时间
	public static final String RING_NAME = "ring_name";		//闹钟名字
	public static final String RING_URI = "ring_uri";		//闹钟地址
	public static final String ENCRYPT = "encrypt";			//加密字符
	
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID + 
				" integer primary key autoincrement , " + CONTENT + " text, " + CONTENT_URI + 
				" text, " + DATE + " text, " + ALARM + " integer, " + ENCRYPT +
				" long, " + RING_NAME + " text, " + RING_DATE + " text, " + RING_URI + " text);"
				);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
