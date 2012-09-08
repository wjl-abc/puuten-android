package com.android.puuter.view;

import com.android.puuter.R;
import com.android.puuter.controller.Controller;
import com.android.puuter.model.WbDetail;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WBDetailInfo extends Activity {

	public static void actionView(Context context, int wbId) {
		Intent i = new Intent(context, WBDetailInfo.class);
		i.putExtra(EXTRA_WB_ID, wbId);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_info);
        
        Intent i = getIntent();
        mWbId = i.getIntExtra(EXTRA_WB_ID, -1);
        
		mBusinessPic = (ImageView) findViewById(R.id.BusinessPic);
		mWbPosterIcon = (ImageView) findViewById(R.id.WbPosterIcon);
		mStarIcon = (ImageView) findViewById(R.id.StarIcon);
		mTextInfo = (TextView) findViewById(R.id.textInfo);
		mPosterName = (TextView) findViewById(R.id.PosterName);
		mHorizontalLine = (TextView) findViewById(R.id.horizontalLine);
		
		mContext = this;
		mController = Controller.getInstance(mContext);
		mResultCallback = new ControllerResults();
		mWbHandler = new WbHandler();
		
		mStarIcon.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				switch(mFlag){
				case 0:
					mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[1]));
					mFlag = 1;
					break;
				case 1:
					mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[0]));
					mFlag = 0;
					break;
				case 2:
					mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[3]));
					mFlag = 3;
					break;
				case 3:
					mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[2]));
					mFlag = 2;
					break;
				}
			}});
		
		// load resource detail
		new Thread() {
			public void run() {
				loadResourceDetail();
			}
		}.start();
    }
    
    
	private void loadResourceDetail() {
		if(mWbId<0){
			Log.v(TAG, "weibo id:" + mWbId + " , load resource fail");
			return;
		}
		mController.loadResourceWbDetail(mWbId, mResultCallback);
	}
    
	private class ControllerResults implements Controller.Result {
		public void loginServerCallBack(Context context, int progress) {
		}

		public void downloadResource(boolean status, Object data) {
		}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
			mWbDetail = wbd;
			mWbHandler.downloadResourceWbDetailStatus(status);
			if(status){
				final String avatarUrl = mWbDetail.getAvatarUrl();
				new Thread(){
					public void run(){
						Drawable drawable = mController.loadImage(avatarUrl);
						boolean status = drawable==null ? false : true;
						mWbHandler.downloadResource(status, 0, drawable);
					}
				}.start();
				
				final String bsUrl = mWbDetail.getBsUrl();
				new Thread(){
					public void run(){
						Drawable drawable = mController.loadImage(bsUrl);
						boolean status = drawable==null ? false : true;
						mWbHandler.downloadResource(status, 1, drawable);
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
					int imageFlag = msg.arg2;
					BitmapDrawable drawable = (BitmapDrawable)msg.obj;
					Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
					if(imageFlag == 1){
						int size[] = new int[2];
						int type = ComputeSize(mWbDetail.getRatio(), size);
						float scale = size[1] / ((float) bitmap.getWidth());
						Matrix matrix = new Matrix();
						matrix.postScale(scale, scale);
						Bitmap bitmapTmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
						bitmap.recycle();
//						LayoutParams lp = new LayoutParams(size[0], size[1]);
//						mBusinessPic.setLayoutParams(lp);
						mBusinessPic.setImageBitmap(bitmapTmp);
						if(type == 2){
							mFlag = 2;
							mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[2]));
							mTextInfo.setTextColor(0xffffffff);
						}else if(type == 3){
							mFlag = 2;
							mStarIcon.setImageDrawable(getResources().getDrawable(mStarRes[2]));
							mTextInfo.setTextColor(0xffffffff);
							mPosterName.setTextColor(0xffffffff);
						}
					}else{
						mWbPosterIcon.setImageBitmap(bitmap);
					}
				}else{
					//do nothing
				}
				break;
			case RESOURCE_DETAIL_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					String name = mWbDetail.getName();
					String body = mWbDetail.getBody();
					mPosterName.setText(name);
					mTextInfo.setText(body);
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
		
		public void downloadResource(boolean status, int imageFlag, Drawable drawable){
			int what = IMAGE_DOWNLOAD;
			if (status == true) {
				android.os.Message msg = android.os.Message.obtain(this, what, 1, imageFlag, drawable);
				sendMessage(msg);
			} else {
				android.os.Message msg = android.os.Message.obtain(this, what, 0, 0);
				sendMessage(msg);
			}
		}
	}
	
	private int ComputeSize(float ratio, int[] size){
		int type = 1;
		int width = mBusinessPic.getWidth();
		int topPos = mBusinessPic.getTop();
		int realHeight = (int)(width * ratio);
		Display display = getWindowManager().getDefaultDisplay();
		
		size[0] = width;
		
		//threshold line
		int line1pos = mTextInfo.getTop()>mStarIcon.getTop() ? mStarIcon.getTop()-5 : mTextInfo.getTop() - 5;
		int line2pos = mHorizontalLine.getTop();
		int height1 = line1pos - topPos;
		int height2 = line2pos - topPos;
		int height3 = height2 + 60;
		
		if(realHeight <= height1){
			size[1] = height1;
			type = 1;
		}else if(realHeight < height2){
			size[1] = height2;
			type = 2;
		}else{
			size[1] = height3;
			type = 3;
		}
		
		return type;
	}
	
	private String TAG = "WBDetailInfo";
    private final static String EXTRA_WB_ID = "wb_id";
    
    private int mWbId;
	private WbDetail mWbDetail;
	private WbHandler mWbHandler;
	private Context mContext;
	private Controller mController;
	private ControllerResults mResultCallback;
    
    private ImageView mBusinessPic;
    private ImageView mWbPosterIcon;
    private ImageView mStarIcon;
    private TextView mTextInfo;
    private TextView mPosterName;
    private TextView mHorizontalLine;
    private int mFlag = 0;
    private final static int[] mStarRes = {R.drawable.star1, R.drawable.star2, R.drawable.star4, R.drawable.star3};
}
