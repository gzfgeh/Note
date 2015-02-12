package com.gzfgeh.service;

import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.dialog.DateTimeSelectorDialogBuilder;
import com.gzfgeh.dialog.DateTimeSelectorDialogBuilder.OnSureClickListener;
import com.gzfgeh.note.R;
import com.gzfgeh.swipemenulistview.SwipeMenu;
import com.gzfgeh.swipemenulistview.SwipeMenuCreator;
import com.gzfgeh.swipemenulistview.SwipeMenuItem;
import com.gzfgeh.swipemenulistview.SwipeMenuListView;
import com.gzfgeh.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.gzfgeh.wheelview.TimeSelectWheelView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Toast;

public class ListViewItemMenu {
	@SuppressWarnings("unused")
	private Context context;
	@SuppressWarnings("unused")
	private SwipeMenuListView listView;
	private OperationSQLiteItem operationSQLiteItem;
	private DateTimeSelectorDialogBuilder dateTimeSelectorDialogBuilder;

	public ListViewItemMenu(final Context context, final SwipeMenuListView listView, final int tabNum) {
		this.listView = listView;
		operationSQLiteItem = new OperationSQLiteItem(context);
		SwipeMenuCreator creator = new SwipeMenuCreator() {
		    		
		    		@Override
		    		public void create(SwipeMenu menu) {
		    			// TODO Auto-generated method stub
		    			SwipeMenuItem alarmItem = new SwipeMenuItem(context);
		    			alarmItem.setIcon(R.drawable.alarm_item);
		    			alarmItem.setBackground(new ColorDrawable(Color.WHITE));
		    			alarmItem.setWidth(240);
		    			menu.addMenuItem(alarmItem);
		    			
		    			SwipeMenuItem deleteItem = new SwipeMenuItem(context);
		    			deleteItem.setIcon(R.drawable.delete_item);
		    			deleteItem.setBackground(new ColorDrawable(Color.RED));
		    			deleteItem.setWidth(240);
		    			menu.addMenuItem(deleteItem);
		    		}
		    	};
		    	listView.setMenuCreator(creator);
		    	listView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
						// TODO Auto-generated method stub
						switch (index) {
						case 0:
							if (null == dateTimeSelectorDialogBuilder)
								dateTimeSelectorDialogBuilder = new DateTimeSelectorDialogBuilder(context, R.style.custom_dialog);
							
							dateTimeSelectorDialogBuilder.show();
							dateTimeSelectorDialogBuilder.setOnSureClickListener(new OnSureClickListener() {
								
								@Override
								public void setOnSureClickListener(TimeSelectWheelView wheelView) {
									// TODO Auto-generated method stub
									Toast.makeText(context, wheelView.getSelectTime(), Toast.LENGTH_SHORT).show();
								}
							});
							break;
						case 1:
							operationSQLiteItem.deleteItemData(listView, tabNum, position);
							break;
						default:
							break;
						}
						return true;
					}
				});
	}

	public void setListView(SwipeMenuListView listView) {
		this.listView = listView;
	}
	
	
}
