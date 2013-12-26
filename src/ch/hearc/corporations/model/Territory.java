package ch.hearc.corporations.model;

import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.view.TerritoriesFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public abstract class Territory implements Comparable<Territory>
{
	protected int			revenue;
	private TerritoyType	type;
	private Player			owner;
	protected long			timeOwned;

	private double[]		latitudes;
	private double[]		longitudes;

	/**
	 * @return the type
	 */
	public TerritoyType getType()
	{
		return type;
	}

	/**
	 * @return the revenue
	 */
	public int getRevenue()
	{
		return revenue;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner()
	{
		return owner;
	}

	/**
	 * @return the timeOwned
	 */
	public long getTimeOwned()
	{
		return timeOwned;
	}

	public long getTotalGain()
	{
		return timeOwned * revenue;
	}

	public double getLatitude()
	{
		return latitudes[CENTER];
	}

	public double getLongitude()
	{
		return longitudes[CENTER];
	}

	private Polygon				polygon;
	private PolygonOptions		polygonOptions;

	private static final int	TOP_LEFT					= 0;
	private static final int	TOP_RIGHT					= 1;
	private static final int	BOTTOM_LEFT					= 2;
	private static final int	BOTTOM_RIGHT				= 3;
	private static final int	CENTER						= 4;
	private static double		deltaLat					= 0;
	private static float[]		results						= new float[3];
	private static final int	TERRITORY_SIZE_IN_METER		= 5000;
	private static final double	TERRITORY_SIZE_IN_LAT_LON	= 0.01D;
	private static final float	BORDER_WIDTH				= 1f;
	public static final int		BORDER_COLOR				= Color.argb(150, 0, 0, 0);

	public Territory(double latitude, double longitude, Player owner, long timeOwned)
	{
		this.owner = owner;
		this.timeOwned = timeOwned;
		this.polygon = null;
		if (owner != null)
		{
			if (owner.getUserID().equals(AccountController.getInstance().getProfile().getUserID()))
				this.type = TerritoyType.Mine;
			else if (owner.isAlly())
				this.type = TerritoyType.Ally;
			else
				this.type = TerritoyType.Enemy;

			owner.addTerritory(this);
		}
		else
			this.type = TerritoyType.Free;

		latitudes = new double[5];
		longitudes = new double[5];

		if (deltaLat == 0)
		{
			deltaLat = computeLatitudeDeltaForMeters();
			Log.d("Log : Delta Lat", Double.toString(deltaLat));
		}

		boolean simple = true;
		if (simple)
		{
			double topLatitude = getTopLatitudeTerritoryForLatitude2(latitude);
			double leftLongitude = getLeftLongitudeTerritoryForLatitudeAndLongitude2(longitude);

			latitudes[TOP_LEFT] = topLatitude;
			longitudes[TOP_LEFT] = leftLongitude;

			latitudes[TOP_RIGHT] = topLatitude;
			longitudes[TOP_RIGHT] = leftLongitude + TERRITORY_SIZE_IN_LAT_LON;

			latitudes[BOTTOM_RIGHT] = topLatitude - TERRITORY_SIZE_IN_LAT_LON;
			longitudes[BOTTOM_RIGHT] = leftLongitude + TERRITORY_SIZE_IN_LAT_LON;

			latitudes[BOTTOM_LEFT] = topLatitude - TERRITORY_SIZE_IN_LAT_LON;
			longitudes[BOTTOM_LEFT] = leftLongitude;

			latitudes[CENTER] = topLatitude - TERRITORY_SIZE_IN_LAT_LON / 2;
			longitudes[CENTER] = leftLongitude + TERRITORY_SIZE_IN_LAT_LON / 2;

			polygonOptions = new PolygonOptions()
					.add(new LatLng(latitudes[TOP_LEFT], longitudes[TOP_LEFT]), new LatLng(latitudes[TOP_RIGHT], longitudes[TOP_RIGHT]), new LatLng(latitudes[BOTTOM_RIGHT], longitudes[BOTTOM_RIGHT]),
							new LatLng(latitudes[BOTTOM_LEFT], longitudes[BOTTOM_LEFT])).strokeColor(BORDER_COLOR).fillColor(type.getColor()).strokeWidth(BORDER_WIDTH);
		}
		else
		{

			double topLatitude = getTopLatitudeTerritoryForLatitude(latitude);
			double[] leftLongitudeAndDelta = getLeftLongitudeTerritoryForLatitudeAndLongitude(topLatitude - deltaLat * TERRITORY_SIZE_IN_METER / 2, longitude);
			double leftLongitude = leftLongitudeAndDelta[0];
			double deltaLon = leftLongitudeAndDelta[1];

			latitudes[TOP_LEFT] = topLatitude;
			longitudes[TOP_LEFT] = topLatitude;

			latitudes[TOP_RIGHT] = topLatitude;
			longitudes[TOP_RIGHT] = leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER;

			latitudes[BOTTOM_RIGHT] = topLatitude - deltaLat * TERRITORY_SIZE_IN_METER;
			longitudes[BOTTOM_RIGHT] = leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER;

			latitudes[BOTTOM_LEFT] = topLatitude - deltaLat * TERRITORY_SIZE_IN_METER;
			longitudes[BOTTOM_LEFT] = leftLongitude;

			latitudes[CENTER] = topLatitude - deltaLat * TERRITORY_SIZE_IN_METER / 2;
			longitudes[CENTER] = leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER / 2;

			polygonOptions = new PolygonOptions()
					.add(new LatLng(latitudes[TOP_LEFT], longitudes[TOP_LEFT]), new LatLng(latitudes[TOP_RIGHT], longitudes[TOP_RIGHT]), new LatLng(latitudes[BOTTOM_RIGHT], longitudes[BOTTOM_RIGHT]),
							new LatLng(latitudes[BOTTOM_LEFT], longitudes[BOTTOM_LEFT])).strokeColor(BORDER_COLOR).fillColor(type.getColor()).strokeWidth(BORDER_WIDTH);
		}

		// Log.d("Log : Territory", points[CENTER].toString());
		// Log.d("Log : Territory points", Arrays.toString(points));
		// Log.d("Log : Territory", location.toString());
		// Log.d("Log : Territory", "delta Lat : " + (location.latitude -
		// points[CENTER].latitude) + " ||| delta Lon : " + (location.longitude
		// - points[CENTER].longitude));
	}

	public boolean isInBounds(double latitude, double longitude)
	{
		return (latitude < latitudes[TOP_LEFT] && latitude > latitudes[BOTTOM_LEFT] && longitude > longitudes[TOP_LEFT] && longitude < longitudes[TOP_RIGHT]);
	}

	@Override
	public int compareTo(Territory second)
	{
		LatLng currentLocation = TerritoriesFragment.currentLocation;
		double distanceToLatLng1 = Math.pow(currentLocation.latitude - latitudes[CENTER], 2) + Math.pow(currentLocation.longitude - longitudes[CENTER], 2);
		double distanceToLatLng2 = Math.pow(currentLocation.latitude - second.latitudes[CENTER], 2) + Math.pow(currentLocation.longitude - second.longitudes[CENTER], 2);
		return Double.compare(distanceToLatLng1, distanceToLatLng2);
	}

	private double getLeftLongitudeTerritoryForLatitudeAndLongitude2(double longitude)
	{
		int sign = (longitude >= 0) ? 1 : -1;
		return Math.round((longitude - (longitude % TERRITORY_SIZE_IN_LAT_LON) * sign) / TERRITORY_SIZE_IN_LAT_LON) * TERRITORY_SIZE_IN_LAT_LON;
	}

	private double getTopLatitudeTerritoryForLatitude2(double latitude)
	{
		int sign = (latitude >= 0) ? 1 : -1;
		return Math.round((latitude + TERRITORY_SIZE_IN_LAT_LON - (latitude % TERRITORY_SIZE_IN_LAT_LON) * sign) / TERRITORY_SIZE_IN_LAT_LON) * TERRITORY_SIZE_IN_LAT_LON;
	}

	private double[] getLeftLongitudeTerritoryForLatitudeAndLongitude(double latitude, double longitude)
	{
		double deltaLon = 0.001D;
		Location.distanceBetween(latitude, 0D, latitude, deltaLon, results);
		double ratio = (1D / results[0]);
		deltaLon *= ratio;

		Location.distanceBetween(latitude, 0D, latitude, longitude, results);
		int sign = (longitude >= 0) ? 1 : -1;

		return new double[] { sign * (results[0] - (results[0] % TERRITORY_SIZE_IN_METER)) * deltaLon, deltaLon };
		// Location.distanceBetween(latitude1, 0D, latitude2, longitude,
		// results);
		// float offset = results[0] - (results[0] % 50);
		// return (float) (longitude - 2 *
		// Math.asin(Math.sqrt(-Math.cos(latitude1) * Math.cos(latitude2) -
		// Math.pow(Math.sin(0.5 * (latitude2 - latitude1)), 2) +
		// Math.pow(Math.sin(offset / (2 * earthRadius)), 2))));

	}

	private double getTopLatitudeTerritoryForLatitude(double latitude)
	{
		Location.distanceBetween(0D, 0D, latitude, 0D, results);
		float lat = results[0];
		Log.d("Log", Float.toString(results[0]));
		Log.d("Log", Double.toString(latitude));
		int sign = (latitude >= 0) ? 1 : -1;
		Location.distanceBetween(0D, 0D, sign * (results[0]) * deltaLat, 0D, results);
		Log.d("Log", Float.toString(results[0]));
		Log.d("Log", Double.toString(sign * (results[0]) * deltaLat));
		double delta = (1 - lat / results[0]) / results[0];
		lat -= delta * lat * results[0];
		return sign * (lat - (lat % TERRITORY_SIZE_IN_METER)) * deltaLat;
	}

	private double computeLatitudeDeltaForMeters()
	{
		double delta = 10D;
		Location.distanceBetween(0D, 0D, delta, 0D, results);
		double ratio = (1D / results[0]);
		delta *= ratio;

		Log.d("Log", Float.toString(results[0]));

		// Location.distanceBetween(-90D, 0D, 90D, 0D, results);
		// Log.d("Log", Float.toString(2 * results[0] / 1000)); // 40007.863km
		//
		// Location.distanceBetween(90D, 0D, 90D, 180D, results);
		// Log.d("Log", Float.toString(2 * results[0] / 1000)); // 39807.188km

		return delta;
	}

	public void setMap(GoogleMap map)
	{
		polygon = map.addPolygon(polygonOptions);
		polygonOptions = null;
	}

	public void setVisible(boolean visible)
	{
		polygon.setVisible(visible);
	}

	/**
	 * @return the polygon
	 */
	public Polygon getPolygon()
	{
		return polygon;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Territory other = (Territory) obj;
		Log.d("Log : Territory", "" + (int) ((latitudes[CENTER] + longitudes[CENTER]) / TERRITORY_SIZE_IN_LAT_LON / 0.1));
		return (int) ((latitudes[CENTER] + longitudes[CENTER]) / TERRITORY_SIZE_IN_LAT_LON / 0.1) == (int) ((other.latitudes[CENTER] + other.longitudes[CENTER]) / TERRITORY_SIZE_IN_LAT_LON / 0.1);
	}

	@Override
	public int hashCode()
	{
		return (int) ((latitudes[CENTER] + longitudes[CENTER]) / TERRITORY_SIZE_IN_LAT_LON / 0.1);
	}

	public void setHighlighted(boolean highlighted)
	{
		polygon.setStrokeWidth(highlighted ? 5f : 1f);
	}

	public void updateAlliance()
	{
		if (owner.isAlly())
			this.type = TerritoyType.Ally;
		else
			this.type = TerritoyType.Enemy;
		if (polygon != null) polygon.setFillColor(this.type.getColor());
	}
}
