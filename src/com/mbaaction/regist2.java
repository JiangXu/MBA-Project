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

public class regist2 extends Activity 
{
	private LinearLayout m_oLayout = null;
	private EditText m_oNameEdit = null;
	private EditText m_oMailEdit = null;
	private EditText m_oPhoneEdit = null;
	private EditText m_oPassWordEdit = null;
	private EditText m_oConfirmEdit = null;
	private Button m_oFinishButton = null;
	
	private Handler m_oHandler = null;
	private HttpClient m_oHttpClient = null;
	private Handler m_oRetHandler = null;
	private ProgressDialog m_oPDialog = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist2);
        
        findView();
        createHandler();
        initParams();
    }
    
    private void findView()
    {
    	m_oLayout = (LinearLayout)findViewById(R.id.layout);
    	
    	m_oNameEdit = (EditText)findViewById(R.id.nameedit);
    	m_oMailEdit = (EditText)findViewById(R.id.mailedit);
    	m_oPhoneEdit = (EditText)findViewById(R.id.phoneedit);
    	m_oPassWordEdit = (EditText)findViewById(R.id.passwordedit);
    	m_oConfirmEdit = (EditText)findViewById(R.id.confirmedit);
    	m_oFinishButton = (Button)findViewById(R.id.finishbutton);
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
        	
        	//向服务器提交注册信息，失败提示用户，成功返回mbaAction登录后模式
        	if( ret == 0 )
        	{
				if( m_oHandler != null )
				{
					UserInfo ui = new UserInfo();
					ui.m_sUserName = m_oNameEdit.getText().toString();
					ui.m_sEmail = m_oMailEdit.getText().toString();
					ui.m_sPassWord = MD5Utils.toMd5(m_oPassWordEdit.getText().toString());
					ui.m_sMobile = m_oPhoneEdit.getText().toString();
					
					Message m = new Message();
					m.what = MessageCode.MSG_GET_REGIST_INFO;
					m.obj = ui;
					
					m_oHandler.sendMessage(m);
				}
				
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
	    
	    if( m_oLayout != null )
    	{
    		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oLayout.getLayoutParams();
    		lp.topMargin = (dm.heightPixels-45-lp.height)/2;
    		m_oLayout.setLayoutParams(lp);
    	}
	    
	    if( m_oNameEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oNameEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oNameEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oMailEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oMailEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oMailEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oPhoneEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oPhoneEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oPhoneEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oPassWordEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oPassWordEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oPassWordEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oConfirmEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oConfirmEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oConfirmEdit.setLayoutParams(lp);
    	}
	    
	    if( m_oFinishButton != null )
    	{
	    	m_oFinishButton.setOnClickListener(new Button.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//向服务器提交注册信息，失败提示用户，成功返回mbaAction登录后模式
					 
					String password = m_oPassWordEdit.getText().toString();
					String confirmpass = m_oConfirmEdit.getText().toString();
					String name = m_oNameEdit.getText().toString();
					String mail = m_oMailEdit.getText().toString();
					if( TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmpass) ||
							TextUtils.isEmpty(name) || TextUtils.isEmpty(mail) )
					{
						Toast toast = Toast.makeText(regist2.this, "资料填写不完整", Toast.LENGTH_LONG);
		        		toast.setDuration(1000);
		        		toast.setGravity(Gravity.CENTER, 0, 0);
		        		toast.show();
		        		
		        		return;
					}
					
					if( !password.equals(confirmpass) )
					{
						Toast toast = Toast.makeText(regist2.this, "密码不一致", Toast.LENGTH_LONG);
		        		toast.setDuration(1000);
		        		toast.setGravity(Gravity.CENTER, 0, 0);
		        		toast.show();
		        		
		        		return;
					}
					
					if( m_oHttpClient != null )
					{
						UserInfo ui = new UserInfo();
						ui.m_sUserName = m_oNameEdit.getText().toString();
						ui.m_sEmail = m_oMailEdit.getText().toString();
						ui.m_sPassWord = MD5Utils.toMd5(m_oPassWordEdit.getText().toString());
						ui.m_sMobile = m_oPhoneEdit.getText().toString();
						
						HttpThread ht = new HttpThread( m_oHttpClient, "http://124.205.252.248:3000/webservice/query", m_oRetHandler );
				        ht.setModeInfo(2, "");
				        ht.setUserInfo(ui);
				        
				        m_oPDialog = ProgressDialog.show( regist2.this, "请稍等", "注册中..." );
				        ht.start();
					}
					
				}
			});
    	}
	    
	    actionData ad = (actionData)getApplication();
	    if( ad != null )
	    {
	    	m_oHandler = ad.getHandler();
	    	m_oHttpClient = ad.getHttpClient();
	    }
	    
	    Bundle bundle = getIntent().getExtras();
		if( bundle != null )
		{
			m_oNameEdit.setText(bundle.getString("name"));
			m_oPhoneEdit.setText(bundle.getString("mobile"));
			m_oMailEdit.setText(bundle.getString("email"));
		}
	    
    }

}
