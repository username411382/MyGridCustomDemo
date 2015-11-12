package com.sixsix.swordsman.grid;

import java.util.List;
import java.util.Map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridItemAdapter extends BaseAdapter {
	
	private Context con;
	private List<Map<String,Object>> list;
	
	public GridItemAdapter(Context context,List<Map<String,Object>> list){
		this.con = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(position >= getCount())
		{
			return null;
		}
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		viewHolder vh = null;
		if(convertView == null){
			vh = new viewHolder();
			convertView = LayoutInflater.from(con).inflate(R.layout.adapter_grid_item, null);
			
			vh.iv = (ImageView) convertView.findViewById(R.id.AdapterGridItemImage);
			vh.tv = (TextView) convertView.findViewById(R.id.AdapterGridItemText);
			
			convertView.setTag(vh);
		}else{
			vh = (viewHolder) convertView.getTag();
		}
		
		Map<String,Object> map = list.get(position);
		vh.iv.setImageResource(Integer.parseInt(map.get("image").toString()));
		vh.tv.setText(map.get("text").toString());
		
		return convertView;
	}
	
	public class viewHolder{
		public ImageView iv;
		public TextView tv;
	}

}
