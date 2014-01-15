/*=====================================================================*
| This file declares the following classes:
|    Tools.java
|
| Description of the class Tools.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 24 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Alexandre
 * 
 */
public class Tools
{
	public static String formatMoney(long money)
	{
		return "$" + String.format("%,d", money);
	}

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

	public static float distanceBetween(LatLng firstLocation, LatLng secondLocation)
	{
		float[] results = new float[3];
		if(firstLocation == null) Log.e("Tools", "1");
		if(secondLocation == null) Log.e("Tools", "2");
		Location.distanceBetween(firstLocation.latitude, firstLocation.longitude, secondLocation.latitude, secondLocation.longitude, results);
		return results[0];
	}
}
