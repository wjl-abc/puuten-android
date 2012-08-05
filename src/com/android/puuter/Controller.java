package com.android.puuter;

import com.android.puuter.model.WaterFlowElement;
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
		int id = mHttpController.loginRemote(username, password);
		Log.d(TAG, "id:"+id);
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
		WaterFlowElement []waterFlowData = new WaterFlowElement[30];
		for(int i=0; i<30; i++){
			Log.v(TAG, i+"");
			waterFlowData[i] = new WaterFlowElement("http://upload.northnews.cn/2011/0805/1312509857406.jpg", 1.0f, 100);
//			waterFlowData[i] = new WaterFlowElement("http://images.orzzso.com/img/photo/1/25440.jpg", 1.0f, 100);
		}
		resultCallback.downloadResource(true, waterFlowData);
		
		String url = Setting.rootUrl + "/" + Setting.eventPath;
		String str = mHttpController.retrieveRemoteData(url);
	}
	
	public interface Result{
		//only for login
		public void loginServerCallBack(Context context, int progress);
		public void downloadResource(boolean status, WaterFlowElement []waterFlowData);
	}
	
	private static Controller sInstance;
	private Context mContext;
	private HttpController mHttpController;
	private final String TAG = "Controller";
}