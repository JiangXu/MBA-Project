package com.mbaaction;

import org.apache.http.client.HttpClient;

import com.util.ActionInfo;
import com.util.HttpThread;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class actionInfo extends Activity 
{
	private ScrollView m_oScroll = null;
	private LinearLayout m_oButtonLayout1 = null;
	private LinearLayout m_oButtonLayout2 = null;
	private TextView m_oInfoText = null;
	
	private Button m_oPartInButton = null;
	private Button m_oMarkButton = null;
	private Button m_oSpeakerButton = null;
	private Button m_oActionButton = null;
	private Button m_oOtherButton = null;
	private Button m_oBackButton = null;
	
	private ActionInfo m_oActionInfo = null;
	private UserInfo m_oUserInfo = null;
	
	private HttpClient m_oHttpClient = null;
	private Handler m_oRetHandler = null;
	private ProgressDialog m_oPDialog = null;
	
	private int m_iMode = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actioninfo);
        findView();
        createHandler();
        init();
        setListen();
    }
	
	private void findView()
    {
		m_oScroll = (ScrollView)findViewById(R.id.scrollview);
		m_oButtonLayout1 = (LinearLayout)findViewById(R.id.buttonlayout1);
		m_oButtonLayout2 = (LinearLayout)findViewById(R.id.buttonlayout2);
		m_oInfoText = (TextView)findViewById(R.id.infotext);
		
		m_oPartInButton = (Button)findViewById(R.id.partinbutton);
		m_oMarkButton = (Button)findViewById(R.id.markbutton);
		m_oSpeakerButton = (Button)findViewById(R.id.speakerbutton);
		m_oActionButton = (Button)findViewById(R.id.actionbutton);
		m_oOtherButton = (Button)findViewById(R.id.otherbutton);
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
					//用户未报名页面
					//向服务器提交报名信息，成功与失败都要告知用户
					//用户已报名页面
					//向服务器提交退报信息，成功与失败都要告知用户
					//组织者页面
					//从服务器得到报名人员信息，启动查看报名详情页面
					
					if( m_oActionInfo == null || TextUtils.isEmpty(m_oActionInfo.m_sEventId) 
							|| m_oUserInfo == null || TextUtils.isEmpty(m_oUserInfo.m_sEmail)
								|| TextUtils.isEmpty(m_oUserInfo.m_sPassWord) )
						return;
					
					HttpThread ht = null;
					if( m_iMode == 1 || m_iMode == 2 )
					{
				        ht = new HttpThread( m_oHttpClient, "http://117.79.88.173:3000/webservice/query/", m_oRetHandler );
				        ht.setModeInfo(8, m_oActionInfo.m_sEventId);
				        ht.setUserInfo(m_oUserInfo);
					}
					else if( m_iMode == 3 || m_iMode == 4 )
					{
						ht = new HttpThread( m_oHttpClient, "http://117.79.88.173:3000/webservice/query/", m_oRetHandler );
				        ht.setModeInfo(7, m_oActionInfo.m_sEventId);
				        ht.setUserInfo(m_oUserInfo);
					}
					
					m_oPDialog = ProgressDialog.show( actionInfo.this, "请稍等", "关注中..." );
			        ht.start();
					
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
					//用户未报名页面
					//向服务器提交关注信息，成功与失败都要告知用户
					//用户已报名页面
					//从服务器得到报名人员信息，启动查看报名详情页面
					//组织者页面
					//从服务器得到关注人员信息，启动查看关注详情页面
					
					if( m_oActionInfo == null || TextUtils.isEmpty(m_oActionInfo.m_sEventId) 
							|| m_oUserInfo == null || TextUtils.isEmpty(m_oUserInfo.m_sEmail)
								|| TextUtils.isEmpty(m_oUserInfo.m_sPassWord) )
						return;
					
					HttpThread ht = null;
					if( m_iMode == 1 || m_iMode == 3 )
					{
				        ht = new HttpThread( m_oHttpClient, "http://117.79.88.173:3000/webservice/query/", m_oRetHandler );
				        ht.setModeInfo(10, m_oActionInfo.m_sEventId);
				        ht.setUserInfo(m_oUserInfo);
					}
					else if( m_iMode == 2 || m_iMode == 4 )
					{
						ht = new HttpThread( m_oHttpClient, "http://117.79.88.173:3000/webservice/query/", m_oRetHandler );
				        ht.setModeInfo(9, m_oActionInfo.m_sEventId);
				        ht.setUserInfo(m_oUserInfo);
					}
					
					m_oPDialog = ProgressDialog.show( actionInfo.this, "请稍等", "关注中..." );
			        ht.start();
				}
			});
		}
		
		if( m_oSpeakerButton != null )
		{
			m_oSpeakerButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					//启动查看演讲者信息页面
					if( m_oActionInfo == null )
						return;
					
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
			
					bundle.putString( "actioninfo", m_oActionInfo.m_sSpeakerInfo );
					String mark = (String)m_oSpeakerButton.getText();
					bundle.putString( "title", m_oActionInfo.m_sTitle );
					bundle.putString( "mark", mark );
					intent.putExtras(bundle);
					
					intent.setClass( actionInfo.this, actionInfo2.class );
					startActivity(intent);
				}
			});
		}
		
		if( m_oActionButton != null )
		{
			m_oActionButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if( m_oActionInfo == null )
						return;
					
					//启动查看活动详细信息页面
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					
					bundle.putString( "actioninfo", m_oActionInfo.m_sIntroduction );
					String title = "活动1";
					String mark = (String)m_oActionButton.getText();
					bundle.putString( "title", m_oActionInfo.m_sTitle );
					bundle.putString( "mark", mark );
					intent.putExtras(bundle);
					
					intent.setClass( actionInfo.this, actionInfo2.class );
					startActivity(intent);
				}
			});
		}
		
		if( m_oOtherButton != null )
		{
			m_oOtherButton.setOnClickListener(new Button.OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					if( m_oActionInfo == null )
						return;
					
					//启动查看其它详细信息页面
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					
					bundle.putString( "actioninfo", m_oActionInfo.m_sOther );
					String mark = (String)m_oOtherButton.getText();
					bundle.putString( "title", m_oActionInfo.m_sTitle );
					bundle.putString( "mark", mark );
					intent.putExtras(bundle);
					
					intent.setClass( actionInfo.this, actionInfo2.class );
					startActivity(intent);
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
        case MessageCode.MSG_GET_PARTIN_LIST:
        	break;
        default:
        	break;
        }
	}
	
	private String getShowText()
	{
		String info = "";
		if( m_oActionInfo == null )
			return info;
		if( !TextUtils.isEmpty(m_oActionInfo.m_sTitle) )
			info += m_oActionInfo.m_sTitle+"\n";
		else
			info += "\n";
		info += "\n";
		
		info += "活动状态："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sStatus) )
		{
			if( m_oActionInfo.m_sStatus.equals("0") )
				info += "正在报名"+"\n";
			else if( m_oActionInfo.m_sStatus.equals("1") )
				info += "报名结束"+"\n";
			else
				info += "\n";
		}
		else
			info += "\n";
		
		info +="活动时间："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sBeginTime) )
			info += m_oActionInfo.m_sBeginTime+"\n";
		else
			info += "\n";
		
		info +="活动地点："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sAddress) )
			info += m_oActionInfo.m_sAddress+"\n";
		else
			info += "\n";
		
		info +="参与人数："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sSeat) )
		{
			String[] strs = m_oActionInfo.m_sSeat.split("[/]");
			if( strs.length == 3 )
			{
				if( !TextUtils.isEmpty( strs[0] ) )
				{
					info += strs[0]+"人"+" ";
					if( !TextUtils.isEmpty( strs[2] ) )
					{
						info += "还剩"+ (Integer.parseInt(strs[2])-Integer.parseInt(strs[0])) +"席"+"\n";	
					}
					else
						info += "\n";
				}
				else
					info += "\n";
			}
			else
				info += "\n";
		}
		else
			info += "\n";
		
		info +="主讲人："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sSpeakerName) )
			info += m_oActionInfo.m_sSpeakerName+"\n";
		else
			info += "\n";
		
		info +="主办方："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sSponsorName) )
			info += m_oActionInfo.m_sSponsorName+"\n";
		else
			info += "\n";
		
		info +="承办方："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sUndertakerName) )
			info += m_oActionInfo.m_sUndertakerName+"\n";
		else
			info += "\n";
		
		info +="协办方："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sCooperaterName) )
			info += m_oActionInfo.m_sCooperaterName+"\n";
		else
			info += "\n";
		
		info +="人员需求："+"\n";
		if( !TextUtils.isEmpty(m_oActionInfo.m_sAuthentication) )
		{
			if( m_oActionInfo.m_sAuthentication.equals("0") )
				info += "任何人"+"\n";
			else if( m_oActionInfo.m_sAuthentication.equals("1") )
				info += "仅俱乐部成员"+"\n";
			else
				info += "\n";
		}
		else
			info += "\n";
		
		return info;
	}
	
	private boolean init()
	{
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    
	    int width = dm.widthPixels;
	    int height = 0;
	   
	    if( m_oButtonLayout1 != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oButtonLayout1.getLayoutParams();
    		lp.leftMargin = width - 150;
    		lp.topMargin = 0;
    		m_oButtonLayout1.setLayoutParams(lp);
	    }
	    
	    if( m_oButtonLayout2 != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oButtonLayout2.getLayoutParams();
    		lp.leftMargin = 0;
    		lp.topMargin = dm.heightPixels - 45 - 100;
    		m_oButtonLayout2.setLayoutParams(lp);
    		
    		height = lp.topMargin;
	    }
	    
	    if( m_oScroll != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oScroll.getLayoutParams();
    		lp.leftMargin = 0;
    		lp.topMargin = 0;
    		lp.width = width - 150;
    		lp.height = height;
    		m_oScroll.setLayoutParams(lp);
	    }
	    
	    Bundle bundle = getIntent().getExtras();
		if( bundle != null )
		{
			m_oActionInfo = new ActionInfo();
			m_oActionInfo.m_sEventId = bundle.getString("eventid");
			m_oActionInfo.m_sTitle = bundle.getString("title");
			m_oActionInfo.m_sSchoolId = bundle.getString("schoolid");
			m_oActionInfo.m_sSchoolName = bundle.getString("schoolname");
			m_oActionInfo.m_sOrgId = bundle.getString("orgid");
			m_oActionInfo.m_sOrgName = bundle.getString("orgname");
			m_oActionInfo.m_sSponsorName = bundle.getString("sponsorname");
			m_oActionInfo.m_sUndertakerName = bundle.getString("undertakername");
			m_oActionInfo.m_sCooperaterName = bundle.getString("cooperatername");
			m_oActionInfo.m_sAddress = bundle.getString("address");
			m_oActionInfo.m_sBeginTime = bundle.getString("begintime");
			m_oActionInfo.m_sEndTime = bundle.getString("endtime");
			m_oActionInfo.m_sSpeakerName = bundle.getString("speakername");
			m_oActionInfo.m_sSpeakerInfo = bundle.getString("speakerinfo");
			m_oActionInfo.m_sSeat = bundle.getString("seat");
			m_oActionInfo.m_sIntroduction = bundle.getString("introduction");
			m_oActionInfo.m_sStatus = bundle.getString("status");
			m_oActionInfo.m_sAuthentication = bundle.getString("authentication");
			m_oActionInfo.m_sOther = bundle.getString("other");
			
			m_iMode = bundle.getInt("mode");
			
			m_oUserInfo = new UserInfo();
			m_oUserInfo.m_sEmail = bundle.getString("email");
			m_oUserInfo.m_sPassWord = bundle.getString("password");
			m_oUserInfo.m_sUserName = bundle.getString("name");
			m_oUserInfo.m_sMobile = bundle.getString("mobile");
			
		}

		if( m_oPartInButton != null && m_oMarkButton != null )
		{
			switch( m_iMode )
			{
			case 1:
				m_oPartInButton.setText("退出报名！");
				m_oMarkButton.setText("退出关注！");
				break;
			case 2:
				m_oPartInButton.setText("退出报名！");
				m_oMarkButton.setText("关注！");
				break;
			case 3:
				m_oPartInButton.setText("抢座！");
				m_oMarkButton.setText("退出关注！");
				break;
			case 4:
				m_oPartInButton.setText("抢座！");
				m_oMarkButton.setText("关注！");
				break;
			default:
				break;
			}
		}
		
		String str = getShowText();
		
		if( TextUtils.isEmpty(str) )
			str = this.getResources().getString(R.string.noinfo);
		
		if( m_oInfoText != null )
			m_oInfoText.setText(str);
		
		actionData ad = (actionData)getApplication();
	    if( ad != null )
	    	m_oHttpClient = ad.getHttpClient();
	    
		return true;
	}
}
