package com.android.puuter.custom;

import com.android.puuter.R;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class FriendDynInfoElement extends LinearLayout {

	public FriendDynInfoElement(Context context) {
	    super(context);
	    init(context);
    }

	public FriendDynInfoElement(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    init(context);
    }
	
	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.friend_dyn_info_element, this, true);
		mFve = (FlowViewElement) findViewById(R.id.newestPic);
		mFbi = (FriendBriefInfoElement) findViewById(R.id.fbie);
	}
	
	public Handler getViewHandler() {
		return mViewHandler;
	}

	public void setViewHandler(Handler viewHandler) {
		mViewHandler = viewHandler;
		mFve.setViewHandler(mViewHandler);
		mFbi.setViewHandler(mViewHandler);
	}
	
	public void setInfoGone(){
		mFbi.setInfoGone();
	}
	
	public void setInfoVisible(){
		mFbi.setInfoVisible();
	}
	
	public void setWbId(int id){
		mId = id;
	}
	
	private String TAG = "FriendDynInfoElement";
	Handler mViewHandler;
	public FlowViewElement mFve;
	public FriendBriefInfoElement mFbi;
	private int mId;
}
