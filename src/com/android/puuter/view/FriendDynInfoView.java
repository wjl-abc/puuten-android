package com.android.puuter.view;

import java.util.ArrayList;

import com.android.puuter.R;
import com.android.puuter.controller.Controller;
import com.android.puuter.controller.Controller.Result;
import com.android.puuter.custom.FlowViewElement;
import com.android.puuter.custom.FriendDynInfoElement;
import com.android.puuter.model.FriendDynInfo;
import com.android.puuter.model.WbDetail;
import com.android.puuter.setting.Setting;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class FriendDynInfoView extends Activity {

	public static void actionView(Context context) {
		Intent i = new Intent(context, FriendDynInfoView.class);
		context.startActivity(i);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_dyn_info_view);
        
		mFriendWaterFallView = (LinearLayout) findViewById(R.id.friendWaterFallContainer);
		mFriendWaterFallScrollView = (ScrollView) findViewById(R.id.friendWaterFallScrollView);
		
		mContext = this;
		mController = Controller.getInstance(mContext);
		mResultCallback = new ControllerResults();

//		mWaterFallScrollView.setOnTouchListener(new WaterFallOnTouchListener());
		mTotalNum = 0;
		mFriendDynInfoHandler = new FriendDynInfoHandler();
		mImageViewWidth = getWindowManager().getDefaultDisplay().getWidth() / mDisplayCols;
		mColumnHeights = new int[mDisplayCols];

		mFriendWaterFallArray = new ArrayList<LinearLayout>();
		for (int i = 0; i < mDisplayCols; i++) {
			LinearLayout itemLayout = new LinearLayout(mContext);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(mImageViewWidth, LayoutParams.WRAP_CONTENT);

			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			mFriendWaterFallArray.add(itemLayout);
			mFriendWaterFallView.addView(itemLayout);
		}

		// load resource
		new Thread() {
			public void run() {
				loadResource();
			}
		}.start();
    }

	private void loadResource() {
		mController.loadFriendInfoRes(0, mResultCallback);
	}
	
	private class ControllerResults implements Controller.Result {
		public void loginServerCallBack(Context context, int progress) {
		}

		public void downloadResource(boolean status, Object data) {
			mFriendDynInfo = (FriendDynInfo)data;
			mFriendDynInfoHandler.downloadResourceStatus(status);
		}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
		}
	}
	
	private class FriendDynInfoHandler extends Handler {
		private final int IMAGE_DOWNLOAD = 1;
		private final int RESOURCE_DOWNLOAD = 2;
		private final int IMAGE_CLICKED = 3;
		
		public void handleMessage(android.os.Message msg) {
			// 0 means fail
			int status = 0;
			switch (msg.what) {
			case IMAGE_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					FlowViewElement fvw = (FlowViewElement) msg.obj;
					Drawable drawable = fvw.getDrawableImage();
					Bitmap bitmapTmp = ((BitmapDrawable) drawable).getBitmap();
					float scale = mImageViewWidth / ((float) bitmapTmp.getWidth());
					Matrix matrix = new Matrix();
					matrix.postScale(scale, scale);
					Bitmap bitmap = Bitmap.createBitmap(bitmapTmp, 0, 0, bitmapTmp.getWidth(), bitmapTmp.getHeight(), matrix, false);
					bitmapTmp.recycle();
					fvw.setImageBitmap(bitmap);
				} else {
					Log.d(TAG, "download pic fail");
				}
				break;
			case RESOURCE_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					int len = mFriendDynInfo.getDataLen();
					int sizeBeforeUpdater = mFriendDynInfo.getSizeBeforeUpdate();
					for (int i = sizeBeforeUpdater; i < len; i++) {
						int columnIndex = GetMinValue(mColumnHeights);
						FriendDynInfoElement fdie = new FriendDynInfoElement(mContext);
						fdie.mFve.setUrl(mFriendDynInfo.getPicUrl(i));
						fdie.mFve.setId(mFriendDynInfo.getWBId(i));
						fdie.mFbi.setFriendPic(mFriendDynInfo.getFriendAvatar(i));
						fdie.mFbi.setTitle(mFriendDynInfo.getFriendName(i));
						fdie.mFbi.setFriendId(mFriendDynInfo.getWBId(i));
						fdie.mFbi.setFriendName(mFriendDynInfo.getFriendName(i));
						fdie.mFbi.setFriendDynInfo(mFriendDynInfo.getDynInfo(i));
						fdie.setViewHandler(mFriendDynInfoHandler);
						
						int height = (int) (mFriendDynInfo.getRatio(i) * mImageViewWidth+80);
						LayoutParams lp = null;//FriendDynInfoElement.getLayoutParams();
						if (lp == null) {
							lp = new LayoutParams(mImageViewWidth, height);
						}
						fdie.setLayoutParams(lp);
						mFriendWaterFallArray.get(columnIndex).addView(fdie);
						mColumnHeights[columnIndex] += height;
						int row = mFriendWaterFallArray.get(columnIndex).indexOfChild(fdie);
						int col = columnIndex;
						fdie.mFve.setPosition(row, col);
					}
				} else {
					// do nothing
				}
				break;
			}
		}
		
		public void downloadResourceStatus(boolean status) {
			int what = RESOURCE_DOWNLOAD;
			if (status == true) {
				android.os.Message msg = android.os.Message.obtain(this, what, 1, 0);
				sendMessage(msg);
			} else {
				android.os.Message msg = android.os.Message.obtain(this, what, 0, 0);
				sendMessage(msg);
			}
		}
	}
	
	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 1; i < length; ++i){
			if (array[i] < array[m]){
				m = i;
			}
		}
		return m;
	}
    
	private ArrayList<LinearLayout> mFriendWaterFallArray;

	private FriendDynInfoHandler mFriendDynInfoHandler;
	private Controller mController;
	private Result mResultCallback;
	private Context mContext;
	// total images got from server
	private int mTotalNum;
	private int mDisplayCols = Setting.displayCols;
	// Each imageview element width
	private int mImageViewWidth;
	private int[] mColumnHeights;

	private LinearLayout mFriendWaterFallView;
	private ScrollView mFriendWaterFallScrollView;
	private FriendDynInfo mFriendDynInfo;

	private String TAG = "FriendDynInfoView";
}
