/*=====================================================================*
| This file declares the following classes:
|    Tools.java
|
| Description of the class Tools.java :
| Contains several method that can help in the whole project.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 24 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

/**
 * @author Alexandre
 * 
 */
public class Tools
{
	/**
	 * Format a number to money string.
	 * 
	 * @param money
	 *            is the money that need to be formated
	 * @return the formated money in string
	 */
	public static String formatMoney(long money)
	{
		return "$" + String.format("%,d", money);
	}

	/**
	 * Show a alert dialog.
	 * 
	 * @param context
	 *            in with one the alert need to be showed
	 * @param title
	 *            the title of the alert
	 * @param message
	 *            the message of the alert
	 */
	public static void showInfoAlertDialog(Context context, String title, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, context.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				// nothing
			}
		});
		alertDialog.show();
	}

	/**
	 * Compute the distance in meter from two points. Use a Location method,
	 * this method is just for an easy to use way.
	 * 
	 * @param latitude1
	 *            is the latitude of the first point
	 * @param longitude1
	 *            is the longitude of the first point
	 * @param latitude2
	 *            is the latitude of the second point
	 * @param longitude2
	 *            is the longitude of the second point
	 * @return the distance in meter
	 */
	public static float distanceBetween(double latitude1, double longitude1, double latitude2, double longitude2)
	{
		float[] results = new float[3];
		Location.distanceBetween(latitude1, longitude1, latitude2, longitude2, results);
		return results[0];
	}

	/**
	 * Generate Key hash, useful for Facebook SDK
	 */
	public static void generateKeyHash()
	{
		PackageInfo info;
		try
		{
			info = Corporations.getAppContext().getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d(TAG, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	private static final String	TAG				= "Log : MainActivity";
	private static final String	PACKAGE_NAME	= "ch.hearc.corporations";
}
