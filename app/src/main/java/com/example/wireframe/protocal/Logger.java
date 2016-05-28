package com.example.wireframe.protocal;

import android.util.Log;

public class Logger
{
	private static final boolean LOGVV = false;

	public static void log(String tag, String msg)
	{
		if(LOGVV)
		{
			Log.i(tag, msg);
		}
	}
}
