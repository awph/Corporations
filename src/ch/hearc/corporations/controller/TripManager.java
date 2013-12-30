/*=====================================================================*
| This file declares the following classes:
|    TripManager.java
|
| Description of the class TripManager.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import ch.hearc.corporations.model.Trip;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Alexandre
 * 
 */
public class TripManager
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final int	RADIUS_HOME_AREA_IN_METER	= 500;
	private static final String	TRIPS_FILENAME				= "trips.bin";
	private static final String	TAG							= TripManager.class.getSimpleName();

	public static void addLocation(Context context, double latitude, double longitude)
	{
		Log.e(TAG, "addLocation");
		List<Trip> trips = loadTrips(context);
		Trip trip;

		if (trips.size() <= 0)
		{
			trip = new Trip();
			trips.add(trip);
		}
		else
			trip = trips.get(trips.size() - 1);

		if (trip.isFinished())
		{
			trip = new Trip();
			trips.add(trip);
		}
		if (isInHomeArea(latitude, longitude))
		{
			LatLng home = AccountController.getInstance().getHome();
			trip.endTrip(home.latitude, home.longitude);
		}
		else
			trip.addLocation(latitude, longitude);

		saveTrips(context, trips);
	}

	@SuppressWarnings("unchecked")
	public static List<Trip> loadTrips(Context context)
	{
		Log.e(TAG, "loadTrips");
		List<Trip> trips = null;
		try
		{
			FileInputStream fis = context.openFileInput(TRIPS_FILENAME);
			ObjectInputStream is = new ObjectInputStream(fis);
			trips = (List<Trip>) is.readObject();
			is.close();
		}
		catch (Exception e)
		{
			trips = new ArrayList<Trip>();
		}
		return trips;
	}

	private static void saveTrips(Context context, List<Trip> trips)
	{
		Log.e(TAG, "saveTrips");
		try
		{
			FileOutputStream fos = context.openFileOutput(TRIPS_FILENAME, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(trips);
			os.close();
		}
		catch (Exception e)
		{
			// Nothing can be done
			Log.e(TAG, e.toString());
		}
	}

	private static boolean isInHomeArea(double latitude, double longitude)
	{
		Log.e(TAG, "isInHomeArea1");
		LatLng home = AccountController.getInstance().getHome();
		if (home == null) return false;
		Log.e(TAG, "isInHomeArea2");

		float[] deltaLocationHome = new float[3];
		Location.distanceBetween(home.latitude, home.longitude, latitude, longitude, deltaLocationHome);
		Log.e(TAG, deltaLocationHome[0] + "");
		return deltaLocationHome[0] < RADIUS_HOME_AREA_IN_METER;
	}
}
