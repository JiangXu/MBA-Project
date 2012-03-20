package com.util;

import org.apache.http.client.HttpClient;

import android.app.Application;
import android.os.Handler;

public class actionData extends Application 
{
	private Handler m_oHandler = null;
	private HttpClient m_oHttpClient = null;
	
	public void setHandler( Handler h )
	{
		m_oHandler = h;
	}
	
	public Handler getHandler()
	{
		return m_oHandler;
	}
	
	public void setHttpClient( HttpClient hc )
	{
		m_oHttpClient = hc;
	}
	
	public HttpClient getHttpClient()
	{
		return m_oHttpClient;
	}

}
