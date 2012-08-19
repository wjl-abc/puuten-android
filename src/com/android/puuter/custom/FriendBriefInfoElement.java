package com.android.puuter.custom;

import com.android.puuter.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Context;

public class FriendBriefInfoElement extends RelativeLayout {

	public FriendBriefInfoElement(Context context) {
		super(context);
		init(context);
	}

	public FriendBriefInfoElement(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FriendBriefInfoElement(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater.from(context).inflate(R.layout.friend_brief_info, this, true);
		mNewestInfo = (FlowViewElement) findViewById(R.id.newestInfo);
		mFriendPic = (FlowViewElement) findViewById(R.id.friendPic);
		mTitle = (TextView) findViewById(R.id.title);
		mFriendName = (TextView) findViewById(R.id.friendName);
		mFriendDynInfo = (TextView) findViewById(R.id.friendDynInfo);
	}

	public void setInfoGone() {
		mNewestInfo.setVisibility(GONE);
		mTitle.setVisibility(GONE);
		mFriendName.setVisibility(GONE);
		mFriendName.setVisibility(GONE);
		mFriendDynInfo.setVisibility(GONE);
	}

	public void setInfoVisible() {
		mNewestInfo.setVisibility(VISIBLE);
		mTitle.setVisibility(VISIBLE);
		mFriendName.setVisibility(VISIBLE);
		mFriendName.setVisibility(VISIBLE);
		mFriendDynInfo.setVisibility(VISIBLE);
	}
	
	public void setNewestInfo(String url){
		mNewestInfo.setUrl(url);
	}
	
	public void setFriendPic(String url){
		mFriendPic.setUrl(url);
	}
	
	public void setTitle(String str){
		mTitle.setText(str);
	}
	
	public void setFriendName(String str){
		mFriendName.setText(str);
	}
	
	public void setFriendDynInfo(String str){
		mFriendDynInfo.setText(str);
	}
	
	public void setFriendId(int id){
		mFriendId = id;
	}
	
	public int getFriendId(){
		return mFriendId;
	}

	private FlowViewElement mNewestInfo;
	private FlowViewElement mFriendPic;
	private TextView mTitle;
	private TextView mFriendName;
	private TextView mFriendDynInfo;
	
	private int mFriendId;
}
