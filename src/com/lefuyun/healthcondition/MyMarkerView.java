
package com.lefuyun.healthcondition;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.lefuyun.R;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent,tvDate;
    DecimalFormat df;
    int num=0;
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);
        tvDate=(TextView) findViewById(R.id.tvDate);
    }
    public void setNum(int num){
    	this.num=num;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight,String xDate) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + subString(ce.getHigh()));/*tils.formatNumber(ce.getHigh(), 0, true));*/
            tvDate.setText(xDate);
        } else {
            tvContent.setText("" + subString(e.getVal()));//Utils.formatNumber(e.getVal(), 0, true));
            tvDate.setText(xDate);
        }
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -getHeight();
    }
    
    private DecimalFormat getDf(){
    	if (null == df) {
    		String s;
	    	if (num==1) {
	    		s = "#.0";
	    	} else if (num==2) {
	    		s = "#.00";
	    	} else {
	    		s="#";
	    	}
	    	this.df = new DecimalFormat(s);
    	}
    	return df;
    }

    
    private String subString(float f){
		return this.getDf().format(f);
    }
}
