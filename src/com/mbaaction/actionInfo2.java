package com.mbaaction;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class actionInfo2 extends Activity 
{
	private ScrollView m_oScroll = null;
	private LinearLayout m_oButtonLayout1 = null;
	private String m_sContent = null;
	private TextView m_oInfoText = null;
	
	private TextView m_oMarkText = null;
	private TextView m_oTitleText = null;
	private Button m_oPartInButton = null;
	private Button m_oMarkButton = null;
	private Button m_oBackButton = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actioninfo2);
       
        findView();
        init();
        setListen();
    }
	
	private void findView()
    {
		m_oScroll = (ScrollView)findViewById(R.id.scrollview);
		m_oButtonLayout1 = (LinearLayout)findViewById(R.id.buttonlayout1);
		m_oInfoText = (TextView)findViewById(R.id.infotext);
		
		m_oMarkText = (TextView)findViewById(R.id.marktext);
		m_oTitleText = (TextView)findViewById(R.id.titletext);
		
		m_oPartInButton = (Button)findViewById(R.id.partinbutton);
		m_oMarkButton = (Button)findViewById(R.id.markbutton);
		m_oBackButton = (Button)findViewById(R.id.backbutton);
    }
	
	private void setListen()
	{
		if( m_oPartInButton != null )
		{
			m_oPartInButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					
				}
			});
		}
		
		if( m_oMarkButton != null )
		{
			m_oMarkButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					
				}
			});
		}
		
		if( m_oBackButton != null )
		{
			m_oBackButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					finish();
				}
			});
		}
			
	}
	
	private boolean init()
	{
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    int width = dm.widthPixels;
	    int height = 0;
	    int height1 = 0;
	    if( m_oTitleText != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oTitleText.getLayoutParams();
    		lp.leftMargin = 0;
    		lp.topMargin = 0;
    		lp.width = width - 150;
    		m_oTitleText.setLayoutParams(lp);
    		
    		height = lp.height;
	    }
	    
	    if( m_oButtonLayout1 != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oButtonLayout1.getLayoutParams();
    		lp.leftMargin = width - 150;
    		lp.topMargin = 0;
    		m_oButtonLayout1.setLayoutParams(lp);
	    }
	   
	    if( m_oMarkText != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oMarkText.getLayoutParams();
    		lp.leftMargin = 0;
    		lp.topMargin = height;
    		lp.width = 200;
    		m_oMarkText.setLayoutParams(lp);
	    }
	    
	    if( m_oBackButton != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oBackButton.getLayoutParams();
    		lp.leftMargin = (dm.widthPixels-lp.width)/2;
	    	lp.topMargin = dm.heightPixels - 45 - lp.height;
    		m_oBackButton.setLayoutParams(lp);
    		
    		height1 = lp.topMargin;
	    }
	    
	    if( m_oScroll != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oScroll.getLayoutParams();
    		lp.leftMargin = 0;
    		lp.topMargin = height + 40;
    		lp.height = height1 - lp.topMargin;
    		m_oScroll.setLayoutParams(lp);
	    }
	    
	    Bundle bundle = getIntent().getExtras();
	    String title = null;
	    String mark = null;
		if( bundle != null )
		{
			m_sContent = bundle.getString("actioninfo");
			title = bundle.getString("title");
			mark = bundle.getString("mark");
		}

		if( TextUtils.isEmpty(m_sContent) )
			m_sContent = this.getResources().getString(R.string.noinfo);
		
		if( m_oInfoText != null )
			m_oInfoText.setText(m_sContent);
		
		if( !TextUtils.isEmpty(mark) && m_oMarkText != null )
			m_oMarkText.setText(mark);
		
		if( !TextUtils.isEmpty(title) && m_oTitleText != null )
			m_oTitleText.setText(title);
		
		return true;
	}

}
