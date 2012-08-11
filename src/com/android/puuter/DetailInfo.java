package com.android.puuter;

import com.android.puuter.controller.Controller;
import com.android.puuter.controller.Controller.Result;
import com.android.puuter.model.WaterFlow;
import com.android.puuter.model.WbDetail;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class DetailInfo extends Activity {

	public static void actionView(Context context, int wbId) {
		Intent i = new Intent(context, TabActivity.class);
		i.putExtra(EXTRA_WB_ID, wbId);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
    }
    
	private class ControllerResults implements Controller.Result {
		public void loginServerCallBack(Context context, int progress) {
		}

		public void downloadResource(boolean status, WaterFlow waterFlow) {
		}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
		}
	}
    
	private class WbHandler extends Handler {
		private final int RESOURCE_DOWNLOAD = 1;
		public void handleMessage(android.os.Message msg) {
			// 0 means fail
			int status = 0;
			switch (msg.what) {
			default:
				super.handleMessage(msg);
			}
		}
	}
    
    private final static String EXTRA_WB_ID = "wb_id";
	private WbHandler mWbHandler;
	private Controller mController;
	private Result mResultCallback;
	private Context mContext;
}
