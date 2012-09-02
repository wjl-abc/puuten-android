package com.android.puuter;

import com.android.puuter.view.FriendDynInfoView;
import com.android.puuter.view.WaterFlowView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;

public class justtest extends TabActivity{

	public static void actionView(Context context) {
		Intent i = new Intent(context, justtest.class);
		context.startActivity(i);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justtest);
        mHost=this.getTabHost();
        
        mHost.addTab(mHost.newTabSpec("ONE").setIndicator("ONE")
        			.setContent(new Intent(this,WaterFlowView.class)));
        mHost.addTab(mHost.newTabSpec("TWO").setIndicator("TWO")
        		.setContent(new Intent(this,FriendDynInfoView.class)));
        
        mFlow2Button = (Button) findViewById(R.id.flows2);
        mFlow3Button = (Button) findViewById(R.id.flows3);
        
        mFlow2Button.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				mHost.setCurrentTabByTag("ONE");
			}
        });
        
        mFlow3Button.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				mHost.setCurrentTabByTag("TWO");
			}
        });
    }
    
    private TabHost mHost;
    private Button mFlow2Button;
    private Button mFlow3Button;
}
