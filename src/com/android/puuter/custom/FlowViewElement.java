package com.android.puuter.custom;

import com.android.puuter.controller.Controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FlowViewElement extends ImageView implements View.OnClickListener{
	private Context mContext;
	private Controller mController;
	private Drawable mDrawable;
	private Handler mViewHandler;
	
	private final int IMAGE_DOWNLOAD = 1;
	private final int IMAGE_DOWNLOAD_SUCCESS = 1;
	private final int IMAGE_DOWNLOAD_FAIL = 0;
	private final int IMAGE_CLICKED = 3;
	
	//image id in server
	private int mWBId;
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
		setOnClickListener(this);
		
		downloadImage();
	}

	public FlowViewElement(Context c, AttributeSet attrs, Handler viewHandler) {
		super(c, attrs);
		mContext = c;
		mController = Controller.getInstance(mContext);
		mViewHandler = viewHandler;
		setOnClickListener(this);
		
		downloadImage();
	}

	public FlowViewElement(Context c, Handler viewHandler) {
		super(c);
		
		mContext = c;
		mController = Controller.getInstance(mContext);
		mViewHandler = viewHandler;
		setOnClickListener(this);
		
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
		return mWBId;
	}
	
	public void setId(int id){
		mWBId = id;
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
		//send message to main activity
		int what = IMAGE_CLICKED;
		Handler h = getViewHandler();
		Message m = h.obtainMessage(what, mWBId, 0, FlowViewElement.this);
		h.sendMessage(m);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画边框
		Rect rec=canvas.getClipBounds();
		rec.bottom -= 5;
		rec.right -= 5;
		Paint paint=new Paint();
		paint.setColor(Color.BLACK);
//		paint.setARGB(100,100,100,100);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rec, paint);
		Log.d(TAG, "in onDraw " + mWBId);
	}

	
	private void downloadImage(){
		new Thread(){
			public void run(){
//				Log.d(TAG, "begin "+mRowId+" "+mColId+" "+mUrl);
				mDrawable = mController.loadImage(mUrl);
//				Log.d(TAG, "end "+mRowId+" "+mColId+" "+mUrl);
				int what = IMAGE_DOWNLOAD;
				int status = mDrawable==null ? IMAGE_DOWNLOAD_FAIL : IMAGE_DOWNLOAD_SUCCESS;
				Handler h = getViewHandler();
				Message m = h.obtainMessage(what, status, mWBId, FlowViewElement.this);
				h.sendMessage(m);
			}
		}.start();
	}
}
