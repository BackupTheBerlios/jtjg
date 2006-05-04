// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerLogHandler.java

package service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.*;
import javax.swing.JTextArea;

public class SerLogAppender extends FileHandler
{

    public SerLogAppender(String fileName, int limit, int count)
        throws IOException
    {
        super("jtjgLog.log", limit, count);
        dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
        setFormatter(new SimpleFormatter());
    }

    public void publish(LogRecord record)
    {
        if(getTextAreas().size() > 0)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append(dateFormat.format(new Date(record.getMillis())) + " ");
            buffer.append(record.getLevel() + " ");
            buffer.append(record.getMessage() + "\n");
            for(int i = 0; i < getTextAreas().size(); i++)
            {
                JTextArea outputArea = (JTextArea)getTextAreas().get(i);
                outputArea.append(buffer.toString());
                outputArea.setCaretPosition(outputArea.getText().length());
            }

        }
        super.publish(record);
    }

    public static ArrayList getTextAreas()
    {
        return textAreas;
    }

    static ArrayList textAreas = new ArrayList();
    SimpleDateFormat dateFormat;

}
