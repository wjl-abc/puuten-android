package com.android.puuter.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.puuter.setting.Setting;

public final class HttpController{
	protected HttpController(Context context){
		mContext = context;
	}
	
	public synchronized static HttpController getInstance(Context context){
		if(mHttpController == null){
			mHttpController = new HttpController(context);
		}
		return mHttpController;
	}
	
	public int loginRemote(String url, HashMap<String, String> parmsMap){
		HttpPost httpPost = new HttpPost(url);
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		Iterator iterator = parmsMap.keySet().iterator();                
        while (iterator.hasNext()) {    
         Object key = iterator.next();  
         params.add(new BasicNameValuePair(key.toString(), parmsMap.get(key)));
        }
		try {
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, "gb2312");
			httpPost.setEntity(httpEntity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);
			mCookiestore=httpClient.getCookieStore();
			String strResult = EntityUtils.toString(httpResponse.getEntity());
			strResult.replaceAll("\r", "");
			return Integer.parseInt(strResult);
		} catch (ClientProtocolException e) {
			Log.e("reponse", "ClientProtocolException: " + e.getMessage().toString());
		} catch (IOException e) {
			Log.e("reponse", "IOException: " + e.getMessage().toString());
		} catch (Exception e) {
			Log.e("reponse", "Exception: " + e.getMessage().toString());
		}
		return -1;
	}
	
	public String retrieveRemoteData(String url, HashMap<String, String> parmsMap){
		String strResult = null;
		if(mCookiestore == null){
			Log.v(TAG, "You have not login");
			return strResult;
		}
		
		try{
			HttpPost httpPost = new HttpPost(url);
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			Iterator iterator = parmsMap.keySet().iterator();                
            while (iterator.hasNext()) {    
             Object key = iterator.next();  
             params.add(new BasicNameValuePair(key.toString(), parmsMap.get(key)));
            }
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, "gb2312");
			httpPost.setEntity(httpEntity);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.setCookieStore(mCookiestore);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				strResult = EntityUtils.toString(httpResponse.getEntity()).replaceAll("\r", "");
//				Log.d(TAG, strResult);
			}else{
				Log.e(TAG, "connect fail");
			}
		}catch(ClientProtocolException e){
			Log.e(TAG, "ClientProtocolException"+e.getMessage().toString());
		}catch(IOException e){
			Log.e(TAG, "IOException"+e.getMessage().toString());
		}catch(Exception e){
			Log.e(TAG, "Exception"+e.getMessage().toString());
		}
		
		return strResult;
	}
	
	public Drawable loadImage(String url){
		try{
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = httpClient.execute(request);
			InputStream is = response.getEntity().getContent();
			Drawable drawable = Drawable.createFromStream(is, "image");
			return drawable;
		}catch (ClientProtocolException e) {
            Log.w(TAG, "ClientProtocolException:" + e.toString());
        }catch (IOException e) {
            Log.w(TAG, "IOException:" + e.toString());
        }catch(Exception e){
            Log.w(TAG, "Exception:" + e.toString());
        }
		return null;
	}
	
	private static HttpController mHttpController;
	private Context mContext;
	private static CookieStore mCookiestore;
	private final String TAG = "HttpController";
}

