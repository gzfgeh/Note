package com.gzfgeh.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gzfgeh.data.DBOpenHelper;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.test.AndroidTestCase;

@SuppressLint("SimpleDateFormat") public class Test extends AndroidTestCase {
		private static final String URI = "content://com.gzfgeh.providers.itemdataprovider/items";
		
		public void testInsert() throws Exception{
			SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
			Date currentDate = new Date(System.currentTimeMillis());
			String date = format.format(currentDate);
			
			Uri uri = Uri.parse(URI);
			ContentResolver resolver = this.getContext().getContentResolver();
			ContentValues values = new ContentValues();
			
			values.put(DBOpenHelper.CONTENT, "1");
			values.put(DBOpenHelper.CONTENT_URI, "");
			values.put(DBOpenHelper.DATE,date);
			values.put(DBOpenHelper.ALARM, 0);
			values.put(DBOpenHelper.ENCRYPT, 1);
			values.put(DBOpenHelper.RING_NAME, "");
			values.put(DBOpenHelper.RING_DATE, "");
			values.put(DBOpenHelper.RING_URI, "");
			
			resolver.insert(uri, values);
		}
		
		public void testDelete() throws Exception{
			///OperationSQLiteItem operationSQLiteItem = new OperationSQLiteItem(this.getContext());
			//operationSQLiteItem.deleteItemData(2);
		}
}
