package com.mbaaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class regist3 extends Activity 
{
	private Button m_oNextButton = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist3);
        
        findView();
        
        initParams();
    }
    
    private void findView()
    {
    	m_oNextButton = (Button)findViewById(R.id.nextbutton);
    }
    
    private void initParams()
    {
    	DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    if( m_oNextButton != null )
    	{
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oNextButton.getLayoutParams();
    		lp.topMargin = dm.heightPixels - 45 - 100 - lp.height;
    		lp.leftMargin = (dm.widthPixels-lp.width)/2;
    		m_oNextButton.setLayoutParams(lp);
    		
	    	m_oNextButton.setOnClickListener(new Button.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//启动非清华ＭＢＡ注册页
					Intent intent = new Intent();
					intent.setClass( regist3.this, regist4.class );
					startActivity(intent);
					
					finish();
				}
			});
    	}
	    
	    
    }

}
