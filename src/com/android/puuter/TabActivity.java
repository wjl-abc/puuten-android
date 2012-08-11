package com.android.puuter;

import java.util.ArrayList;

import com.android.puuter.controller.Controller;
import com.android.puuter.controller.Controller.Result;
import com.android.puuter.custom.FlowViewElement;
import com.android.puuter.model.WaterFlow;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class TabActivity extends Activity {

	public static void actionView(Context context, int tabId) {
		Intent i = new Intent(context, TabActivity.class);
		i.putExtra(EXTRA_TAB_ID, tabId);
		context.startActivity(i);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		mWaterFallView = (LinearLayout) findViewById(R.id.waterFallContainer);
		mWaterFallScrollView = (ScrollView) findViewById(R.id.waterFallScrollView);

		mContext = this;
		mController = Controller.getInstance(mContext);
		mResultCallback = new ControllerResults();

		mWaterFallScrollView.setOnTouchListener(new WaterFallOnTouchListener());
		mTotalNum = 0;
		mTabHandler = new TabHandler();
		mImageViewWidth = getWindowManager().getDefaultDisplay().getWidth() / mDisplayCols;
		mColumnHeights = new int[mDisplayCols];

		mWaterFallArray = new ArrayList<LinearLayout>();
		for (int i = 0; i < mDisplayCols; i++) {
			LinearLayout itemLayout = new LinearLayout(mContext);
			LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(mImageViewWidth, LayoutParams.WRAP_CONTENT);

			itemLayout.setPadding(2, 2, 2, 2);
			itemLayout.setOrientation(LinearLayout.VERTICAL);

			itemLayout.setLayoutParams(itemParam);
			mWaterFallArray.add(itemLayout);
			mWaterFallView.addView(itemLayout);
		}

		// load resource
		new Thread() {
			public void run() {
				loadResource();
			}
		}.start();
	}

	private void loadResource() {
		mController.loadResource(0, mResultCallback);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.activity_tab, menu);
	// return true;
	// }

	private class ControllerResults implements Controller.Result {
		public void loginServerCallBack(Context context, int progress) {
		}

		public void downloadResource(boolean status, WaterFlow waterFlow) {
			mWaterFlow = waterFlow;
			mTabHandler.downloadResourceStatus(status);
		}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
		}
	}

	private class TabHandler extends Handler {
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
					Bitmap bitmap = Bitmap.createBitmap(bitmapTmp, 0, 0, bitmapTmp.getWidth(), bitmapTmp.getHeight(), matrix, true);
					bitmapTmp.recycle();
					fvw.setImageBitmap(bitmap);
				} else {
					Log.d(TAG, "download pic fail");
				}
				break;
			case RESOURCE_DOWNLOAD:
				status = msg.arg1;
				if (status == 1) {
					int len = mWaterFlow.getDataLen();
					int sizeBeforeUpdater = mWaterFlow.getSizeBeforeUpdate();
					for (int i = sizeBeforeUpdater; i < len; i++) {
						int columnIndex = GetMinValue(mColumnHeights);
						FlowViewElement flowViewElement = new FlowViewElement(mContext, mTabHandler);
						flowViewElement.setUrl(mWaterFlow.getPicUrl(i));
						flowViewElement.setRatio(mWaterFlow.getRatio(i));
						flowViewElement.setId(mWaterFlow.getWBId(i));

						int height = (int) (mWaterFlow.getRatio(i) * mImageViewWidth);

						LayoutParams lp = flowViewElement.getLayoutParams();
						if (lp == null) {
							lp = new LayoutParams(mImageViewWidth, height);
						}
						flowViewElement.setLayoutParams(lp);
						mWaterFallArray.get(columnIndex).addView(flowViewElement);
						mColumnHeights[columnIndex] += height;
						int row = mWaterFallArray.get(columnIndex).indexOfChild(flowViewElement);
						int col = columnIndex;
						flowViewElement.setPosition(row, col);
					}
				} else {
					// do nothing
				}
				break;
			case IMAGE_CLICKED:
				int wbId = msg.arg1;
				Toast.makeText(mContext, "webo id: " + wbId + " clicked", Toast.LENGTH_SHORT).show();
				break;
			default:
				super.handleMessage(msg);
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

	private class WaterFallOnTouchListener implements OnTouchListener {
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				View view = mWaterFallScrollView.getChildAt(0);
				if (view != null) {
					if (view.getMeasuredHeight() - 20 <= mWaterFallScrollView.getScrollY() + mWaterFallScrollView.getHeight()) {
						new Thread() {
							public void run() {
								loadResource();
							}
						}.start();
					}
				}
				break;
			default:
				break;
			}
			return false;
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

	private final static String EXTRA_TAB_ID = "tab_id";

	private ArrayList<LinearLayout> mWaterFallArray;

	private TabHandler mTabHandler;
	private Controller mController;
	private Result mResultCallback;
	private Context mContext;
	// total images got from server
	private int mTotalNum;
	private int mDisplayCols = Setting.displayCols;
	// Each imageview element width
	private int mImageViewWidth;
	private int[] mColumnHeights;

	private LinearLayout mWaterFallView;
	private ScrollView mWaterFallScrollView;
	private WaterFlow mWaterFlow;

	private String TAG = "TabActivity";
}
