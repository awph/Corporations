/*=====================================================================*
| This file declares the following classes:
|    OnBoorBroadcastReceiver.java
|
| Description of the class OnBoorBroadcastReceiver.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author Alexandre
 * 
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver
{
	/**
	 * Method call every minutes, since the app is launch. And when the device
	 * boot. Create the service for track the user trip. (Only when not running)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (!TripService.running) context.startService(new Intent(context, TripService.class));
	}
}
