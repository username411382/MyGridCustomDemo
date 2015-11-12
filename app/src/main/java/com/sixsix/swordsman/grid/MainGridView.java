package com.sixsix.swordsman.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

public class MainGridView extends Activity {

	private ScrollView sv;
	private LinearLayout ll,LLF;
	private MyGridView myGridView;
	private Thread th = null;
	private GifView gif;
	private List<Map<String,Object>> list;
	private GridItemAdapter adapter;
	private int[] image={R.drawable.griditem1,R.drawable.griditem2,R.drawable.griditem3};
	private int i=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		fillViewsByID();
		init();
	}
	
	private void fillViewsByID(){
		sv = (ScrollView) findViewById(R.id.MainGridViewScroll);
		ll = (LinearLayout) findViewById(R.id.MainGridViewScrollLinear);
		myGridView = (MyGridView) findViewById(R.id.MainGridViewGrid);
		LLF = (LinearLayout) findViewById(R.id.MainGridViewFooterLinear);
		gif = (GifView) findViewById(R.id.MainGridViewFooterGif);
		
		int gifDip = getConversionDip(30);
		gif.setGifImage(R.drawable.folder_list_footer);
		gif.setShowDimension(gifDip, gifDip);
		gif.setGifImageType(GifImageType.COVER);
		
		LayoutParams lp = gif.getLayoutParams();
		lp.width = lp.height = gifDip;
		gif.setLayoutParams(lp);
		
		sv.setOnTouchListener(new OnTouchListener() {
			
			private int lastY = 0;
		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_UP){
					lastY = sv.getScrollY();
					if(lastY == (ll.getHeight() - sv.getHeight())){
						LLF.setVisibility(View.VISIBLE);
						addMoreData();
					}
				}
				return false;
			}
		});
	}
	
	private void init(){
		
		LLF.setVisibility(View.GONE);
		list = new ArrayList<Map<String,Object>>();
		
		th = new Thread(new Runnable(){

			@Override
			public void run() {
				Bundle b = new Bundle();
				try{
					Map<String,Object> map = null;
					for(i=0;i<11;i++){
						map = new HashMap<String,Object>();
						if(i>2){
							map.put("image",image[i%3]);
						}else{
							map.put("image",image[i]);
						}
						map.put("text", "流星雨的季节"+i);
						list.add(map);
					}
					b.putBoolean("grid", true);
				}catch(Exception e){
					
				}finally{
					Message msg = handler.obtainMessage();
					msg.setData(b);
					handler.sendMessage(msg);
				}
			}});
		
		try{
			th.start();
		}catch(Exception e){}
	}
	
	private void bindGrid(){
		adapter = new GridItemAdapter(this, list);
		
		myGridView.setAdapter(adapter);
		
	}
	
	private Handler handler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			try{
				if(msg.getData().containsKey("grid")){
					bindGrid();
				}else if(msg.getData().containsKey("addMoreData")){
					LLF.setVisibility(View.GONE);
					Map<String,Object> map = null;
					for(int j=i;j<i+5;j++){
					map = new HashMap<String,Object>();
					map.put("image",image[j%3]);
					map.put("text", "流星雨的季节"+j);
					list.add(map);
					}
					adapter.notifyDataSetChanged();
				}
			}catch(Exception e){}
		}};
	
	private void addMoreData(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				Bundle b = new Bundle();
				try{
					Thread.sleep(1000);
					b.putBoolean("addMoreData", true);
				}catch(Exception e){}finally{
					Message msg = handler.obtainMessage();
					msg.setData(b);
					handler.sendMessage(msg);
				}
			}}).start();
	}
	
	public int getConversionDip(float dpValue) {
		final float scale = this.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
