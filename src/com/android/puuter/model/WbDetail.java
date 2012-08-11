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
			Log.d(TAG, "BS id: " + mBsId);
			Log.d(TAG, "name: " + mName);
			Log.d(TAG, "body: " + mBody);
			Log.d(TAG, "avatarUrl: " + mAvatarUrl);
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
	private static String TAG = "WbDetail";
}
