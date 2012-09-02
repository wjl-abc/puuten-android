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
	private int mId;
	//image url
	private String mUrl;
	
	//image position in water flow
	private int mRowId;
	private int mColId;
	
	private String TAG = "FlowViewElement";
	
	public FlowViewElement(Context c, AttributeSet attrs, int defStyle) {
		super(c, attrs, defStyle);
		init(c);
	}

	public FlowViewElement(Context c, AttributeSet attrs) {
		super(c, attrs);
		init(c);
	}

	public FlowViewElement(Context c) {
		super(c);
		init(c);
	}
	
	private void init(Context context){
		mContext = context;
		mController = Controller.getInstance(context);
		setOnClickListener(this);
	}
	
	public Handler getViewHandler() {
		return mViewHandler;
	}

	public void setViewHandler(Handler viewHandler) {
		mViewHandler = viewHandler;
		downloadImage();
	}
	
	public String getUrl(){
		return mUrl;
	}
	
	public void setUrl(String url){
		mUrl = url;
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
		//send message to main activity
		int what = IMAGE_CLICKED;
		Handler h = getViewHandler();
		Message m = h.obtainMessage(what, mId, 0, FlowViewElement.this);
		h.sendMessage(m);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
		//画边框
		Rect rec=canvas.getClipBounds();
		rec.top += 2;
		rec.left += 2;
		rec.bottom -= 2;
		rec.right -= 2;
		Paint paint=new Paint();
		paint.setColor(Color.GRAY);
		paint.setStyle(Paint.Style.FILL);
		canvas.drawRect(rec, paint);
//		Log.d(TAG, "in onDraw " + mUrl);
		super.onDraw(canvas);
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
				Message m = h.obtainMessage(what, status, mId, FlowViewElement.this);
				h.sendMessage(m);
			}
		}.start();
	}
}
