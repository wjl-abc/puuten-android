package com.android.puuter.model;

import org.json.JSONArray;

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
		if(jsonStr == null){
			return -1;
		}
		
		try{
			JSONArray jsonArray = new JSONArray(jsonStr);
			int len = jsonArray.length();
			mWaterFlowData = new WaterFlowElement[len];
			for(int i=0; i<len; i++){
				mWaterFlowData[i] = new WaterFlowElement();
				mWaterFlowData[i].mName = jsonArray.getJSONObject(i).getString("name");
				mWaterFlowData[i].mBody = jsonArray.getJSONObject(i).getString("body");
				mWaterFlowData[i].mPicUrl = jsonArray.getJSONObject(i).getString("thumbnail_pic");
				mWaterFlowData[i].mId = jsonArray.getJSONObject(i).getInt("wb_id");
				mWaterFlowData[i].mRatio = jsonArray.getJSONObject(i).getInt("ratio");
			}
		}catch(Exception e){
			Log.v(TAG, "json parse fail, json data:" + jsonStr);
			return -1;
		}
		return 0;
	}
	
	public int getDataLen(){
		return mWaterFlowData.length;
	}
	
	public WaterFlowElement getData(int i){
		return mWaterFlowData[i];
	}
	
	
	public String getPicUrl(int i){
		return mWaterFlowData[i].mPicUrl;
	}
	
	public String getName(int i){
		return mWaterFlowData[i].mName;
	}
	
	public String getBody(int i){
		return mWaterFlowData[i].mBody;
	}
	
	public float getRatio(int i){
		return mWaterFlowData[i].mRatio;
	}
	
	public int getId(int i){
		return mWaterFlowData[i].mId;
	}
	
	private class WaterFlowElement {
		public String mName;
		public String mBody;
		public String mPicUrl;
		public int mId;
		public float mRatio;
	}
	
	private WaterFlowElement []mWaterFlowData;
	private String mJsonStr;
	private String TAG = "WaterFlow";
}