package com.android.puuter;

import android.content.Context;
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
		resultCallback.loginServerCallBack(context, 100);
	}
	
	public interface Result{
		//only for login
		public void loginServerCallBack(Context context, int progress);
	}
	
	private static Controller sInstance;
	private Context mContext;
	private HttpController mHttpController;
	private final String TAG = "Controller";
}