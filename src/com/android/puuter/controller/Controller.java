package com.android.puuter.controller;

import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import com.android.puuter.model.WaterFlow;
import com.android.puuter.model.WbDetail;
import com.android.puuter.setting.Setting;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Log;

public class Controller {
	protected Controller(Context context){
		mContext = context;
		mHttpController = HttpController.getInstance(context);
	}
	
	public synchronized static Controller getInstance(Context context){
		if(sInstance == null){
			sInstance = new Controller(context);
		}
		return sInstance;
	}
	
	public void loginServer(final Context context, final String username, final String password, final Result resultCallback){
		resultCallback.loginServerCallBack(context, 0);
		String url = Setting.rootUrl + "/" + Setting.loginPath;
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("username", username);
		parms.put("password", password);
		parms.put("mobile", "android");
		int id = mHttpController.loginRemote(url, parms);
//		Log.d(TAG, "id:"+id);
		if(id > 0){
			resultCallback.loginServerCallBack(context, 100);
		}else{
			resultCallback.loginServerCallBack(context, -1);
		}
	}
	
	public Drawable loadImage(String url){
		return mHttpController.loadImage(url);
	}
	
	public void loadResource(int id, final Result resultCallback){
		String url = Setting.rootUrl + "/" + Setting.eventPath;
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("mobile", "android");
		String jsonStr = mHttpController.retrieveRemoteData(url, parms);
		WaterFlow waterFlow = new WaterFlow();
		int ret = waterFlow.parseJson(jsonStr);
		resultCallback.downloadResource(ret==0? true:false, waterFlow);
	}
	
	public void loadResourceWbDetail(int wbId, final Result resultCallback){
		String url = Setting.rootUrl + "/" + Setting.idDetail + "/" + wbId + "/";
		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put("mobile", "android");
		String jsonStr = mHttpController.retrieveRemoteData(url, parms);
		WbDetail wbd = new WbDetail(wbId);
		int ret = wbd.parseJsonToWbDetail(jsonStr);
		resultCallback.downloadResourceWbDetail(ret==0? true:false, wbd);
	}
	
	public interface Result{
		//only for login
		public void loginServerCallBack(Context context, int progress);
		public void downloadResource(boolean status, WaterFlow waterFlow);
		public void downloadResourceWbDetail(boolean status, WbDetail wbd);
	}
	
	private static Controller sInstance;
	private Context mContext;
	private HttpController mHttpController;
	private final String TAG = "Controller";
}