package com.gzfgeh.note;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.gzfgeh.swipemenulistview.SwipeMenu;
import com.gzfgeh.swipemenulistview.SwipeMenuCreator;
import com.gzfgeh.swipemenulistview.SwipeMenuItem;
import com.gzfgeh.swipemenulistview.SwipeMenuListView;
import com.gzfgeh.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.gzfgeh.data.ListAdapt;
import com.gzfgeh.data.ListItemData;
import com.gzfgeh.data.OperationSQLiteItem;
import com.gzfgeh.dialog.DateTimeSelectorDialogBuilder;
import com.gzfgeh.dialog.DateTimeSelectorDialogBuilder.OnSureClickListener;
import com.gzfgeh.service.SharedPreferencesData;
import com.gzfgeh.service.help;
import com.gzfgeh.wheelview.TimeSelectWheelView;
import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ClickableViewAccessibility") public class Display extends FragmentActivity {
	//private static final String TAG = "Display";
	public static final String NOTE = "note";
	@SuppressWarnings("unused")
	private final static String ALBUM_PATH=Environment.getExternalStorageDirectory()+File.separator+NOTE+File.separator;
	
	private static final int TAB = 0;
	//private Context context;
	private ImageButton titleSetView;
	@SuppressWarnings("unused")
	private ImageButton titleDisplayMenu;
	private TabHost tabs;
	private View tableView;
	private TextView tableName;
	
	private Intent intent;
	private boolean exitFlag = false;
	private Vibrator vibrator;

	private TextView userName;
	private SharedPreferencesData sp;
	private static final int ITEM1 = Menu.FIRST;
	private static final int ITEM2 = Menu.FIRST + 1;
	
	private TextView titleText;
	
	private OperationSQLiteItem operationSQLiteItem;
	private List<ListItemData> data;
	private ListAdapt listAdapt;
	private SwipeMenuListView listView;
	//侧滑  
	private DrawerLayout drawerLayout;
	private DateTimeSelectorDialogBuilder dateTimeSelectorDialogBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu_draw_layout);
		
		initView();
		addEvent();
		
		titleText = (TextView) findViewById(R.id.titlebar_text);
		titleText.setText("文字");
		
		Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
		sp = new SharedPreferencesData(this);
		
		//设置标签页
		tabs= (TabHost) findViewById(R.id.tabhost);
        tabs.setup();
        tabs.setOnTabChangedListener(TabChangeListener);
        
        //tab1
        TabSpec tab = tabs.newTabSpec("text");  
        tab.setIndicator(createTableView("文字",R.drawable.text));
        tab.setContent(R.id.text);    			// 关联控件  
        tabs.addTab(tab);                			// 添加tab1
        
        //tab2
        tab = tabs.newTabSpec("sounds");  
        tab.setIndicator(createTableView("语音",R.drawable.sounds));      	// 设置tab1的名称
        tab.setContent(R.id.sounds);    			// 关联控件  
        tabs.addTab(tab);                			// 添加tab1
        
        //tab3
        tab = tabs.newTabSpec("pic");  
        tab.setIndicator(createTableView("图片",R.drawable.photo));      	// 设置tab1的名称
        tab.setContent(R.id.pic);    			// 关联控件  
        tabs.addTab(tab);                			// 添加tab1
        
        //tab4
        tab = tabs.newTabSpec("movie");  
        tab.setIndicator(createTableView("视频",R.drawable.movie));      	// 设置tab1的名称
        tab.setContent(R.id.movie);    			// 关联控件  
        tabs.addTab(tab);                			// 添加tab1
        
        tabs.setCurrentTab(0);
        
        //设置侧滑初始化数值
        userName = (TextView) findViewById(R.id.left_menu_name);
        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("user_name"));
        
        listView = (SwipeMenuListView) findViewById(R.id.textlistview);
        operationSQLiteItem = new OperationSQLiteItem(this);
        data = operationSQLiteItem.getProviderData(TAB);
        listAdapt = new ListAdapt(this, data, R.layout.list_item);
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				TextView itemId = (TextView) view.findViewById(R.id.item_id);
				int num = Integer.valueOf(itemId.getText().toString());
				Intent intent = new Intent(Display.this, RecordText.class);
				intent.putExtra("ItemID", num);
				startActivity(intent);
			}
        	
        });
        listView.setAdapter(listAdapt);
      
        SwipeMenuCreator creator = new SwipeMenuCreator() {
    		
    		@Override
    		public void create(SwipeMenu menu) {
    			// TODO Auto-generated method stub
    			SwipeMenuItem alarmItem = new SwipeMenuItem(getApplicationContext());
    			alarmItem.setIcon(R.drawable.alarm_item);
    			alarmItem.setBackground(new ColorDrawable(Color.WHITE));
    			alarmItem.setWidth(240);
    			menu.addMenuItem(alarmItem);
    			
    			SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
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
						dateTimeSelectorDialogBuilder = new DateTimeSelectorDialogBuilder(Display.this, R.style.custom_dialog);
					
					dateTimeSelectorDialogBuilder.show();
					dateTimeSelectorDialogBuilder.setOnSureClickListener(new OnSureClickListener() {
						
						@Override
						public void setOnSureClickListener(TimeSelectWheelView wheelView) {
							// TODO Auto-generated method stub
							Toast.makeText(Display.this, wheelView.getSelectTime(), Toast.LENGTH_SHORT).show();
						}
					});
					break;
				case 1:
					deleteItem(listView, position);
					break;
				default:
					break;
				}
				return true;
			}
		});

        operationSQLiteItem = new OperationSQLiteItem(this);
        data = operationSQLiteItem.getProviderData(TAB);
        listAdapt = new ListAdapt(this,data,R.layout.list_item);
        listView.setAdapter(listAdapt);
        
        registerForContextMenu(listView);		//显示菜单
        
        //title bar 的效果
        titleSetView = (ImageButton) findViewById(R.id.left_btn);
        Animation titleSetAnim = AnimationUtils.loadAnimation(this, R.anim.all_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        titleSetAnim.setInterpolator(lin);
        titleSetView.startAnimation(titleSetAnim);
        
        titleDisplayMenu = (ImageButton) findViewById(R.id.right_btn);
	}

	private void updateTab(TabHost tabHost) {
		// TODO Auto-generated method stub
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
            View v = tabHost.getTabWidget().getChildAt(i);
            if (tabHost.getCurrentTab() == i) 
            	v.setBackgroundResource(R.drawable.background);
            else 
            	v.setBackgroundResource(R.drawable.title_background);
		}
	}

	@SuppressLint({ "NewApi", "InflateParams" }) private View createTableView(String string, int layout) {
		// TODO Auto-generated method stub
		tableView = (View) getLayoutInflater().inflate(R.layout.table, null);
		tableName = (TextView)tableView.findViewById(R.id.table_name);
		tableName.setText(string);
		ImageView image = (ImageView)tableView.findViewById(R.id.table_image);
		image.setBackground(getResources().getDrawable(layout));
		return tableView;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (keyCode == KeyEvent.KEYCODE_BACK){
				exitBy2Click();      //调用双击退出函数 
				return true;
			}
		}
		return false;
	}

	private void exitBy2Click() {
		// TODO Auto-generated method stub
		Timer timer = null;
		if (exitFlag == false){
			exitFlag = true;
			Toast.makeText(this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					exitFlag = false;
				}
			}, 1500);
		}else {
			intent = new Intent();
			intent.setClass(Display.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
	
	
	private OnTabChangeListener TabChangeListener  = new OnTabChangeListener() {
        @Override
        public void onTabChanged(String tabId) {
    		updateTab(tabs);
            switch (tabs.getCurrentTab()) {
			case TAB:
				titleText.setText("文字");
				break;
			case TAB + 1:
				titleText.setText("语音");
				break;
			case TAB + 2:
				titleText.setText("图片");
				break;
			case TAB + 3:
				titleText.setText("视频");
				break;
			default:
				titleText.setText("文字");
				break;
			}
        }
	};
	
	public void onClickTitleBtn(View v){
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(new long[]{1,100,0,0}, -1);
		
		switch (v.getId()) {
		case R.id.right_btn:
			//btnOpenRightMenu();
			switch (tabs.getCurrentTab()) {
			case TAB:
				intent = new Intent(Display.this,RecordText.class);
				break;
			case TAB + 1:
				intent = new Intent(Display.this,RecordVoice.class);
				break;
			case TAB + 2:
				intent = new Intent(Display.this,RecordPicture.class);
				break;
			case TAB + 3:
				intent = new Intent(Display.this,RecordVideos.class);
				break;
			default:
				break;
			}
			startActivity(intent);
			break;
			
		case R.id.left_btn:
			btnOpenLeftMenu();
			break;
			
		case R.id.about:
			
			
			break;
			
		default:
			break;
		}
		
	}
	
	public void onMenu(View v){
		switch (v.getId()) {
		case R.id.set_username:
			sp.clearAll();
			finish();
			break;
			
		case R.id.about:
			intent = new Intent(Display.this,help.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}
    
    
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.setHeaderTitle("请选择操作");
		menu.add(0, ITEM1, 0, "添加闹钟");  
        menu.add(0, ITEM2, 0, "删除"); 
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		@SuppressWarnings("unused")
		int id = (int) info.position;
		
		switch (item.getItemId()) {
		case ITEM1:
			AlertDialog dialog = new AlertDialog.Builder(this)
			.setTitle("是否删除")
			.setPositiveButton("是", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			})
			.setNegativeButton("否", null)
			.create();
			dialog.show();
			break;
		case ITEM2:
			id = 1;
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
    
	void deleteItem(SwipeMenuListView swipedItemDelete, int position){
		View view = swipedItemDelete.getChildAt(position - swipedItemDelete.getFirstVisiblePosition());
		TextView itemId = (TextView) view.findViewById(R.id.item_id);
		int num = Integer.valueOf(itemId.getText().toString());
		operationSQLiteItem.deleteItemData(num);
		String filePath = operationSQLiteItem.queryContentUri(num);
		File file = new File(filePath);
		if (file.exists())
			file.delete();
		ReflashListView();
	}
	
    void ReflashListView(){
    	data = operationSQLiteItem.getProviderData(tabs.getCurrentTab());
    	listAdapt = new ListAdapt(this,data,R.layout.list_item);
    	listView.setAdapter(listAdapt);
    }
    
	private void addEvent() {
		// TODO Auto-generated method stub
		drawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int newState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				View content = drawerLayout.getChildAt(0);
				View menu = drawerView;
				float scale = 1 - slideOffset;  
                float rightScale = 0.8f + scale * 0.2f;  
  
                if (drawerView.getTag().equals("LEFT")){  
  
                    float leftScale = 1 - 0.3f * scale;  
  
                    ViewHelper.setScaleX(menu, leftScale);  
                    ViewHelper.setScaleY(menu, leftScale);  
                    ViewHelper.setAlpha(menu, 0.6f + 0.4f * (1 - scale));  
                    ViewHelper.setTranslationX(content,  
                    		menu.getMeasuredWidth() * (1 - scale));  
                    ViewHelper.setPivotX(content, 0);  
                    ViewHelper.setPivotY(content,  
                    		content.getMeasuredHeight() / 2);  
                    content.invalidate();  
                    ViewHelper.setScaleX(content, rightScale);  
                    ViewHelper.setScaleY(content, rightScale);  
                } else {  
                    ViewHelper.setTranslationX(content,  
                            -menu.getMeasuredWidth() * slideOffset);  
                    ViewHelper.setPivotX(content, content.getMeasuredWidth());  
                    ViewHelper.setPivotY(content,  
                    		content.getMeasuredHeight() / 2);  
                    content.invalidate();  
                    ViewHelper.setScaleX(content, rightScale);  
                    ViewHelper.setScaleY(content, rightScale);  
                }
				
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
				drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.RIGHT);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout_id);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.RIGHT);	//右侧菜单 只有编程才能划出，不能滑动出现
	}
	
	void btnOpenRightMenu(){
		drawerLayout.openDrawer(Gravity.RIGHT);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.RIGHT);
	}
	
	void btnOpenLeftMenu(){
		drawerLayout.openDrawer(Gravity.LEFT);
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.LEFT);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		ReflashListView();
	}
	
	
	    
}
