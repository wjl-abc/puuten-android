package com.android.puuter.custom;

import com.android.puuter.Controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class FlowViewElement extends ImageView implements View.OnClickListener{
	private Context mContext;
	private Controller mController;
	private Drawable mDrawable;
	private Handler mViewHandler;
	
	private final int IMAGE_DOWNLOAD_SUCCESS = 1;
	private final int IMAGE_DOWNLOAD_FAIL = 2;
	
	//image id in server
	private int mId;
	//image url
	private String mUrl;
	//image height/width ratio
	private float mRatio;
	
	//image position in water flow
	private int mRowId;
	private int mColId;
	
	private String TAG = "FlowViewElement";
	
	public FlowViewElement(Context c, AttributeSet attrs, int defStyle, Handler viewHandler) {
		super(c, attrs, defStyle);
		
		mContext = c;
		mController = Controller.getInstance(mContext);
		mViewHandler = viewHandler;
		
		downloadImage();
	}

	public FlowViewElement(Context c, AttributeSet attrs, Handler viewHandler) {
		super(c, attrs);
		mContext = c;
		mController = Controller.getInstance(mContext);
		mViewHandler = viewHandler;
		
		downloadImage();
	}

	public FlowViewElement(Context c, Handler viewHandler) {
		super(c);
		
		mContext = c;
		mController = Controller.getInstance(mContext);
		mViewHandler = viewHandler;
		
		downloadImage();
	}
	
	public Handler getViewHandler() {
		return mViewHandler;
	}

	public void setViewHandler(Handler viewHandler) {
		mViewHandler = viewHandler;
	}
	
	public String getUrl(){
		return mUrl;
	}
	
	public void setUrl(String url){
		mUrl = url;
	}
	
	public float getRatio(){
		return mRatio;
	}
	
	public void setRatio(float ratio){
		mRatio = ratio;
	}
	
	public int getId(){
		return mId;
	}
	
	public void setId(int id){
		mId = id;
	}
	
	public int getRowId(){
		return mRowId;
	}
	
	public int getColId(){
		return mColId;
	}
	
	public void setPosition(int rowId, int colId){
		mRowId = rowId;
		mColId = colId;
	}
	
	public Drawable getDrawableImage(){
		return mDrawable;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	private void downloadImage(){
		new Thread(){
			public void run(){
				Log.v(TAG, "begin "+mRowId+" "+mColId+" "+mUrl);
				mDrawable = mController.loadImage(mUrl);
				Log.v(TAG, "end "+mRowId+" "+mColId+" "+mUrl);
				int what = mDrawable==null ? IMAGE_DOWNLOAD_FAIL : IMAGE_DOWNLOAD_SUCCESS;
				Handler h = getViewHandler();
				Message m = h.obtainMessage(what, mRowId, mColId, FlowViewElement.this);
				h.sendMessage(m);
			}
		}.start();
	}
}
