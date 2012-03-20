package com.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class HttpThread extends Thread 
{
	private Handler m_oHandler = null;
	private HttpClient m_oHttpClient = null;
	private String m_sUrl = null;
	private int m_iMode = 0;
	
	private final int m_iConnectTime = 60000;
	
	private String m_sActionId = null;
	
	private UserInfo m_oUserInfo = null;
	
	public HttpThread( HttpClient client, String url, Handler handle )
	{
		m_oHttpClient = client;
		if( m_oHttpClient != null )
		{
			m_oHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, m_iConnectTime);  
			m_oHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, m_iConnectTime);
		}
		m_sUrl = url;
		m_oHandler = handle;
	}
	
	public void setUserInfo( UserInfo ui )
	{
		m_oUserInfo = ui;
	}
	
	@Override
	public void run() 
	{
		String data = "";
		String retStr = "";
		switch( m_iMode )
		{
		case 1://decribe events
			data = getActionInfo( m_sUrl );
			if( decodeActionInfoData( data ) )
			{
				data = describeSchools( m_sUrl );
				decodeSchoolData( data );
				retStr = "获取数据成功";
			}
			break;
		case 2://regist user
			data = registUser( m_sUrl );
			//decodeRegistUser( data );
			retStr = "注册成功";
			break;
		case 3:
			break;
		case 4://describe user
			data = describeUsers( m_sUrl );
			decodeUserData( data, 1 );
			retStr = "登录成功";
			break;
		case 5://describe userbymobile
			data = describeUserByMobile( m_sUrl );
			decodeUserData( data, 2 );
			retStr = "获取数据成功";
			break;
		case 6:
			data = describeUser( m_sUrl );
			decodeUserData( data, 2 );
			break;
		case 7:
			data = joinEvent( m_sUrl );
			break;
		case 8:
			data = exitEvent( m_sUrl );
			break;
		case 9:
			data = followEvent( m_sUrl );
			break;
		case 10:
			data = unFollowEvent( m_sUrl );
			break;
		case 11:
			data =  describeJoinsEvent( m_sUrl );
			break;
		default:
			break;
		}
		
		if( !TextUtils.isEmpty(data) && m_oHandler != null )
		{
			Message msg = new Message();
			msg.what = MessageCode.MSG_HTTP_STATE;
			msg.obj = retStr;
			msg.arg1 = 0;
			
			m_oHandler.sendMessage(msg);
		}
	}
	
	private ActionInfo makeActionInfo( JSONObject jObject )
	{
		ActionInfo ret = new ActionInfo();
				
		try
		{
			if( !jObject.isNull("id") )
				ret.m_sEventId = jObject.getString("id");
			
			if( !jObject.isNull("title") )
				ret.m_sTitle = jObject.getString("title");
			
			if( !jObject.isNull("school_id") )
				ret.m_sSchoolId = jObject.getString("school_id");
			
			if( !jObject.isNull("school_name") )
				ret.m_sSchoolName = jObject.getString("school_name");
			
			if( !jObject.isNull("org_id") )
				ret.m_sOrgId = jObject.getString("org_id");
			
			if( !jObject.isNull("org_name") )
				ret.m_sOrgName = jObject.getString("org_name");
			
			if( !jObject.isNull("sponsor") )
				ret.m_sSponsorName = jObject.getString("sponsor");
			
			if( !jObject.isNull("undertaker") )
				ret.m_sUndertakerName = jObject.getString("undertaker");
			
			if( !jObject.isNull("cooperater") )
				ret.m_sCooperaterName = jObject.getString("cooperater");
			
			if( !jObject.isNull("place") )
				ret.m_sAddress = jObject.getString("place");
			
			if( !jObject.isNull("beginTime") )
				ret.m_sBeginTime = jObject.getString("beginTime");
			
			if( !jObject.isNull("endTime") )
				ret.m_sEndTime = jObject.getString("endTime");
			
			if( !jObject.isNull("speaker") )
				ret.m_sSpeakerName = jObject.getString("speaker");
			
			if( !jObject.isNull("speakerInfo") )
				ret.m_sSpeakerInfo = jObject.getString("speakerInfo");
			
			if( !jObject.isNull("seat") )
				ret.m_sSeat = jObject.getString("seat");
			
			if( !jObject.isNull("brief") )
				ret.m_sIntroduction = jObject.getString("brief");
			
			if( !jObject.isNull("status") )
				ret.m_sStatus = jObject.getString("status");
			
			if( !jObject.isNull("toward") )
				ret.m_sAuthentication = jObject.getString("toward");
			
			if( !jObject.isNull("others") )
				ret.m_sOther = jObject.getString("others");
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private boolean decodeActionInfoData( String data )
	{
		if( TextUtils.isEmpty(data) )
			return false;
		try 
		{
			JSONArray jsonArray = new JSONArray(data);
			if( jsonArray == null )
				return false;
			int len = jsonArray.length();
			ActionInfo[] actionArray = new ActionInfo[len]; 
			for( int i = 0; i < len; i++ )
			{
				JSONObject jObject = jsonArray.getJSONObject(i);
				if( jObject == null )
					continue;
				actionArray[i] = makeActionInfo( jObject );
			}
			
			if( m_oHandler != null )
			{
				Message msg = new Message();
				msg.what = MessageCode.MSG_GET_ACTION_INFO;
				msg.obj = actionArray;
				
				m_oHandler.sendMessage(msg);
			}
				
		} 
		catch(JSONException e) 
		{
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	private boolean decodeRegistUser( String data )
	{
		if( TextUtils.isEmpty(data) )
			return false;
		
		try 
		{
			JSONObject jObject = new JSONObject(data);
			if( jObject == null )
				return false;
			
			String str = null;
			if( !jObject.isNull("updated_at") )
				str = jObject.getString("updated_at");
			if( !jObject.isNull("password") )
				str = jObject.getString("password");
			if( !jObject.isNull("mobile") )
				str = jObject.getString("mobile");
			if( !jObject.isNull("name") )
				str = jObject.getString("name");
			if( !jObject.isNull("email") )
				str = jObject.getString("email");
			if( !jObject.isNull("created_at") )
				str = jObject.getString("created_at");
			if( !jObject.isNull("id") )
				str = jObject.getString("id");
			
		} 
		catch(JSONException e) 
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	private boolean decodeSchoolData( String data )
	{
		if( TextUtils.isEmpty(data) )
			return false;
		
		try 
		{
			JSONArray jsonArray = new JSONArray(data);
			if( jsonArray == null )
				return false;
			int len = jsonArray.length();
			SchoolInfo[] schoolArray = new SchoolInfo[len];
			String str = null;
			for( int i = 0; i < len; i++ )
			{
				JSONObject jObject = jsonArray.getJSONObject(i);
				if( jObject == null )
					continue;
				SchoolInfo si = new SchoolInfo();
				
				if( !jObject.isNull("schoolId") )
				{
					str = jObject.getString("schoolId");
					si.m_sSchoolId = str;
				}
				if( !jObject.isNull("schoolName") )
				{
					str = jObject.getString("schoolName");
					si.m_sSchoolName = str;
				}
				if( !jObject.isNull("create_at") )
				{
					str = jObject.getString("create_at");
					si.m_sCreateTime = str;
				}
				if( !jObject.isNull("updated_at") )
				{
					str = jObject.getString("updated_at");
					si.m_sUpdateTime = str;
				}
				
				schoolArray[i] = si;
			}
			
			if( m_oHandler != null )
			{
				Message msg = new Message();
				msg.what = MessageCode.MSG_GET_SCHOOL_INFO;
				msg.obj = schoolArray;
				
				m_oHandler.sendMessage(msg);
			}
			
		} 
		catch(JSONException e) 
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	private boolean decodeUserData( String data, int mode )
	{
		if( TextUtils.isEmpty(data) )
			return false;
		
		try 
		{
			if( mode == 1 )
			{
				JSONArray jarray = new JSONArray(data);
				
				if( jarray == null )
					return false;
				
				int len = jarray.length();
				UserInfo ui = new UserInfo();
				for( int i = 0; i < len; i++ )
				{
					JSONObject jObject = jarray.getJSONObject(i);
					if( jObject == null )
						continue;
					if( !jObject.isNull("email") )
					{
						ui.m_sEmail = jObject.getString("email");
					}
					if( !jObject.isNull("name") )
					{
						ui.m_sUserName = jObject.getString("name");
					}
					if( !jObject.isNull("mobile") )
					{
						ui.m_sMobile = jObject.getString("mobile");
					}
						
				}
				
				if( m_oHandler != null )
				{
					Message msg = new Message();
					msg.what = MessageCode.MSG_GET_USER_INFO;
					msg.obj = ui;
					
					m_oHandler.sendMessage(msg);
				}
				
			}
			else if( mode == 2 )
			{
				JSONObject jObject = new JSONObject(data);
				if( jObject == null )
					return false;
	
				UserInfo ui = new UserInfo();
				String str = null;
				if( !jObject.isNull("email") )
				{
					str = jObject.getString("email");
					ui.m_sEmail = str;
				}
				if( !jObject.isNull("name") )
				{
					str = jObject.getString("name");
					ui.m_sUserName = str;
				}
				if( !jObject.isNull("mobile") )
				{
					str = jObject.getString("mobile");
					ui.m_sMobile = str;
				}
				
				if( m_oHandler != null )
				{
					Message msg = new Message();
					msg.what = MessageCode.MSG_GET_USER_INFO;
					msg.obj = ui;
					
					m_oHandler.sendMessage(msg);
				}
				/*
				if( !jObject.isNull("school") )
				{
					str = jObject.getString("school");
					ui.m_sCompany = str;
				}
				if( !jObject.isNull("company") )
				{
					str = jObject.getString("company");
					ui.
				}
				if( !jObject.isNull("isRegister") )
					str = jObject.getString("isRegister");*/
			}
			
		} 
		catch(JSONException e) 
		{
			e.printStackTrace();
		}
		
		return true;
	}
	
	public void setModeInfo( int mode, String id )
	{
		m_iMode = mode;
		m_sActionId = id;
	}
	
	private boolean sentErrorCode( String retSrc )
	{
		if( TextUtils.isEmpty(retSrc) )
			return false;
		JSONArray jArray;
		String str = "未知错误";
		try 
		{
			jArray = new JSONArray(retSrc);
			if( jArray == null || jArray.length() <= 0 )
				return false;
		
			JSONObject jObject = jArray.getJSONObject(0);
				    			
			if( !jObject.isNull("error") )
				str = jObject.getString("error");
			
		} 
		catch(JSONException e) 
		{
			e.printStackTrace();
		}
		
		if( m_oHandler != null )
		{
			Message msg = new Message();
			msg.what = MessageCode.MSG_HTTP_STATE;
			msg.obj = str;
			msg.arg1 = -1;
			
			m_oHandler.sendMessage(msg);
		}
		
		
		return true;
	}
	
	private String getActionInfo( String url ) 
	{
        HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_events");
        	if( m_oUserInfo == null )
        	{
        		param.put("email", "");
        		param.put("password", "");
        	}
        	else
        	{
        		param.put("email", m_oUserInfo.m_sEmail);
        		param.put("password", m_oUserInfo.m_sPassWord);
        	}
        	param.put("filter", "");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	int ret = httpResponse.getStatusLine().getStatusCode();
	        	if( ret == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			msg.arg1 = -2;
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        return null;
    }
	
	private String registUser( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "register_user");
        	param.put("name", m_oUserInfo.m_sUserName);
        	param.put("email", m_oUserInfo.m_sEmail);
        	param.put("password", m_oUserInfo.m_sPassWord);
        	param.put("mobile", m_oUserInfo.m_sMobile);
        	/*
        	param.put("workstate", m_oUserInfo.m_sWorkState);
        	param.put("company", m_oUserInfo.m_sCompany);
        	param.put("school", m_oUserInfo.m_sSchool);*/
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			msg.arg1 = -2;
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String describeSchools( String url )
	{		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_schools");
        	if( m_oUserInfo == null )
        	{
        		param.put("email", "");
        		param.put("password", "");
        	}
        	else
        	{
        		param.put("email", m_oUserInfo.m_sEmail);
        		param.put("password", m_oUserInfo.m_sPassWord);
        	}
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	int ret = httpResponse.getStatusLine().getStatusCode();
	        	if( ret == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{	
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        		
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String describeUsers( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_users");
        	param.put("email", "stevenchen945@qq.com");
        	param.put("password", "b4ed99c0de60df97c9ef2fb8b700778d");
        	//param.put("email", m_oUserInfo.m_sEmail);
        	//param.put("password", m_oUserInfo.m_sPassWord);
        	param.put("getEvents", "true");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			msg.arg1 = -2;
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String describeUserByMobile( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_user_by_mobile");
        	param.put("mobile", "13912345678");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	int ret = httpResponse.getStatusLine().getStatusCode();
	        	if( ret == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			msg.arg1 = -2;
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String describeUser( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_users");
        	param.put("email", m_oUserInfo.m_sEmail);
        	param.put("password", m_oUserInfo.m_sPassWord);
        	param.put("getEvents", "true");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String joinEvent( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "join_event");
        	param.put("eventId", "1");
        	param.put("email", m_oUserInfo.m_sEmail);
        	param.put("password", m_oUserInfo.m_sPassWord);
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	int ret = httpResponse.getStatusLine().getStatusCode();
	        	if( ret == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String exitEvent( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "exit_event");
        	param.put("eventId", "1");
        	param.put("email", "stevenchen945@qq.com");
        	param.put("password", "b4ed99c0de60df97c9ef2fb8b700778d");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String followEvent( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "follow_event");
        	param.put("eventId", m_sActionId);
        	//param.put("email", m_oUserInfo.m_sEmail);
        	//param.put("password", m_oUserInfo.m_sPassWord);
        	param.put("email", "stevenchen945@qq.com");
        	param.put("password", "b4ed99c0de60df97c9ef2fb8b700778d");
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String unFollowEvent( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "unfollow_event");
        	param.put("ecentId", m_sActionId);
        	param.put("email", "stevenchen945@qq.com");
        	param.put("password", "b4ed99c0de60df97c9ef2fb8b700778d");
        	//param.put("email", m_oUserInfo.m_sEmail);
        	//param.put("password", m_oUserInfo.m_sPassWord);
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
	private String describeJoinsEvent( String url )
	{
		if( m_oUserInfo == null )
			return null;
		
		HttpPost request = new HttpPost(url);  
        // 先封装一个 JSON 对象  
        JSONObject param = new JSONObject();  
        try 
        {
        	param.put("timeout", "40");
        	param.put("cmd", "describe_joins");
        	param.put("ecentId", m_sActionId);
        	param.put("email", m_oUserInfo.m_sEmail);
        	param.put("password", m_oUserInfo.m_sPassWord);
        	
			 // 绑定到请求 Entry  
	        StringEntity se = new StringEntity(param.toString());   
	        request.setEntity(se);  
	        // 发送请求  
	        HttpResponse httpResponse = m_oHttpClient.execute(request); 
	        
	        String retSrc = null;
	        if(httpResponse != null && httpResponse.getStatusLine() != null )
	        {
	        	if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK )
	        		// 得到应答的字符串，这也是一个 JSON 格式保存的数据  
		        	retSrc = EntityUtils.toString(httpResponse.getEntity()); 
	        	else
	        	{
	        		//错误4xx
	        		retSrc = EntityUtils.toString(httpResponse.getEntity());
	        		sentErrorCode( retSrc );
	        		
	        		return null;
	        	}
	        		
	        }
                   
	        return retSrc; 
		} 
        catch(Exception e) 
		{
        	if( m_oHandler != null )
    		{
    			Message msg = new Message();
    			msg.what = MessageCode.MSG_HTTP_STATE;
    			msg.obj = "连接服务器失败";
    			
    			m_oHandler.sendMessage(msg);
    		}
			e.printStackTrace();
		}
        
        return null;
	}
	
}
