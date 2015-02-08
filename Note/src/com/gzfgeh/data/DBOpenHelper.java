package com.gzfgeh.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final int DB_VERSION = 10;				//�汾��
	private static final String DB_NAME = "NoteText.db";	//���ݿ�����
	public static final String TABLE_NAME = "items";		//���ݿ�������
	public static final String ID = "_id";					//��������������
	public static final String CONTENT = "content";			//����˵��
	public static final String CONTENT_URI = "content_uri";	//������ַ
	public static final String DATE = "date";				//�������
	public static final String ALARM = "alarm";				//�Ƿ������
	public static final String RING_DATE = "ring_date";		//����ʱ��
	public static final String RING_NAME = "ring_name";		//��������
	public static final String RING_URI = "ring_uri";		//���ӵ�ַ
	public static final String ENCRYPT = "encrypt";			//�����ַ�
	
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
