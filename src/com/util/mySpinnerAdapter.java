package com.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class mySpinnerAdapter extends BaseAdapter
{
	private String[] m_saShowText = null;
	private Context m_oCtx = null;
	
	private int m_iTextSize = 16;
	
	public mySpinnerAdapter( Context context, String[] str )
	{
		m_oCtx = context;
		m_saShowText = str;
	}
	
	public void setTextSize( int size )
	{
		m_iTextSize = size;
	}
	
	@Override
	public int getCount() 
	{
		if( m_saShowText != null )
			return m_saShowText.length;
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
		if( arg1 == null && m_saShowText != null )
		{
			TextView tv = new TextView(m_oCtx);
			tv.setTextSize(m_iTextSize);
			if( m_saShowText[arg0] != null )
				tv.setText(m_saShowText[arg0]);
			
			return tv;
		}
		else if( arg1 != null && m_saShowText != null )
		{
			((TextView)arg1).setTextSize(m_iTextSize);
			if( m_saShowText[arg0] != null )
				((TextView)arg1).setText(m_saShowText[arg0]);
			
			return arg1;
		}
		
		return null;
		
	}
	
}


