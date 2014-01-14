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
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ch.hearc.corporations.controller.TripManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

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

	private LocationRequest		locationRequest;
	private LocationClient		locationClient;

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

		locationRequest = LocationRequest.create();
		locationRequest.setInterval(FIVE_MINUTES);
		locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		locationRequest.setFastestInterval(FIVE_MINUTES);

		ConnectionCallbacks connectionCallbacks = new GooglePlayServicesClient.ConnectionCallbacks() {
			@Override
			public void onDisconnected()
			{
			}

			@Override
			public void onConnected(Bundle connectionHint)
			{
				locationClient.requestLocationUpdates(locationRequest, TripService.this);
			}
		};

		locationClient = new LocationClient(this, connectionCallbacks, new OnConnectionFailedListener() {

			@Override
			public void onConnectionFailed(ConnectionResult arg0)
			{
				// Nothing to do...
			}
		});
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		running = true;
		Toast.makeText(this, "Trip service starting", Toast.LENGTH_SHORT).show();
		locationClient.connect();
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
		TripManager.addLocation(this, location.getLatitude(), location.getLongitude());
	}

}
