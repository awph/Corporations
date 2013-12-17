package ch.hearc.corporations.model;

import java.util.Comparator;

import android.util.Log;
import ch.hearc.corporations.view.TerritoriesFragment;

import com.google.android.gms.maps.model.LatLng;

public class LatLngComparator implements Comparator<LatLng>
{

	@Override
	public int compare(LatLng latLng1, LatLng latLng2)
	{
		LatLng currentLocation = TerritoriesFragment.currentLocation;
		double distanceToLatLng1 = Math.pow(currentLocation.latitude - latLng1.latitude, 2) + Math.pow(currentLocation.longitude - latLng1.longitude, 2);
		double distanceToLatLng2 = Math.pow(currentLocation.latitude - latLng2.latitude, 2) + Math.pow(currentLocation.longitude - latLng2.longitude, 2);
		if (Double.compare(distanceToLatLng1, distanceToLatLng2) == 0)
		{
			Log.i("compare", latLng1.toString());
			Log.i("compare", latLng2.toString());
		}
		return Double.compare(distanceToLatLng1, distanceToLatLng2);
	};
}
