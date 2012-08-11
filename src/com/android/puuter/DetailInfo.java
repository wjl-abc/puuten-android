package com.android.puuter;

import com.android.puuter.controller.Controller;
import com.android.puuter.controller.Controller.Result;
import com.android.puuter.model.WaterFlow;
import com.android.puuter.model.WbDetail;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DetailInfo extends Activity {

	public static void actionView(Context context, int wbId) {
		Intent i = new Intent(context, DetailInfo.class);
		i.putExtra(EXTRA_WB_ID, wbId);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        
        mTextView = (TextView) findViewById(R.id.wb_detail);
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
		}
	}
    
	private class WbHandler extends Handler {
		private final int RESOURCE_DETAIL_DOWNLOAD = 4;
		public void handleMessage(android.os.Message msg) {
			// 0 means fail
			int status = 0;
			switch (msg.what) {
			case RESOURCE_DETAIL_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					int BsId = mWbDetail.getBsId();
					int wbId = mWbDetail.getWbId();
					String name = mWbDetail.getName();
					String body = mWbDetail.getBody();
					String avatarUrl = mWbDetail.getAvatarUrl();
					String str = BsId + "\n" + wbId + "\n" + name + "\n" + body + "\n" + avatarUrl;
					mTextView.setText(str);
				}else {
					// do nothing
				}
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
	}
    private final static String EXTRA_WB_ID = "wb_id";
	private WbHandler mWbHandler;
	private Controller mController;
	private Result mResultCallback;
	private Context mContext;
	private WbDetail mWbDetail;
	
	private TextView mTextView;
	private int mWbId;
}
