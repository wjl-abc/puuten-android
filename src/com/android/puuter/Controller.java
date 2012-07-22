package com.android.puuter;

import android.content.Context;

public class Controller {
	protected Controller(Context context){
		mContext = context;
	}
	
	public static Controller getInstance(Context context){
		if(sInstance == null){
			sInstance = new Controller(context);
		}
		return sInstance;
	}
	
	public interface Result{
		//only for login
		public void loginServerCallBack(Context context, int progress);
	}
	
	private static Controller sInstance;
	private Context mContext;
}