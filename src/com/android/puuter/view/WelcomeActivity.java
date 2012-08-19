package com.android.puuter.view;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.puuter.R;
import com.android.puuter.controller.Controller;
import com.android.puuter.model.WaterFlow;
import com.android.puuter.model.WbDetail;

public class WelcomeActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        
        initView();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_welcome, menu);
//        return true;
//    }
    
    private void initView(){
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mOkButton = (Button) findViewById(R.id.okButton);
        mCancelButton = (Button) findViewById(R.id.cancelButton);
        mLoginProgressBar = (ProgressBar) findViewById(R.id.loginprogressbar);
        
        mControllerCallback = new ControllerResults();
        mHandler = new LoginHandler();
        mController = Controller.getInstance(mContext);
        
        mLoginProgressBar.setVisibility(View.GONE);
        
        mOkButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				new Thread(){
					public void run(){
						String username = mUsername.getText().toString();
						String password = mPassword.getText().toString();
						mController.loginServer(mContext, username, password, mControllerCallback);
					}
				}.start();
			}
        });
    }

    private EditText mUsername;
    private EditText mPassword;
    private Button mOkButton;
    //not used
    private Button mCancelButton;
    private ProgressBar mLoginProgressBar;
    
    private ControllerResults mControllerCallback;
    private LoginHandler mHandler;
    private Controller mController;
    private Context mContext = this;
    
    private String TAG = "login";
    
    private class ControllerResults implements Controller.Result{
    	public void loginServerCallBack(Context context, int progress){
    		switch(progress){
    		case 0:
    			mHandler.progress(0);
//    			Log.d(TAG, "login begin");
    			break;
    		case 100:
    			mHandler.progress(100);
//    			Log.d(TAG, "login success");
    			break;
    		case -1:
    			mHandler.progress(-1);
    			Log.d(TAG, "login canceled or exception occured");
    		}
    	}
    	
    	public void downloadResource(boolean status, WaterFlow waterFlow){
    	}
		
		public void downloadResourceWbDetail(boolean status, WbDetail wbd){
		}
    }
    
    private class LoginHandler extends Handler{
    	private static final int MSG_PROGRESS = 1;
    	
    	public void handleMessage(android.os.Message msg){
    		switch(msg.what){
    		case MSG_PROGRESS:
    			if(msg.arg1 == 0){	
    				mLoginProgressBar.setVisibility(View.VISIBLE);
    			}else if(msg.arg1 == 100){
    				mLoginProgressBar.setVisibility(View.GONE);
    				TabView.actionView(mContext);
    			}else if(msg.arg1 < 0){
    				mLoginProgressBar.setVisibility(View.GONE);
        			Toast.makeText(mContext, "login canceled or exception occured", Toast.LENGTH_LONG).show();
    			}
    			break;
    		default:
    			super.handleMessage(msg);
    		}
    	}
    	
    	public void progress(int progress){
    		android.os.Message msg = android.os.Message.obtain(this, MSG_PROGRESS);
    		msg.arg1 = progress;
    		sendMessage(msg);
    	}
    }
}
