package com.android.httptest;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private EditText username;
    private EditText passwd;
    private Button okButton;
    private TextView result;
    private MyHandler myHandler = new MyHandler();
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        username = (EditText) findViewById(R.id.username);
        passwd = (EditText) findViewById(R.id.passwd);
        okButton = (Button) findViewById(R.id.ok);
        result = (TextView) findViewById(R.id.result);

        okButton.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.v("username", username.getText().toString());
				Log.v("passwd", passwd.getText().toString());
				thread = new Thread(httpRunnable);
				thread.start();
			}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private Thread thread;
    private Runnable httpRunnable = new Runnable(){
    	public void run()
    	{
    		String strResult = null;
    		//http processing
			String url = "http://10.0.2.2:8000/account/login/";
//			String url = "http://www.puuter.com/account/login/";
//			String url = "http://192.168.1.104:8080/index.php";
			Log.v("url", url);
			HttpPost httpPost = new HttpPost(url);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username.getText().toString()));
			params.add(new BasicNameValuePair("password", passwd.getText().toString()));
			params.add(new BasicNameValuePair("mobile", "android"));
			try{
				HttpEntity httpEntity = new UrlEncodedFormEntity(params, "gb2312");
				httpPost.setEntity(httpEntity);
				HttpClient httpClient = new DefaultHttpClient();
				HttpResponse httpResponse = httpClient.execute(httpPost);
				
				if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
					strResult = EntityUtils.toString(httpResponse.getEntity());
					strResult.replaceAll("\r", "");
					Log.v("response", strResult);
				}else{
					Log.v("response", "connect fail");
				}
			}catch(ClientProtocolException e){
				Log.v("reponse", "ClientProtocolException"+e.getMessage().toString());
			}catch(IOException e){
				Log.v("reponse", "IOException"+e.getMessage().toString());
			}catch(Exception e){
				Log.v("reponse", "Exception"+e.getMessage().toString());
			}
			strResult = strResult==null ? "failed" : strResult;
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putString("result", strResult);
			msg.setData(b);
			MainActivity.this.myHandler.sendMessage(msg);
    	}
    };
    
    private class MyHandler extends Handler {
        public MyHandler() {
        }

        // 子类必须重写此方法,接受数据
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle b = msg.getData();
            String strResult = b.getString("result");
            Log.v("handler", strResult);
            MainActivity.this.result.setText(strResult);
        }
    }
}
