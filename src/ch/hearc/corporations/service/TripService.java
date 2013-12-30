/*=====================================================================*
| This file declares the following classes:
|    TrakingService.java
|
| Description of the class TrakingService.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Alexandre
 * 
 */
public class TripService extends Service implements LocationListener
{

	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static boolean		running			= false;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private LocationManager		locationManager;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/
	private static final String	TAG				= TripService.class.getSimpleName();

	private static final long	FIVE_MINUTES	= 1000 * 60 * 5;

	/*------------------------------------------------------------------*\
	|*							Service Methods							*|
	\*------------------------------------------------------------------*/
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.e(TAG, "onCreate");
		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Start track location each five minutes
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, FIVE_MINUTES, 0, this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		running = true;
		Toast.makeText(this, "Trip service starting", Toast.LENGTH_SHORT).show();
		Log.e(TAG, "Received start id " + startId + ": " + intent);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onDestroy()
	{
		running = false;
		Log.e(TAG, "onDestroy");
		super.onDestroy();
	}

	/*------------------------------------------------------------------*\
	|*						Location Listener Methods					*|
	\*------------------------------------------------------------------*/

	@Override
	public void onLocationChanged(Location location)
	{
		Log.e(TAG, "onLocationChanged -> " + location.toString());
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		Log.e(TAG, "onProviderDisabled -> " + provider);
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		Log.e(TAG, "onProviderEnabled -> " + provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		Log.e(TAG, "onStatusChanged -> " + provider + " -status-> " + status + " -extras-> " + extras.toString());

	}

}
