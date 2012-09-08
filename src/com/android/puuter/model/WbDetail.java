package com.android.puuter.model;

import org.json.JSONObject;

import android.util.Log;

public class WbDetail {
	public WbDetail(int wbId){
		mWbId = wbId;
	}

	public int getWbId(){
		return mWbId;
	}
	
	public int getBsId(){
		return mBsId;
	}
	
	public String getName(){
		return mName;
	}
	
	public String getBody(){
		return mBody;
	}
	
	public String getAvatarUrl(){
		return mAvatarUrl;
	}
	
	public float getRatio(){
		return mRatio;
	}
	
	public String getReWbBody(){
		return mReWbBody;
	}
	
	public String getReWbName(){
		return mReWbName;
	}
	
	public int getType(){
		return mType;
	}
	
	public String getBsUrl(){
		return mBsUrl;
	}
	
	public int parseJsonToWbDetail(String jsonStr){
		if(jsonStr == null){
			return -1;
		}

		try{
			JSONObject jsonArray = new JSONObject(jsonStr);
			mName = jsonArray.getString("name");
			mBody = jsonArray.getString("body");
			mAvatarUrl = jsonArray.getString("avatar_url");
			mBsId = jsonArray.getInt("bs_id");
			mRatio = (float)jsonArray.getDouble("ratio");
			mReWbBody = jsonArray.getString("re_wb_body");
			mReWbName = jsonArray.getString("re_wb_name");
			mType = jsonArray.getInt("type");
			mBsUrl = jsonArray.getString("pic_url");
		}catch(Exception e){
			Log.v(TAG, "json parse fail, json data:" + jsonStr);
			return -1;
		}
		return 0;
	}
	
	private int mWbId;
	private int mBsId;
	private String mName;
	private String mBody;
	private String mAvatarUrl;
	private float mRatio;
	private String mReWbBody;
	private String mReWbName;
	private int mType;
	private String mBsUrl;
	private static String TAG = "WbDetail";
}
