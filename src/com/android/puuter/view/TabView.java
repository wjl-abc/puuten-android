package com.android.puuter.view;

import com.android.puuter.R;
import com.android.puuter.justtest;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class TabView extends TabActivity implements OnCheckedChangeListener{

	public static void actionView(Context context) {
		Intent i = new Intent(context, TabView.class);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);
        mHost=this.getTabHost();
        
        mHost.addTab(mHost.newTabSpec("ONE").setIndicator("ONE")
        			.setContent(new Intent(this,WaterFlowView.class)));
        mHost.addTab(mHost.newTabSpec("TWO").setIndicator("TWO")
        		.setContent(new Intent(this,FriendDynInfoView.class)));
        mHost.addTab(mHost.newTabSpec("THREE").setIndicator("THREE")
        		.setContent(new Intent(this,justtest.class)));
//        mHost.addTab(mHost.newTabSpec("FOUR").setIndicator("FOUR")
//        		.setContent(new Intent(this,FourActivity.class)));
//        mHost.addTab(mHost.newTabSpec("FIVE").setIndicator("FIVE")
//        		.setContent(new Intent(this,FiveActivity.class)));
        
        radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioderGroup.setOnCheckedChangeListener(this);
    }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button0:
			mHost.setCurrentTabByTag("ONE");
			break;
		case R.id.radio_button1:
			mHost.setCurrentTabByTag("TWO");
			break;
		case R.id.radio_button2:
			mHost.setCurrentTabByTag("THREE");
			break;
//		case R.id.radio_button3:
//			mHost.setCurrentTabByTag("FOUR");
//			break;
//		case R.id.radio_button4:
//			mHost.setCurrentTabByTag("FIVE");
//			break;
		}		
	}
	
	private TabHost mHost;
	private RadioGroup radioderGroup;
}
