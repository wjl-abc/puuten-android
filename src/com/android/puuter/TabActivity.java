package com.android.puuter;

import java.util.ArrayList;

import com.android.puuter.Controller.Result;
import com.android.puuter.custom.FlowViewElement;
import com.android.puuter.model.WaterFlowElement;
import com.android.puuter.setting.Setting;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class TabActivity extends Activity {
	
    public static void actionView(Context context, int tabId){
    	Intent i = new Intent(context, TabActivity.class);
    	i.putExtra(EXTRA_TAB_ID, tabId);
    	context.startActivity(i);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        
        mWaterFallView = (LinearLayout) findViewById(R.id.waterFallContainer);
        mContext = this;
        mController = Controller.getInstance(mContext);
        mResultCallback = new ControllerResults();
        mTotalNum = 0;
        mTabHandler = new TabHandler();
        mImageViewWidth = getWindowManager().getDefaultDisplay().getWidth()/mDisplayCols;
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
        
        //load resource
        new Thread(){
        	public void run(){
        		loadResource();
        	}
        }.start();
    }
    
    private void loadResource(){
    	mController.loadResource(0, mResultCallback);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_tab, menu);
//        return true;
//    }
    
    private class ControllerResults implements Controller.Result{
    	public void loginServerCallBack(Context context, int progress){
    	}
    	
    	public void downloadResource(boolean status, WaterFlowElement []waterFlowData){
    		mWaterFlowData = waterFlowData;
    		mTabHandler.downloadResourceStatus(status);
    	}
    }
    
    private class TabHandler extends Handler{
    	private final int IMAGE_DOWNLOAD_SUCCESS = 1;
    	private final int IMAGE_DOWNLOAD_FAIL = 2;
    	private final int RESOURCE_DOWNLOAD_SUCCESS = 3;
    	private final int RESOURCE_DOWNLOAD_FAIL = 4;
    	
    	public void handleMessage(android.os.Message msg){
    		switch(msg.what){
    		case IMAGE_DOWNLOAD_SUCCESS:
    			int rowId = msg.arg1;
    			int colId = msg.arg2;
    			FlowViewElement fvw = (FlowViewElement) msg.obj;
    			Drawable drawable = fvw.getDrawableImage();
    			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
    			fvw.setImageBitmap(bitmap);
    			break;
    		case IMAGE_DOWNLOAD_FAIL:
    			break;
    		case RESOURCE_DOWNLOAD_SUCCESS:
    			for(WaterFlowElement wfe : mWaterFlowData){
    				int columnIndex = GetMinValue(mColumnHeights);
    				FlowViewElement flowViewElement = new FlowViewElement(mContext, mTabHandler);
    				flowViewElement.setUrl(wfe.getUrl());
    				flowViewElement.setRatio(wfe.getRatio());
    				flowViewElement.setId(wfe.getId());
    				
    				int height = (int)(wfe.getRatio()*mImageViewWidth);
    				
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
    			break;
    		case RESOURCE_DOWNLOAD_FAIL:
    			break;
    		default:
    			super.handleMessage(msg);
    		}
    	}
    	
    	public void downloadResourceStatus(boolean status){
    		if(status == true){
    			android.os.Message msg = android.os.Message.obtain(this, RESOURCE_DOWNLOAD_SUCCESS);
    			sendMessage(msg);
    		}else{
    			android.os.Message msg = android.os.Message.obtain(this, RESOURCE_DOWNLOAD_FAIL);
    			sendMessage(msg);
    		}
    	}
    }
    
	private int GetMinValue(int[] array) {
		int m = 0;
		int length = array.length;
		for (int i = 1; i < length; ++i) {
			if (array[i] < array[m]) {
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
    //total images got from server
    private int mTotalNum;
    private int mDisplayCols = Setting.displayCols;
    //Each imageview element width
    private int mImageViewWidth;
    private int []mColumnHeights;
    
    private LinearLayout mWaterFallView;
    private WaterFlowElement []mWaterFlowData;
    
    private String TAG = "TabActivity";
}
