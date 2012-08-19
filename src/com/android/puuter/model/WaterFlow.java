package com.android.puuter.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.util.Log;

public class WaterFlow {
	public WaterFlow(){
		mWaterFlowData = new ArrayList<WaterFlowElement>();
		mWBId2index = new HashMap<Integer, Integer>();
		mSizeBeforeUpdate = 0;
	}
	public int parseJson(String jsonStr){
		if(jsonStr == null){
			return -1;
		}
		
		try{
			JSONArray jsonArray = new JSONArray(jsonStr);
			int len = jsonArray.length();
			mSizeBeforeUpdate = mWaterFlowData.size();
			for(int i=0; i<len; i++){
				WaterFlowElement waterFlowData = new WaterFlowElement();
				waterFlowData.mName = jsonArray.getJSONObject(i).getString("name");
				waterFlowData.mBody = jsonArray.getJSONObject(i).getString("body");
				waterFlowData.mPicUrl = jsonArray.getJSONObject(i).getString("thumbnail_pic");
				waterFlowData.mWBId = jsonArray.getJSONObject(i).getInt("wb_id");
				waterFlowData.mRatio = (float)jsonArray.getJSONObject(i).getDouble("ratio");
				mWBId2index.put(waterFlowData.mWBId, mSizeBeforeUpdate+i);
				mWaterFlowData.add(waterFlowData);
			}
		}catch(Exception e){
			Log.v(TAG, "json parse fail, json data:" + jsonStr);
			return -1;
		}
		return 0;
	}
	
	public int getDataLen(){
		return mWaterFlowData.size();
	}
	
	public int getSizeBeforeUpdate(){
		return mSizeBeforeUpdate;
	}
	
	public String getPicUrl(int i){
		return mWaterFlowData.get(i).mPicUrl;
	}
	
	public String getName(int i){
		return mWaterFlowData.get(i).mName;
	}
	
	public String getBody(int i){
		return mWaterFlowData.get(i).mBody;
	}
	
	public float getRatio(int i){
		return mWaterFlowData.get(i).mRatio;
	}
	
	public int getWBId(int i){
		return mWaterFlowData.get(i).mWBId;
	}
	
	public int getIndexByWBId(int wbId){
		return mWBId2index.get(wbId);
	}
	
	private class WaterFlowElement {
		public String mName;
		public String mBody;
		public String mPicUrl;
		public int mWBId;
		public float mRatio;
	}
	
	private ArrayList<WaterFlowElement> mWaterFlowData;
	//map from mWBId to index of mWaterFlowData
	private HashMap<Integer, Integer> mWBId2index;
	private int mSizeBeforeUpdate;
	private String TAG = "WaterFlow";
}
