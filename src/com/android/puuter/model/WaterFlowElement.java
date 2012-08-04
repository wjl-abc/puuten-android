package com.android.puuter.model;

public class WaterFlowElement {
	private String mUrl;
	private float mRatio;
	private int mId;
	
	public WaterFlowElement(String url, float ratio, int id){
		mUrl = url;
		mRatio = ratio;
		mId = id;
	}
	
	public WaterFlowElement(){
	}
	
	public void setUrl(String url){
		mUrl = url;
	}
	
	public String getUrl(){
		return mUrl;
	}
	
	public void setRatio(float ratio){
		mRatio = ratio;
	}
	
	public float getRatio(){
		return mRatio;
	}
	
	public void setId(int id){
		mId = id;
	}
	
	public int getId(){
		return mId;
	}
}
