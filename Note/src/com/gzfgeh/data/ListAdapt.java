package com.gzfgeh.data;

import java.util.List;

import com.gzfgeh.note.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapt extends BaseAdapter {
	@SuppressWarnings("unused")
	private Context context;		//上下文
	private List<ListItemData> data;//item
	private int listViewItem;		//条目ID
	private LayoutInflater layoutInflater;
	
	public ListAdapt(Context context, List<ListItemData> data, int listViewItem) {
		super();
		this.context = context;
		this.listViewItem = listViewItem;
		this.data = data;
		layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHold viewHold;
		
		if (convertView == null){
			convertView = layoutInflater.inflate(listViewItem, null);
			viewHold = new ViewHold();
			
			viewHold.itemContent = (TextView)convertView.findViewById(R.id.item_content);
			viewHold.itemDate = (TextView)convertView.findViewById(R.id.item_date);
			viewHold.itemId = (TextView)convertView.findViewById(R.id.item_id);
			convertView.setTag(viewHold);
		}else{
			viewHold = (ViewHold)convertView.getTag();
		}
		
		ListItemData itemData = data.get(position);
		viewHold.itemContent.setText(itemData.getContent());
		viewHold.itemDate.setText(String.valueOf(itemData.getDate()));
		viewHold.itemId.setText(String.valueOf(itemData.get_id()));
		//this.notifyDataSetChanged();
		return convertView;
	}

	private final class ViewHold{
		public TextView itemContent;
		public TextView itemDate;
		public TextView itemId;
	}

}
