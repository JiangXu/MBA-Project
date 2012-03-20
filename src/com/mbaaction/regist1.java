package com.mbaaction;

import org.apache.http.client.HttpClient;

import com.util.HttpThread;
import com.util.MD5Utils;
import com.util.MessageCode;
import com.util.UserInfo;
import com.util.actionData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class regist1 extends Activity 
{
	private LinearLayout m_oLayout1 = null;
	private LinearLayout m_oLayout2 = null;
	private EditText m_oPhoneEdit = null;
	private Button m_oNextButton = null;
	private Button m_oJumpButton = null;
	
	private HttpClient m_oHttpClient = null;
	
	private Handler m_oRetHandler = null;
	private ProgressDialog m_oPDialog = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist1);
        
        findView();
        createHandler();
        initParams();
    }
    
    private void findView()
    {
    	m_oLayout1 = (LinearLayout)findViewById(R.id.layout1);
    	m_oLayout2 = (LinearLayout)findViewById(R.id.layout2);
    	m_oPhoneEdit = (EditText)findViewById(R.id.phoneedit);
    	m_oNextButton = (Button)findViewById(R.id.nextbutton);
    	m_oJumpButton = (Button)findViewById(R.id.next1button);
    }
    
    private boolean createHandler()
	{
		if( m_oRetHandler != null )
		{
			return true;
		}
		m_oRetHandler = new Handler()
		{
            public void handleMessage(Message msg) 
            {
            	super.handleMessage(msg);
            	HandleMsg(msg);
            }
       };
       return( m_oRetHandler != null );
	}
	
	private void HandleMsg( Message msg ) 
	{
        switch(msg.what)
        {
        case MessageCode.MSG_HTTP_STATE:
        	if( m_oPDialog != null )
        	{
        		m_oPDialog.dismiss();
        		m_oPDialog = null;
        	}
        	
        	int ret = msg.arg1;
        	String str = (String)msg.obj;
        	if( !TextUtils.isEmpty(str) )
        	{
        		Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        		toast.setDuration(1000);
        		toast.setGravity(Gravity.CENTER, 0, 0);
        		toast.show();
        	}
        
        	if( ret == -1 )
        	{
        		Intent intent = new Intent();
    			intent.setClass( regist1.this, regist3.class );
    			startActivity(intent);
    			
    			finish();
        	}
        	
        	break;
        case MessageCode.MSG_GET_USER_INFO:
        	UserInfo ui = (UserInfo)msg.obj;
        	if( ui != null && !TextUtils.isEmpty(ui.m_sUserName) &&
        			!TextUtils.isEmpty(ui.m_sMobile) && !TextUtils.isEmpty(ui.m_sEmail) )
        	{
        		Intent intent = new Intent();
        		Bundle bundle = new Bundle();
        		bundle.putString("name", ui.m_sUserName);
        		bundle.putString("mobile", ui.m_sMobile);
        		bundle.putString("email", ui.m_sEmail);
        		intent.putExtras(bundle);
    			intent.setClass( regist1.this, regist2.class );
    			startActivity(intent);
    			
    			finish();
        	}
        	break;
        default:
        	break;
        }
	}
	
    private void initParams()
    {
    	DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    if( m_oLayout1 != null )
    	{
    		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oLayout1.getLayoutParams();
    		lp.topMargin = 100;
    		m_oLayout1.setLayoutParams(lp);
    	}
	    
	    if( m_oLayout2 != null )
    	{
    		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oLayout2.getLayoutParams();
    		lp.topMargin = dm.heightPixels - 45 - 100 - lp.height;
    		lp.leftMargin = (dm.widthPixels-lp.width)/2;
    		m_oLayout2.setLayoutParams(lp);
    	}
	    
	    if( m_oPhoneEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oPhoneEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oPhoneEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oNextButton != null )
    	{
	    	m_oNextButton.setOnClickListener(new Button.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//向服务器提交手机验证，如果是清华ＭＢＡ返回学员数据并启动
					//注册清华ＭＢＡ学员页，否则启动非清华ＭＢＡ学员提示页
					
					String phoneStr = m_oPhoneEdit.getText().toString();
					if( TextUtils.isEmpty(phoneStr) )
					{
						Toast toast = Toast.makeText(regist1.this, "请填写手机号", Toast.LENGTH_LONG);
		        		toast.setDuration(1000);
		        		toast.setGravity(Gravity.CENTER, 0, 0);
		        		toast.show();
		        		
		        		return;
					}
					
					UserInfo ui = new UserInfo();
					ui.m_sMobile = m_oPhoneEdit.getText().toString();
					
					HttpThread ht = new HttpThread( m_oHttpClient, "http://124.205.252.248:3000/webservice/query", m_oRetHandler );
			        ht.setModeInfo(5, "");
			        ht.setUserInfo(ui);
			        
			        m_oPDialog = ProgressDialog.show( regist1.this, "请稍等", "获取数据中..." );
			        ht.start();
				}
			});
    	}
	    
	    if( m_oJumpButton != null )
    	{
	    	m_oJumpButton.setOnClickListener(new Button.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//跳过按钮直接进入注册非清华ＭＢＡ学员页
					Intent intent = new Intent();
					intent.setClass( regist1.this, regist4.class );
					startActivity(intent);
					
					finish();
				}
			});
    	}
	    
	    actionData ad = (actionData)getApplication();
	    if( ad != null )
	    	m_oHttpClient = ad.getHttpClient();
	        
    }
	
}
