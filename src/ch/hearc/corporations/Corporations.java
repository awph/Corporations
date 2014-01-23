package ch.hearc.corporations;

import android.app.Application;
import android.content.Context;

public class Corporations extends Application
{
	private static Context	context;

	public void onCreate()
	{
		super.onCreate();
		Corporations.context = getApplicationContext();
	}

	/**
	 * @return the app context
	 */
	public static Context getAppContext()
	{
		return Corporations.context;
	}
}
