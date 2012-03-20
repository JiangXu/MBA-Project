package com.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TextImageAdapter extends BaseAdapter
{
	private Context m_oCtx = null;
	private ListLayoutInterface m_oInterface = null;
		
	private int m_iWidth = 0;
	private int m_iHeight = 0;
	
	private ActionInfo[] m_oaInfos = null;
	
	public TextImageAdapter( Context context, ListLayoutInterface li, int width, int height )
	{
		m_oCtx = context;
		m_oInterface = li;
		m_iWidth = width;
		m_iHeight = height;
	}
	
	public void setActionInfo( ActionInfo[] ai )
	{
		m_oaInfos = ai;
	}
	
	@Override
	public int getCount() 
	{
		if( m_oaInfos != null )
			return m_oaInfos.length;
		else
			return 0;
	}

	@Override
	public Object getItem(int arg0) 
	{
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{	
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) 
	{ 
		if( m_oaInfos == null )
			return null;
		//get information from server
		
		ActionInfo ai = m_oaInfos[arg0];
		if( ai == null )
			return null;
		if( arg1 == null )
		{
			ListLayout ll = new ListLayout( m_oCtx, ai.m_sEventId, m_iWidth );
			String content = ai.m_sTitle + "\n" + ai.m_sBeginTime + "жа" + ai.m_sEndTime;
			ll.setActionContent( Color.WHITE, 24, content, m_iHeight, true );
			ll.setInterface(m_oInterface);
			
			return ll;
		}
		else
		{
			((ListLayout)arg1).setParams(ai.m_sEventId, m_iWidth);
			String content = ai.m_sTitle + "\n" + ai.m_sBeginTime + "жа" + ai.m_sEndTime;
			((ListLayout)arg1).setActionContent( Color.WHITE, 24, content, m_iHeight, false );
			((ListLayout)arg1).setInterface(m_oInterface);
			return arg1;
		}
	}
	
}


