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
import android.util.Log;
import ch.hearc.corporations.Tools;
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

	private static final int	RADIUS_HOME_AREA_IN_METER	= 1000;
	private static final String	TRIPS_FILENAME				= "trips.bin";
	private static final String	TAG							= TripManager.class.getSimpleName();

	/**
	 * Add the location passed in parameter in the current trip.
	 * 
	 * @param context
	 *            app context
	 * @param latitude
	 *            latitude of the location
	 * @param longitude
	 *            longitude of the location
	 */
	public static void addLocation(Context context, double latitude, double longitude)
	{
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

	/**
	 * Load and return trips saved on device
	 * 
	 * @param context
	 *            app context
	 * @return the list of trips
	 */
	@SuppressWarnings("unchecked")
	public static List<Trip> loadTrips(Context context)
	{
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

	/**
	 * Save the trips to the device
	 * 
	 * @param context
	 *            app context
	 * @param trips
	 *            the list of trips to save
	 */
	public static void saveTrips(Context context, List<Trip> trips)
	{
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

	/**
	 * Check if the location is near the home location. Look at the
	 * {@link RADIUS_HOME_AREA_IN_METER} for zone size
	 * 
	 * @param latitude
	 *            the latitude of the current location
	 * @param longitude
	 *            the longitude of the current location
	 * @return true if it's in home area
	 */
	private static boolean isInHomeArea(double latitude, double longitude)
	{
		LatLng home = AccountController.getInstance().getHome();
		if (home == null) return false;

		return Tools.distanceBetween(home.latitude, home.longitude, latitude, longitude) < RADIUS_HOME_AREA_IN_METER;
	}
}
