package ch.hearc.corporations.model;

import java.util.Random;

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

	private LatLng[]		points;

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

	/**
	 * @return the points
	 */
	public LatLng[] getPoints()
	{
		return points;
	}

	public long getTotalGain()
	{
		return timeOwned * revenue;
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
	private static final int	EARTH_RADIUS				= 6356752;
	private static final int	TERRITORY_SIZE_IN_METER		= 5000;
	private static final double	TERRITORY_SIZE_IN_LAT_LON	= 0.01D;

	public Territory(LatLng location, Player owner, long timeOwned)
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

		if (deltaLat == 0)
		{
			deltaLat = computeLatitudeDeltaForMeters();
			Log.d("Log : Delta Lat", Double.toString(deltaLat));
		}
		points = new LatLng[5];

		boolean simple = true;
		if (simple)
		{
			double topLatitude = getTopLatitudeTerritoryForLatitude2(location.latitude);
			double leftLongitude = getLeftLongitudeTerritoryForLatitudeAndLongitude2(location.longitude);

			points[TOP_LEFT] = new LatLng(topLatitude, leftLongitude);
			points[TOP_RIGHT] = new LatLng(topLatitude, leftLongitude + TERRITORY_SIZE_IN_LAT_LON);
			points[BOTTOM_LEFT] = new LatLng(topLatitude - TERRITORY_SIZE_IN_LAT_LON, leftLongitude);
			points[BOTTOM_RIGHT] = new LatLng(topLatitude - TERRITORY_SIZE_IN_LAT_LON, leftLongitude + TERRITORY_SIZE_IN_LAT_LON);
			points[CENTER] = new LatLng(topLatitude - TERRITORY_SIZE_IN_LAT_LON / 2, leftLongitude + TERRITORY_SIZE_IN_LAT_LON / 2);
			polygonOptions = new PolygonOptions().add(points[TOP_LEFT], points[TOP_RIGHT], points[BOTTOM_RIGHT], points[BOTTOM_LEFT]).strokeColor(TerritoriesFragment.BORDER_COLOR)
					.fillColor(type.getColor()).strokeWidth(1f);
		}
		else
		{

			double topLatitude = getTopLatitudeTerritoryForLatitude(location.latitude);
			double[] leftLongitudeAndDelta = getLeftLongitudeTerritoryForLatitudeAndLongitude(topLatitude - deltaLat * TERRITORY_SIZE_IN_METER / 2, location.longitude);
			double leftLongitude = leftLongitudeAndDelta[0];
			double deltaLon = leftLongitudeAndDelta[1];

			points[TOP_LEFT] = new LatLng(topLatitude, leftLongitude);
			points[TOP_RIGHT] = new LatLng(topLatitude, leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER);
			points[BOTTOM_LEFT] = new LatLng(topLatitude - deltaLat * TERRITORY_SIZE_IN_METER, leftLongitude);
			points[BOTTOM_RIGHT] = new LatLng(topLatitude - deltaLat * TERRITORY_SIZE_IN_METER, leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER);
			points[CENTER] = new LatLng(topLatitude - deltaLat * TERRITORY_SIZE_IN_METER / 2, leftLongitude + deltaLon * TERRITORY_SIZE_IN_METER / 2);
			polygonOptions = new PolygonOptions().add(points[TOP_LEFT], points[TOP_RIGHT], points[BOTTOM_RIGHT], points[BOTTOM_LEFT]).strokeColor(TerritoriesFragment.BORDER_COLOR)
					.fillColor(type.getColor()).strokeWidth(1f);
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
		return (latitude < points[TOP_LEFT].latitude && latitude > points[BOTTOM_LEFT].latitude && longitude > points[TOP_LEFT].longitude && longitude < points[TOP_RIGHT].longitude);
	}

	@Override
	public int compareTo(Territory another)
	{
		return new LatLngComparator().compare(points[CENTER], another.points[CENTER]);
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
		Log.d("Log : Territory", "" + (int) ((points[CENTER].latitude + points[CENTER].longitude) / TERRITORY_SIZE_IN_LAT_LON / 0.1));
		return (int) ((points[CENTER].latitude + points[CENTER].longitude) / TERRITORY_SIZE_IN_LAT_LON / 0.1) == (int) ((other.points[CENTER].latitude + other.points[CENTER].longitude)
				/ TERRITORY_SIZE_IN_LAT_LON / 0.1);
	}

	@Override
	public int hashCode()
	{
		Log.d("Log : Territory", "" + (int) ((points[CENTER].latitude + points[CENTER].longitude) / TERRITORY_SIZE_IN_LAT_LON / 0.1));
		Random random = new Random();
		return (int) ((points[CENTER].latitude + points[CENTER].longitude) / TERRITORY_SIZE_IN_LAT_LON / 0.1) * random.nextInt();
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
		polygon.setFillColor(this.type.getColor());
	}
}
