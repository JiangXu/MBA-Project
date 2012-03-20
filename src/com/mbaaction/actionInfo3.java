package com.mbaaction;

import com.util.mySpinnerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class actionInfo3 extends Activity 
{
	private GridView m_oInfoGrid = null;
	
	private TextView m_oTitle1Text = null;
	private TextView m_oTitle2Text = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actioninfo3);
       
        findView();
        init();
    }
	
	private void findView()
    {
		m_oInfoGrid = (GridView)findViewById(R.id.infogrid);
		m_oTitle1Text = (TextView)findViewById(R.id.titletext1);
		m_oTitle2Text = (TextView)findViewById(R.id.titletext2);
    }
	
	private boolean init()
	{
		DisplayMetrics dm = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(dm);
	   
	    if( m_oTitle1Text != null )
	    {
	    	m_oTitle1Text.setText("ΥβΈφ»ξ¶― ±¨ΓϋΗιΏφ²ιΡ―");
	    }
		
	    if( m_oTitle2Text != null )
	    {
	    	m_oTitle2Text.setText("±¨ΓϋΘΛΚύ£Ί20ΘΛ");
	    }
	    
	    if( m_oInfoGrid != null )
	    {
	    	LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)m_oInfoGrid.getLayoutParams();
    		lp.height = dm.heightPixels - 45 - 200;
    		m_oInfoGrid.setLayoutParams(lp);
    		
	    	m_oInfoGrid.setNumColumns(4);
	    	m_oInfoGrid.setColumnWidth(dm.widthPixels/4);
	    	m_oInfoGrid.setHorizontalSpacing(10);
	    	
	    	String[] str = {"½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123",
	    			"½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123",
	    			"½―Πρ\n123", "½―Πρ\n123", "½―Πρ\n123"};
	    	
	    	mySpinnerAdapter sa = new mySpinnerAdapter( this, str );
	    	m_oInfoGrid.setAdapter(sa);
	    }
		return true;
	}

}
