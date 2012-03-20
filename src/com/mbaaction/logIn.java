package com.mbaaction;

import org.apache.http.client.HttpClient;

import com.util.ActionInfo;
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
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class logIn extends Activity 
{
	private LinearLayout m_oLayout = null;
	private EditText m_oNameEdit = null;
	private EditText m_oPassWordEdit = null;
	private Button m_oLogInButton = null;
	
	private HttpClient m_oHttpClient = null;
	private Handler m_oRetHandler = null;
	private ProgressDialog m_oPDialog = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        findView();
        createHandler();
        initParams();

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
        	
        	String str = (String)msg.obj;
        	if( !TextUtils.isEmpty(str) )
        	{
        		Toast toast = Toast.makeText(this, str, Toast.LENGTH_LONG);
        		toast.setDuration(1000);
        		toast.setGravity(Gravity.CENTER, 0, 0);
        		toast.show();
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
        		bundle.putString("password", m_oPassWordEdit.getText().toString() );
        		if( ui.m_vFollowEvents != null && ui.m_vFollowEvents.size() > 0 )
        		{
        			int len = ui.m_vFollowEvents.size();
        			String[] followIds = new String[len];
        			
        			for( int i = 0; i < len; i++ )
        			{
        				ActionInfo ai = ui.m_vFollowEvents.get(i);
        				if( ai == null || TextUtils.isEmpty(ai.m_sEventId) )
        					continue;
        				followIds[i] = ai.m_sEventId;
        			}
        			
        			bundle.putStringArray("followids", followIds);
        		}
        		if( ui.m_vJoinEvents != null && ui.m_vJoinEvents.size() > 0 )
        		{
        			int len = ui.m_vJoinEvents.size();
        			String[] joinIds = new String[len];
        			
        			for( int i = 0; i < len; i++ )
        			{
        				ActionInfo ai = ui.m_vJoinEvents.get(i);
        				if( ai == null || TextUtils.isEmpty(ai.m_sEventId) )
        					continue;
        				joinIds[i] = ai.m_sEventId;
        			}
        			
        			bundle.putStringArray("joinids", joinIds);
        		}

        		intent.putExtras(bundle);
        		
        		setResult(RESULT_OK, intent);
    			
    			finish();
        	}
        	break;
        default:
        	break;
        }
	}
	
    private void findView()
    {
    	m_oLayout = (LinearLayout)findViewById(R.id.layout);
    	m_oNameEdit = (EditText)findViewById(R.id.nameedit);
    	m_oPassWordEdit = (EditText)findViewById(R.id.passwordedit);
    	m_oLogInButton = (Button)findViewById(R.id.loginbutton);
    }
    
    private void initParams()
    {
    	DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
    	if( m_oLayout != null )
    	{
    		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oLayout.getLayoutParams();
    		lp.topMargin = (dm.heightPixels-lp.height)/2;
    		m_oLayout.setLayoutParams(lp);
    	}
    	
    	if( m_oNameEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oNameEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oNameEdit.setLayoutParams(lp);
    	}
    	
    	if( m_oPassWordEdit != null )
    	{
    		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oPassWordEdit.getLayoutParams();
    		lp.width = dm.widthPixels-160-10;
    		m_oPassWordEdit.setLayoutParams(lp);
    		
    		m_oPassWordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    	}
    	
    	if( m_oLogInButton != null )
    	{
    		m_oLogInButton.setOnClickListener(new Button.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//向服务器端提交登录信息，登录不成功提醒用户，成功则返回mbaAction登录后模式
					
					String nameStr = m_oNameEdit.getText().toString();
					String passwordStr = m_oPassWordEdit.getText().toString();
					if( TextUtils.isEmpty(nameStr) ||  TextUtils.isEmpty(passwordStr) )
					{
						Toast toast = Toast.makeText(logIn.this, "资料填写不完整", Toast.LENGTH_LONG);
		        		toast.setDuration(1000);
		        		toast.setGravity(Gravity.CENTER, 0, 0);
		        		toast.show();
		        		
		        		return;
					}
					
					UserInfo ui = new UserInfo();
					ui.m_sEmail = m_oNameEdit.getText().toString();
					ui.m_sPassWord = MD5Utils.toMd5(m_oPassWordEdit.getText().toString());
					HttpThread ht = new HttpThread( m_oHttpClient, "http://124.205.252.248:3000/webservice/query", m_oRetHandler );
			        ht.setModeInfo(4, "");
			        ht.setUserInfo(ui);
			        
			        m_oPDialog = ProgressDialog.show( logIn.this, "请稍等", "获取数据中..." );
			        ht.start();
			        
			        
				}
			});
    	}
    	
    	actionData ad = (actionData)getApplication();
	    if( ad != null )
	    	m_oHttpClient = ad.getHttpClient();
	    
    }
}
