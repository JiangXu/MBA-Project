package com.mbaaction;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.util.ActionInfo;
import com.util.HttpThread;
import com.util.ListLayoutInterface;
import com.util.MD5Utils;
import com.util.MessageCode;
import com.util.SchoolInfo;
import com.util.TextImageAdapter;
import com.util.UserInfo;
import com.util.actionData;
import com.util.mySpinnerAdapter;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class mbaAction extends Activity implements ListLayoutInterface
{
	private ListView m_oListView = null;
	private Handler m_oHandler = null;
	
	private EditText m_oSearchEdit = null;
	private Spinner m_oSchoolSpinner = null;
	private Spinner m_oActionSpinner = null;
	private TextView m_oSearchText = null;
	private TextView m_oLoginText = null;
	private TextView m_oRegistText = null;
	private TextView m_oNameText = null;

	private ProgressDialog m_oPDialog = null;
	private int m_iWidth = 0;
	
	private ActionInfo[] m_oaActionArray = null;
	private HttpClient m_oHttpClient = null;
	
	private UserInfo m_oUserInfo = null;
	private String[] m_saFollowIds = null;
	private String[] m_saJoinIds = null;
	
	private boolean m_bCanScanAction = false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        createHandler();
        findView();
        initParams();
        
        //从服务器处得到所有活动的信息
        //从服务器处得到活动筛选用的学校名称
     
        m_oHttpClient = new DefaultHttpClient();
        HttpThread ht = new HttpThread( m_oHttpClient, "http://124.205.252.248:3000/webservice/query", m_oHandler );
        ht.setModeInfo(1, "");//*/
        
        /*
        UserInfo ui = new UserInfo();
		ui.m_sUserName = "sherry";
		ui.m_sEmail = "sherrynew2006@sina.com";
		ui.m_sPassWord = MD5Utils.toMd5("sherry841115");
		ui.m_sCompany = "creative";
		ui.m_sMobile = "13810256590";
		ui.m_sWorkState = "3";
		ui.m_sSchool = "清华大学";
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(3, "");//*/
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://124.205.252.248:3000/webservice/query", m_oHandler );
        ht.setModeInfo(4, "");
        ht.setUserInfo(ui);//*/
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(5, "");
        ht.setUserInfo(ui);//*/
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(6, "");
        ht.setUserInfo(ui);//*/
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(7, "");
        ht.setUserInfo(ui);//*/
        
        /*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://124.205.252.248:3000/webservice/query", m_oHandler );
        ht.setModeInfo(8, "");
        ht.setUserInfo(ui);//*/
        
		/*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(9, "");
        ht.setUserInfo(ui);
        
        /*
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(10, "");
        ht.setUserInfo(ui);
        
        HttpClient hc = new DefaultHttpClient();
        HttpThread ht = new HttpThread( hc, "http://117.79.88.173:3000/webservice/query/", m_oHandler );
        ht.setModeInfo(11, "");
        ht.setUserInfo(ui);//*/
        
        m_oPDialog = ProgressDialog.show( this, "请稍等", "连接服务器中..." );
        ht.start();
        
    	actionData ad = (actionData)getApplication();
    	ad.setHandler(m_oHandler);
        ad.setHttpClient(m_oHttpClient);
    }
    
    private void findView()
    {
    	m_oListView = (ListView)findViewById(R.id.listview);
    	m_oSearchEdit = (EditText)findViewById(R.id.searchedit);
    	
    	m_oSchoolSpinner = (Spinner)findViewById(R.id.schoolspinner);
    	m_oActionSpinner = (Spinner)findViewById(R.id.actionspinner);
    	m_oSearchText = (TextView)findViewById(R.id.searchtext);
    	m_oLoginText = (TextView)findViewById(R.id.logintext);
    	m_oRegistText = (TextView)findViewById(R.id.registtext);
    	m_oNameText = (TextView)findViewById(R.id.nametext);
    }

    private void initParams()
    {
    	DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	    	    
	    m_iWidth = dm.widthPixels;
	    if( m_oSearchEdit != null )
	    {
	    	LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oSearchEdit.getLayoutParams();
	    	lp.width = m_iWidth - 250;
	    	m_oSearchEdit.setLayoutParams(lp);
	    }
	    
	    if( m_oSearchText != null )
	    {
	    	m_oSearchText.setOnClickListener(new TextView.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//模糊搜索从服务器处得到活动信息
				}
			});
	    	
	    }
	    
	    if( m_oLoginText != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oLoginText.getLayoutParams();
	    	lp.leftMargin = m_iWidth - 130;
	    	m_oLoginText.setLayoutParams(lp);
	    	
	    	m_oLoginText.setOnClickListener(new TextView.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//启动登录页面
					Intent intent = new Intent();
					intent.setClass( mbaAction.this, logIn.class );
					startActivityForResult(intent, 1);
				}
			});
	    	
	    }
	    
	    if( m_oRegistText != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oRegistText.getLayoutParams();
	    	lp.leftMargin = m_iWidth - 60;
	    	m_oRegistText.setLayoutParams(lp);
	    	
	    	m_oRegistText.setOnClickListener(new TextView.OnClickListener() 
	    	{
				@Override
				public void onClick(View v) 
				{
					//启动注册页面１
					Intent intent = new Intent();
					intent.setClass( mbaAction.this, regist1.class );
					startActivity(intent);
				}
			});
	    }
	    
	    if( m_oSchoolSpinner != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oSchoolSpinner.getLayoutParams();
	    	lp.width = (m_iWidth - 140)/2;
	    	m_oSchoolSpinner.setLayoutParams(lp);
	    	
	    	m_oSchoolSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) 
				{
					//根据学校名称筛选活动更改列表信息
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) 
				{
					
					
				}

	    	});

	    }
	    
	    if( m_oActionSpinner != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oActionSpinner.getLayoutParams();
	    	lp.leftMargin = (m_iWidth - 140)/2 + 10;
	    	lp.width = (m_iWidth - 140)/2;
	    	m_oActionSpinner.setLayoutParams(lp);
	    	
	    	m_oActionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) 
				{
					//根据活动名称启动详细活动页面
					switch( arg2 )
					{
					case 0:
						if( m_oaActionArray != null && m_oaActionArray.length > 0 )
						{
							TextImageAdapter adpter = new TextImageAdapter( mbaAction.this, mbaAction.this, m_iWidth, 160 );
							adpter.setActionInfo(m_oaActionArray);
							m_oListView.removeAllViewsInLayout();
							m_oListView.setAdapter(adpter);
						}
						else
						{
							Toast toast = Toast.makeText(mbaAction.this, "无活动信息", Toast.LENGTH_LONG);
			        		toast.setDuration(1000);
			        		toast.setGravity(Gravity.CENTER, 0, 0);
			        		toast.show();
						}
						break;
					case 1:
						if( m_saJoinIds != null && m_saJoinIds.length > 0 )
						{
							TextImageAdapter adpter = new TextImageAdapter( mbaAction.this, mbaAction.this, m_iWidth, 160 );
							ActionInfo[] ais = getActionFromId( m_saJoinIds );
							if( ais != null )
							{
								adpter.setActionInfo(ais);
								m_oListView.removeAllViewsInLayout();
								m_oListView.setAdapter(adpter);
							}
						}
						else
						{
							Toast toast = Toast.makeText(mbaAction.this, "无已报名的活动", Toast.LENGTH_LONG);
			        		toast.setDuration(1000);
			        		toast.setGravity(Gravity.CENTER, 0, 0);
			        		toast.show();
						}
						break;
					case 2:
						if( m_saFollowIds != null && m_saFollowIds.length > 0 )
						{
							TextImageAdapter adpter = new TextImageAdapter( mbaAction.this, mbaAction.this, m_iWidth, 160 );
							ActionInfo[] ais = getActionFromId( m_saFollowIds );
							if( ais != null )
							{
								adpter.setActionInfo(ais);
								m_oListView.removeAllViewsInLayout();
								m_oListView.setAdapter(adpter);
							}
						}
						else
						{
							Toast toast = Toast.makeText(mbaAction.this, "无已关注的活动", Toast.LENGTH_LONG);
			        		toast.setDuration(1000);
			        		toast.setGravity(Gravity.CENTER, 0, 0);
			        		toast.show();
						}
						break;
					default:
						break;
					
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) 
				{
					
					
				}

	    	});
	    }
	    
	    if( m_oNameText != null )
	    {
	    	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)m_oNameText.getLayoutParams();
	    	lp.leftMargin = m_iWidth - 130;
	    	m_oNameText.setLayoutParams(lp);
	    }
    	
    }
    
    private boolean createHandler()
	{
		if( m_oHandler != null )
		{
			return true;
		}
		m_oHandler = new Handler()
		{
            public void handleMessage(Message msg) 
            {
            	super.handleMessage(msg);
            	HandleMsg(msg);
            }
       };
       return( m_oHandler != null );
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
        case MessageCode.MSG_GET_ACTION_INFO:
        	if( m_oListView != null )
    	    {
    	    	TextImageAdapter adpter = new TextImageAdapter( this, this, m_iWidth, 160 );
    	    	adpter.setActionInfo((ActionInfo[])msg.obj);
            	m_oListView.setAdapter(adpter);
            	
            	m_oaActionArray = (ActionInfo[])msg.obj;
    	    }
        	break;
        case MessageCode.MSG_GET_REGIST_INFO:
        	m_oUserInfo = (UserInfo)msg.obj;
        	if( m_oUserInfo != null && !TextUtils.isEmpty(m_oUserInfo.m_sUserName) )
        	{
	        	if( m_oLoginText != null )
					m_oLoginText.setVisibility(View.INVISIBLE);
				if( m_oRegistText != null )
					m_oRegistText.setVisibility(View.INVISIBLE);
				
				if( m_oNameText != null )
				{
					m_oNameText.setVisibility(View.VISIBLE);
					String name = "你好，"+ m_oUserInfo.m_sUserName;
					m_oNameText.setText(name);
				}
				if( m_oActionSpinner != null )
				{
					String[] strs = { "全部活动", "已报名活动", "已关注活动" };
					mySpinnerAdapter sa = new mySpinnerAdapter(this, strs);
					
					m_oActionSpinner.setAdapter(sa);
					m_oActionSpinner.setVisibility(View.VISIBLE);
					
				}
				
				m_bCanScanAction = true;
        	}
			break;
        case MessageCode.MSG_GET_SCHOOL_INFO:
        	if( m_oSchoolSpinner != null )
        	{
        		SchoolInfo[] sia = (SchoolInfo[])msg.obj;
        		if( sia != null )
        		{
        			int len = sia.length;
        			String[] schoolNames = new String[len];
        			for( int i = 0; i < len; i++ )
        			{
        				if( sia[i] == null )
        					continue;
        				schoolNames[i] = sia[i].m_sSchoolName;
        			}
        			
        			mySpinnerAdapter sa = new mySpinnerAdapter( this, schoolNames );
        			m_oSchoolSpinner.setAdapter(sa);
        		}
        	}
        	break;
        default:
        	break;
        }
	}
	
	private ActionInfo[] getActionFromId( String[] actionId )
	{
		if( m_oaActionArray == null || m_oaActionArray.length <= 0 
				|| actionId == null || actionId.length <= 0 )
			return null;
		
		int len = m_oaActionArray.length;
		int len1 = actionId.length;
		ActionInfo[] ret = new ActionInfo[len1];
		
		int k = 0;
		for( int i = 0; i < len1; i++ )
		{
			String str = actionId[i];
			if( TextUtils.isEmpty(str) )
				continue;
			for( int j = 0; j < len; j++ )
			{
				ActionInfo ai = m_oaActionArray[j];
				if( ai == null || TextUtils.isEmpty(ai.m_sEventId)
						|| TextUtils.isEmpty(ai.m_sTitle) )
					continue;
				if( str.equals( ai.m_sEventId ) )
				{
					ret[k] = ai;
					k++;
				}
			}
		}
		
		return ret;
	}
	
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data ) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == 1 )//登录返回
		{
			if( data == null )
				return;
			
			Bundle bundle = data.getExtras();
			if( bundle == null )
				return;
			
			m_oUserInfo = new UserInfo();
			m_oUserInfo.m_sUserName = bundle.getString("name");
			m_oUserInfo.m_sMobile = bundle.getString("mobile");
			m_oUserInfo.m_sEmail = bundle.getString("email");
			m_oUserInfo.m_sPassWord = MD5Utils.toMd5(bundle.getString("password"));
						
			if( m_oLoginText != null )
				m_oLoginText.setVisibility(View.INVISIBLE);
			if( m_oRegistText != null )
				m_oRegistText.setVisibility(View.INVISIBLE);
			
			if( m_oNameText != null && !TextUtils.isEmpty(m_oUserInfo.m_sUserName) )
			{
				m_oNameText.setVisibility(View.VISIBLE);
				String name = "你好，"+ m_oUserInfo.m_sUserName;
				m_oNameText.setText(name);
			}
			
			m_saFollowIds = bundle.getStringArray("followids");
			m_saJoinIds = bundle.getStringArray("joinids");
			
			String[] strs = { "全部活动", "已报名活动", "已关注活动" };
			
			if( m_saJoinIds != null )
			{
				if( m_oActionSpinner != null )
				{
					mySpinnerAdapter sa = new mySpinnerAdapter(this, strs);
					
					m_oActionSpinner.setAdapter(sa);
					m_oActionSpinner.setSelection(1);
					m_oActionSpinner.setVisibility(View.VISIBLE);
				}
				
				TextImageAdapter adpter = new TextImageAdapter( this, this, m_iWidth, 160 );
				ActionInfo[] ais = getActionFromId( m_saJoinIds );
				if( ais != null )
				{
					adpter.setActionInfo(ais);
					m_oListView.removeAllViewsInLayout();
					m_oListView.setAdapter(adpter);
				}
			}
			else if( m_saFollowIds != null )
			{
				if( m_oActionSpinner != null )
				{
					mySpinnerAdapter sa = new mySpinnerAdapter(this, strs);
					
					m_oActionSpinner.setAdapter(sa);
					m_oActionSpinner.setSelection(2);
					m_oActionSpinner.setVisibility(View.VISIBLE);
				}
				
				TextImageAdapter adpter = new TextImageAdapter( this, this, m_iWidth, 160 );
				ActionInfo[] ais = getActionFromId( m_saFollowIds );
				if( ais != null )
				{
					adpter.setActionInfo(ais);
					m_oListView.removeAllViewsInLayout();
					m_oListView.setAdapter(adpter);
				}
			}
			else
			{
				if( m_oActionSpinner != null )
				{
					
					mySpinnerAdapter sa = new mySpinnerAdapter(this, strs);
					
					m_oActionSpinner.setAdapter(sa);
					m_oActionSpinner.setSelection(0);
					m_oActionSpinner.setVisibility(View.VISIBLE);
				}
			}
				
			m_bCanScanAction = true;
		}
		
	}
	
	private boolean isJoinAction( String actionId )
	{
		if( m_saJoinIds == null || TextUtils.isEmpty(actionId) )
			return false;
		
		int len = m_saJoinIds.length;
		for( int i = 0; i < len; i++ )
		{
			String str = m_saJoinIds[i];
			if( !TextUtils.isEmpty(str) && str.equals(actionId) )
				return true;
		}
		return false;
		
	}
	
	private boolean isFollowAction( String actionId )
	{
		if( m_saFollowIds == null || TextUtils.isEmpty(actionId) )
			return false;
		
		int len = m_saFollowIds.length;
		for( int i = 0; i < len; i++ )
		{
			String str = m_saFollowIds[i];
			if( !TextUtils.isEmpty(str) && str.equals(actionId) )
				return true;
		}
		
		return false;
		
	}
	
	@Override
	public void onItemClicked( String actionId ) 
	{
		//点击了某个活动启动详细活动页面
		//如果用户未参加活动，启动未参加活动详细页面
		//如果用户已参加活动，启动已参加活动详细页面
		//如果用户是此活动组织者，启动组织者活动详细页面
		
		if( !m_bCanScanAction )
		{
			Toast toast = Toast.makeText(this, "登录或注册后才可浏览详细信息", Toast.LENGTH_LONG);
    		toast.setDuration(1000);
    		toast.setGravity(Gravity.CENTER, 0, 0);
    		toast.show();
			return;
		}
		
		if( TextUtils.isEmpty(actionId) )
			return;
		
		if( m_oaActionArray == null || m_oUserInfo == null )
			return;
		
		int len = m_oaActionArray.length;
		ActionInfo ai = null;
		for( int i = 0; i < len; i++ )
		{
			ai = m_oaActionArray[i];
			if( ai.m_sEventId.equals(actionId) )
				break;
		}
				
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		
		bundle.putString("eventid", ai.m_sEventId);
		bundle.putString("title", ai.m_sTitle);
		bundle.putString("schoolid", ai.m_sSchoolId);
		bundle.putString("schoolname", ai.m_sSchoolName);
		bundle.putString("orgid", ai.m_sOrgId);
		bundle.putString("orgname", ai.m_sOrgName);
		bundle.putString("sponsorname", ai.m_sSponsorName);
		bundle.putString("undertakername", ai.m_sUndertakerName);
		bundle.putString("cooperatername", ai.m_sCooperaterName);
		bundle.putString("address",ai.m_sAddress);
		bundle.putString("begintime",ai.m_sBeginTime);
		bundle.putString("endtime", ai.m_sEndTime);
		bundle.putString("speakername", ai.m_sSpeakerName);
		bundle.putString("speakerinfo", ai.m_sSpeakerInfo);
		bundle.putString("seat", ai.m_sSeat);
		bundle.putString("introduction", ai.m_sIntroduction);
		bundle.putString("status", ai.m_sStatus);
		bundle.putString("authentication", ai.m_sAuthentication);
		bundle.putString("other", ai.m_sOther);
		
		if( isJoinAction( actionId ) && isFollowAction( actionId ) )
			bundle.putInt("mode", 1);//此活动已报名已关注
		else if( isJoinAction( actionId ) )
			bundle.putInt("mode", 2);//此活动已报名未关注
		else if( isFollowAction( actionId ) )
			bundle.putInt("mode", 3);//此活动未报名已关注
		else
			bundle.putInt("mode", 4);//此活动未报名未关注
		
		
		bundle.putString("email", m_oUserInfo.m_sEmail);
		bundle.putString("name", m_oUserInfo.m_sUserName);
		bundle.putString("moblie", m_oUserInfo.m_sMobile);
		bundle.putString("password", m_oUserInfo.m_sPassWord);
		
		intent.putExtras(bundle);
		intent.setClass( mbaAction.this, actionInfo.class );
		startActivity(intent);
	}
    
}