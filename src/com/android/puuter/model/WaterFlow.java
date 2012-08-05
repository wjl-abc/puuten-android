package com.android.puuter.model;

import org.json.JSONObject;

import android.util.Log;

public class WaterFlow {
	public WaterFlow(){
	}
	
	public WaterFlow(String jsonStr){
		mJsonStr = jsonStr;
	}
	
	public int parseJson(){
		return parseJson(mJsonStr);
	}
	
	public int parseJson(String jsonStr){
		try{
			JSONObject jsonObject = new JSONObject(jsonStr); 
		}catch(Exception e){
			Log.v(TAG, "json parse fail, json data:" + jsonStr);
			return -1;
		}
		return 0;
	}
	
	private class WaterFlowElement {
		private String mUrl;
		private float mRatio;
		private int mId;
	}
	
	private WaterFlowElement []mWaterFlowData;
	private String mJsonStr;
	private String TAG = "WaterFlow";
}
