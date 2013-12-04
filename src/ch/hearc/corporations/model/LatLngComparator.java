package ch.hearc.corporations.model;

import java.util.Comparator;

import android.location.Location;

import ch.hearc.corporations.view.TerritoriesFragment;

import com.google.android.gms.maps.model.LatLng;

public class LatLngComparator implements Comparator<LatLng>
{

	@Override
	public int compare(LatLng latLng1, LatLng latLng2)
	{
		LatLng currentLocation = TerritoriesFragment.currentLocation;
		float[] results = new float[3];
		float distanceToLatLng1 = -1;
		float distanceToLatLng2 = -1;
		Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, latLng1.latitude, latLng1.longitude, results);
		if (results != null && results.length > 0) distanceToLatLng1 = results[0];
		Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, latLng2.latitude, latLng2.longitude, results);
		if (results != null && results.length > 0) distanceToLatLng2 = results[0];
		return Float.compare(distanceToLatLng1, distanceToLatLng2);
	};

}
