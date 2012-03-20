package com.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListLayout extends LinearLayout 
{
	private Button m_oActionButton = null;
	private TextView m_oActionContent = null;
	private String m_sActionId = null;
	
	private int m_iWidth = 0;
	private ListLayoutInterface m_oInterface = null;
	
	public ListLayout(Context context, String id, int Width ) 
	{
		super(context);
		init( context, id, Width );
	}
	
	public ListLayout(Context context, AttributeSet attrs, String id, int Width ) 
	{
		super(context, attrs);
		init( context, id, Width );
	}
	
	private void init( Context context, String id, int w )
	{
		setOrientation(LinearLayout.HORIZONTAL);
		m_sActionId = id;
		
		m_oActionButton = new Button( context );
		m_oActionContent = new TextView( context );
		m_iWidth = w;
		
		if( m_oActionButton != null )
			m_oActionButton.setOnClickListener(buttonClicked);
	}
	
	private Button.OnClickListener buttonClicked = new Button.OnClickListener()
	{
		public void onClick( View v )
		{
			if( m_oInterface != null )
				m_oInterface.onItemClicked(m_sActionId);
		}
	};
	
	
	public void setParams( String id, int width )
	{
		m_sActionId = id;
		m_iWidth = width;
	}
	
	public void setInterface( ListLayoutInterface li )
	{
		m_oInterface = li;
	}
		
	public boolean setActionContent( int color, int textSize, String content, int height, boolean init )
	{
		if( TextUtils.isEmpty(content) || m_oActionContent == null || m_iWidth <= 0 || m_oActionButton == null 
	           || height <= 0 )
			return false;
		
		if( !init )
		{
			if( m_oActionContent != null )
			{
				m_oActionContent.setTextSize(textSize);
				m_oActionContent.setTextColor(color);
				m_oActionContent.setText(content);
			}
			if( m_oActionButton != null )
				m_oActionButton.setTextSize(textSize);
			return true;
		}
		
		int width = m_iWidth*2/3;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams( width, height );
		
		addView( m_oActionContent, lp );
		
		m_oActionContent.setTextSize(textSize);
		m_oActionContent.setTextColor(color);
		m_oActionContent.setText(content);
		m_oActionContent.setGravity(Gravity.CENTER);
		
		width = m_iWidth/3;
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams( width, height-10 );
		lp1.gravity = Gravity.CENTER_VERTICAL;
		
		addView( m_oActionButton, lp1 );
		
		m_oActionButton.setTextSize(textSize);
		m_oActionButton.setText("²é¿´ÏêÇé");
		m_oActionButton.setGravity(Gravity.CENTER);
		
		return true;
	}
	
	public void clean()
	{
		m_oActionButton = null;
		m_oActionContent = null;
		m_sActionId = null;
		
		m_oInterface = null;
	}

}
