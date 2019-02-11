package org.Model;

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
        Calendar c = new GregorianCalendar();
        super.println("[" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND) + "] " + string);
    }



}
