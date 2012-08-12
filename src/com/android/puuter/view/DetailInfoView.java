package com.android.puuter.view;

import com.android.puuter.R;
import com.android.puuter.controller.Controller;
import com.android.puuter.controller.Controller.Result;
import com.android.puuter.model.WaterFlow;
import com.android.puuter.model.WbDetail;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class DetailInfoView extends Activity {

	public static void actionView(Context context, int wbId) {
		Intent i = new Intent(context, DetailInfoView.class);
		i.putExtra(EXTRA_WB_ID, wbId);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        
        
        mName = (TextView) findViewById(R.id.bs_name);
        mBody = (TextView) findViewById(R.id.bs_body);
        mIcon = (ImageView) findViewById(R.id.bs_icon);
        Intent i = getIntent();
        mWbId = i.getIntExtra(EXTRA_WB_ID, -1);
        
		mContext = this;
		mController = Controller.getInstance(mContext);
		mResultCallback = new ControllerResults();
		mWbHandler = new WbHandler();
        
		// load resource detail
		new Thread() {
			public void run() {
				loadResourceDetail();
			}
		}.start();
    }
    
	private void loadResourceDetail() {
		mController.loadResourceWbDetail(mWbId, mResultCallback);
	}
    
	private class ControllerResults implements Controller.Result {
		public void loginServerCallBack(Context context, int progress) {
		}

		public void downloadResource(boolean status, WaterFlow waterFlow) {
		}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
			mWbDetail = wbd;
			mWbHandler.downloadResourceWbDetailStatus(status);
			if(status){
				final String avatarUrl = mWbDetail.getAvatarUrl();
				new Thread(){
					public void run(){
						mDrawable = mController.loadImage(avatarUrl);
						boolean status = mDrawable==null ? false : true;
						mWbHandler.downloadResource(status);
					}
				}.start();
			}
		}
	}
    
	private class WbHandler extends Handler {
		private final int IMAGE_DOWNLOAD = 1;
		private final int RESOURCE_DETAIL_DOWNLOAD = 4;
		public void handleMessage(android.os.Message msg) {
			// 0 means fail
			int status = 0;
			switch (msg.what) {
			case IMAGE_DOWNLOAD:
				status = msg.arg1;
				if(status == 1){
					Bitmap bitmapTmp = ((BitmapDrawable) mDrawable).getBitmap();
					mIcon.setImageBitmap(bitmapTmp);
				}else{
					//do nothing
				}
				break;
			case RESOURCE_DETAIL_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					String name = mWbDetail.getName();
					String body = mWbDetail.getBody();
					mName.setText(name);
					mBody.setText(body);
				}else {
					// do nothing
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
		
		public void downloadResourceWbDetailStatus(boolean status){
			int what = RESOURCE_DETAIL_DOWNLOAD;
			if (status == true) {
				android.os.Message msg = android.os.Message.obtain(this, what, 1, 0);
				sendMessage(msg);
			} else {
				android.os.Message msg = android.os.Message.obtain(this, what, 0, 0);
				sendMessage(msg);
			}
		}
		
		public void downloadResource(boolean status){
			int what = IMAGE_DOWNLOAD;
			if (status == true) {
				android.os.Message msg = android.os.Message.obtain(this, what, 1, 0);
				sendMessage(msg);
			} else {
				android.os.Message msg = android.os.Message.obtain(this, what, 0, 0);
				sendMessage(msg);
			}
		}
	}
    private final static String EXTRA_WB_ID = "wb_id";
	private WbHandler mWbHandler;
	private Controller mController;
	private Result mResultCallback;
	private Context mContext;
	private WbDetail mWbDetail;
	
	private TextView mName;
	private TextView mBody;
	private ImageView mIcon;
	private int mWbId;
	private Drawable mDrawable;
}

