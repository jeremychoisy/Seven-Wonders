package org.Model.tools;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MyPrintStream extends PrintStream {
	public MyPrintStream(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public MyPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
		super(out, autoFlush, encoding);
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public void println(String string) {
    	String heure,minute,seconde;
        Calendar c = new GregorianCalendar(); 
        
        if(c.get(Calendar.HOUR_OF_DAY)< 10) { heure = "0" + c.get(Calendar.HOUR_OF_DAY); } else { heure = "" + c.get(Calendar.HOUR_OF_DAY); }
        if(c.get(Calendar.MINUTE)< 10) { minute = "0" + c.get(Calendar.MINUTE); } else { minute = "" + c.get(Calendar.MINUTE); }
        if(c.get(Calendar.SECOND)< 10) { seconde = "0" + c.get(Calendar.SECOND); } else { seconde = "" + c.get(Calendar.SECOND); }

        super.println("[" + heure + ":" + minute + ":" + seconde + "] "  +  string);
    }



}
