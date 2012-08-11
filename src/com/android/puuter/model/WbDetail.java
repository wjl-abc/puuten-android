package com.android.puuter.model;

public class WbDetail {
	private WbDetail(){
	}

	public int getWbId(){
		return mWbId;
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
	
	public static WbDetail parseJsonToWbDetail(String jsonStr){
		if(jsonStr == null){
			return null;
		}
		WbDetail wbd = new WbDetail();
		return wbd;
	}
	
	private int mWbId;
	private String mName;
	private String mBody;
	private String mAvatarUrl;
}
