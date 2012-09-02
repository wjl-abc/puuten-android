package com.android.puuter.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import android.util.Log;

public class FriendDynInfo {
	public FriendDynInfo() {
		mFriendDynInfoData = new ArrayList<FriendDynInfoElement>();
		mWBId2index = new HashMap<Integer, Integer>();
		mSizeBeforeUpdate = 0;
		mStrType = new String[5];
		mStrType[0] = "把该信息加入了愿望单";
		mStrType[1] = "更新了和该信息相关的活动专辑";
		mStrType[2] = null;
		mStrType[3] = null;
		mStrType[4] = "接受了%s参加相关活动的邀请";
	}

	public int parseJson(String jsonStr) {
		if (jsonStr == null) {
			return -1;
		}

		try {
			JSONArray jsonArray = new JSONArray(jsonStr);
			int len = jsonArray.length();
			mSizeBeforeUpdate = mFriendDynInfoData.size();
			for (int i = 0; i < len; i++) {
				FriendDynInfoElement fdie = new FriendDynInfoElement();
				fdie.mName = jsonArray.getJSONObject(i).getString("name");
				fdie.mPicUrl = jsonArray.getJSONObject(i).getString("thumbnail_pic");
				fdie.mWBId = jsonArray.getJSONObject(i).getInt("wb_id");
				fdie.mRatio = (float) jsonArray.getJSONObject(i).getDouble("ratio");
				fdie.mFriendName = jsonArray.getJSONObject(i).getString("user_name");
				fdie.mFriendAvatar = jsonArray.getJSONObject(i).getString("avatar");
				fdie.mPartnerName = jsonArray.getJSONObject(i).getString("partner");
				fdie.mType = jsonArray.getJSONObject(i).getInt("type");
				mWBId2index.put(fdie.mWBId, mSizeBeforeUpdate+i);
				mFriendDynInfoData.add(fdie);
			}
		} catch (Exception e) {
			Log.v(TAG, "json parse fail, json data:" + jsonStr);
			return -1;
		}
		return 0;
	}

	public int getDataLen() {
		return mFriendDynInfoData.size();
	}

	public int getSizeBeforeUpdate() {
		return mSizeBeforeUpdate;
	}

	public String getCompanyName(int i) {
		return mFriendDynInfoData.get(i).mName;
	}

	public String getPicUrl(int i) {
		return mFriendDynInfoData.get(i).mPicUrl;
	}

	public int getWBId(int i) {
		return mFriendDynInfoData.get(i).mWBId;
	}

	public float getRatio(int i) {
		return mFriendDynInfoData.get(i).mRatio;
	}

	public String getFriendName(int i) {
		return mFriendDynInfoData.get(i).mFriendName;
	}

	public String getFriendAvatar(int i) {
		return mFriendDynInfoData.get(i).mFriendAvatar;
	}

	public String getPartnerName(int i) {
		return mFriendDynInfoData.get(i).mPartnerName;
	}

	public String getDynInfo(int i){
		if(mFriendDynInfoData.get(i).mType==0 || mFriendDynInfoData.get(i).mType==1){
			return mStrType[mFriendDynInfoData.get(i).mType];
		}else if(mFriendDynInfoData.get(i).mType == 4){
			return String.format(mStrType[mFriendDynInfoData.get(i).mType], mFriendDynInfoData.get(i).mPartnerName);
		}
		return null;
	}

	private class FriendDynInfoElement {
		public String mName;
		public String mPicUrl;
		public int mWBId;
		public float mRatio;
		public String mFriendName;
		public String mFriendAvatar;
		public String mPartnerName;
		public int mType;
	}

	private ArrayList<FriendDynInfoElement> mFriendDynInfoData;
	// map from mWBId to index of mWaterFlowData
	private HashMap<Integer, Integer> mWBId2index;
	private int mSizeBeforeUpdate;
	private String TAG = "FriendDynInfo";

	private final String[] mStrType;
}
