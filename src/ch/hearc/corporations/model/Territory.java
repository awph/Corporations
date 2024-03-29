/*=====================================================================*
| This file declares the following classes:
|    Territory.java
|
| Description of the class Territory.java :
| This abstract class contain all info of the territory.
| And method for compute position, and display on the map
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.util.Log;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.view.TerritoriesFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public abstract class Territory implements Comparable<Territory>
{

	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	// Size of the territory in degree
	public static final double	TERRITORY_SIZE_IN_LAT_LON	= 0.01D;

	/*------------------------------------------------------------------*\
	|*							Protected Attributes					*|
	\*------------------------------------------------------------------*/

	protected int				revenue;
	protected long				timeOwned;
	protected Player			owner;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private double[]			latitudes;
	private double[]			longitudes;
	private TerritoyType		type;
	private float				borderWidth;
	private Polygon				polygon;
	private PolygonOptions		polygonOptions;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final int	TOP_LEFT					= 0;
	private static final int	TOP_RIGHT					= 1;
	private static final int	BOTTOM_LEFT					= 2;
	private static final int	BOTTOM_RIGHT				= 3;
	private static final int	CENTER						= 4;
	private static double		deltaLat					= 0;
	private static float[]		results						= new float[3];
	private static final int	TERRITORY_SIZE_IN_METER		= 5000;
	private static final float	BORDER_WIDTH				= 1f;
	private static final float	BORDER_WIDTH_HIGHLIGHTED	= 5f;
	private static final float	BORDER_WIDTH_SPECIAL		= 2f;
	private static final int	BORDER_COLOR				= Color.argb(150, 0, 0, 0);

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	/**
	 * 
	 * @param latitude
	 *            of a point in the territory
	 * @param longitude
	 *            of a point in the territory
	 * @param owner
	 * @param timeOwned
	 *            is the time in second since the player got it
	 * @param revenue
	 */
	public Territory(double latitude, double longitude, Player owner, long timeOwned, int revenue)
	{
		this.owner = owner;
		this.timeOwned = timeOwned;
		this.revenue = revenue;
		this.polygon = null;
		latitudes = new double[5];
		longitudes = new double[5];

		if (this.owner != null) this.owner.addTerritory(this);

		updateType();

		if (deltaLat == 0)
		{
			deltaLat = computeLatitudeDeltaForMeters();
			Log.d("Log : Delta Lat", Double.toString(deltaLat));
		}

		computeLocation(latitude, longitude);
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Check if the current territory is in water. Check four pixels in each
	 * corner, and if in each corner at least one pixel is blue the territory is
	 * in water. The delta and the offset is for not detect a border of an other
	 * territory
	 * 
	 * @param projection
	 *            a projection object for convert from/to screen coordinates
	 *            to/from map coordinates
	 * @param bitmap
	 *            a image a the map
	 * @return true if territory is in water
	 */
	public boolean isInWater(Projection projection, Bitmap bitmap)
	{
		int WATER1 = 0x00ADCEFF;
		int WATER2 = 0x00ACCCFF;
		int OFFSET = 10;
		int DELTA = 8;

		// get the coordinate in screen of a territory point
		Point point = projection.toScreenLocation(new LatLng(latitudes[TOP_LEFT], longitudes[TOP_LEFT]));
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int x = point.x < OFFSET ? OFFSET : point.x < width ? point.x : width - OFFSET;
		int y = point.y < OFFSET ? OFFSET : point.y < height ? point.y : height - OFFSET;

		if (!((bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x + DELTA, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y + DELTA) & 0x00FFFFFF) == WATER1
				|| (bitmap.getPixel(x + DELTA, y + DELTA) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x + DELTA, y) & 0x00FFFFFF) == WATER2
				|| (bitmap.getPixel(x, y + DELTA) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x + DELTA, y + DELTA) & 0x00FFFFFF) == WATER2)) return false;
		point = projection.toScreenLocation(new LatLng(latitudes[TOP_RIGHT], longitudes[TOP_RIGHT]));
		x = point.x < OFFSET ? OFFSET : point.x < width ? point.x : width - OFFSET;
		y = point.y < OFFSET ? OFFSET : point.y < height ? point.y : height - OFFSET;
		if (!((bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x - DELTA, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y + DELTA) & 0x00FFFFFF) == WATER1
				|| (bitmap.getPixel(x - DELTA, y + DELTA) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x - DELTA, y) & 0x00FFFFFF) == WATER2
				|| (bitmap.getPixel(x, y + DELTA) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x - DELTA, y + DELTA) & 0x00FFFFFF) == WATER2)) return false;
		point = projection.toScreenLocation(new LatLng(latitudes[BOTTOM_RIGHT], longitudes[BOTTOM_RIGHT]));
		x = point.x < OFFSET ? OFFSET : point.x < width ? point.x : width - OFFSET;
		y = point.y < OFFSET ? OFFSET : point.y < height ? point.y : height - OFFSET;
		if (!((bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x - DELTA, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y - DELTA) & 0x00FFFFFF) == WATER1
				|| (bitmap.getPixel(x - DELTA, y - DELTA) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x - DELTA, y) & 0x00FFFFFF) == WATER2
				|| (bitmap.getPixel(x, y - DELTA) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x - DELTA, y - DELTA) & 0x00FFFFFF) == WATER2)) return false;
		point = projection.toScreenLocation(new LatLng(latitudes[BOTTOM_LEFT], longitudes[BOTTOM_LEFT]));
		x = point.x < OFFSET ? OFFSET : point.x < width ? point.x : width - OFFSET;
		y = point.y < OFFSET ? OFFSET : point.y < height ? point.y : height - OFFSET;
		if (!((bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x + DELTA, y) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y - DELTA) & 0x00FFFFFF) == WATER1
				|| (bitmap.getPixel(x + DELTA, y - DELTA) & 0x00FFFFFF) == WATER1 || (bitmap.getPixel(x, y) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x + DELTA, y) & 0x00FFFFFF) == WATER2
				|| (bitmap.getPixel(x, y - DELTA) & 0x00FFFFFF) == WATER2 || (bitmap.getPixel(x + DELTA, y - DELTA) & 0x00FFFFFF) == WATER2)) return false;

		return true;
	}

	/**
	 * Check if the territory is too far of the home player location, for know
	 * if the player can buy it.
	 * 
	 * @return true if the territory is too far
	 */
	public boolean isTooFar()
	{
		if(AccountController.getInstance().getProfile().getNumberTerritories() == 0) return false;
		float limit = AccountController.getInstance().getProfile().getSkill(SkillType.purchaseDistance).getValue();
		float distance = (float) (Math.sqrt(Math.pow(Math.abs(AccountController.getInstance().getHome().latitude - getLatitude()), 2)
				+ Math.pow(Math.abs(AccountController.getInstance().getHome().longitude - getLongitude()), 2)) / TERRITORY_SIZE_IN_LAT_LON);
		return limit < distance;
	}

	/**
	 * Check if the point is in the territory
	 * 
	 * @param latitude
	 *            of the point
	 * @param longitude
	 *            of the point
	 * @return true if the point is in the territory
	 */
	public boolean isInBounds(double latitude, double longitude)
	{
		return (latitude < latitudes[TOP_LEFT] && latitude > latitudes[BOTTOM_LEFT] && longitude > longitudes[TOP_LEFT] && longitude < longitudes[TOP_RIGHT]);
	}

	/**
	 * Check if the point passed in parameter is in this territory and if this
	 * territory is a player one's or a ally one's
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public boolean isAConnectedTerritory(double latitude, double longitude)
	{
		return isInBounds(latitude, longitude) && this.owner != null && (this.owner.isAlly() || this.owner.me());
	}

	/**
	 * Update the territory type, and it color.
	 */
	public void updateType()
	{
		if (owner != null)
		{
			if (owner.getUserId().equals(AccountController.getInstance().getProfile().getUserId()))
				this.type = TerritoyType.Mine;
			else if (owner.isAlly())
				this.type = TerritoyType.Ally;
			else
				this.type = TerritoyType.Enemy;
		}
		else
			this.type = TerritoyType.Free;
		if (polygon != null) polygon.setFillColor(this.type.getColor(this instanceof SpecialTerritory));
	}

	@Override
	public int compareTo(Territory second)
	{
		LatLng currentLocation = TerritoriesFragment.currentLocation;
		double distanceToLatLng1 = Math.pow(currentLocation.latitude - latitudes[CENTER], 2) + Math.pow(currentLocation.longitude - longitudes[CENTER], 2);
		double distanceToLatLng2 = Math.pow(currentLocation.latitude - second.latitudes[CENTER], 2) + Math.pow(currentLocation.longitude - second.longitudes[CENTER], 2);
		return Double.compare(distanceToLatLng1, distanceToLatLng2);
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

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Compute the correct location of the territory from the point passed in
	 * parameter
	 * 
	 * @param latitude
	 *            is a latitude in the territory
	 * @param longitude
	 *            is a longitude in the territory
	 */
	private void computeLocation(double latitude, double longitude)
	{
		boolean simple = true;
		boolean isSpecial = this instanceof SpecialTerritory;
		borderWidth = isSpecial ? BORDER_WIDTH_SPECIAL : BORDER_WIDTH;

		// simple mean latitude == longitude, so non-square territory
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

			createPolygonOptions(isSpecial);
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

			createPolygonOptions(isSpecial);
		}
	}

	/**
	 * Create the polygonOptions for display the territory on the map
	 * 
	 * @param isSpecial
	 *            true if it's a special territory
	 */
	private void createPolygonOptions(boolean isSpecial)
	{
		polygonOptions = new PolygonOptions()
				.add(new LatLng(latitudes[TOP_LEFT], longitudes[TOP_LEFT]), new LatLng(latitudes[TOP_RIGHT], longitudes[TOP_RIGHT]), new LatLng(latitudes[BOTTOM_RIGHT], longitudes[BOTTOM_RIGHT]),
						new LatLng(latitudes[BOTTOM_LEFT], longitudes[BOTTOM_LEFT])).strokeColor(BORDER_COLOR).fillColor(type.getColor(isSpecial)).strokeWidth(borderWidth);
	}

	/**
	 * Compute the left longitude of the territory from the longitude passed in
	 * parameter.
	 * 
	 * @param longitude
	 *            is a longitude in the territory
	 * @return the left longitude of the territory
	 */
	private double getLeftLongitudeTerritoryForLatitudeAndLongitude2(double longitude)
	{
		int sign = (longitude >= 0) ? 1 : -1;
		return Math.round((longitude - (longitude % TERRITORY_SIZE_IN_LAT_LON) * sign) / TERRITORY_SIZE_IN_LAT_LON) * TERRITORY_SIZE_IN_LAT_LON;
	}

	/**
	 * Compute the top latitude of the territory from the latitude passed in
	 * parameter.
	 * 
	 * @param latitude
	 *            is a longitude in the territory
	 * @return the top latitude of the territory
	 */
	private double getTopLatitudeTerritoryForLatitude2(double latitude)
	{
		int sign = (latitude >= 0) ? 1 : -1;
		return Math.round((latitude + TERRITORY_SIZE_IN_LAT_LON - (latitude % TERRITORY_SIZE_IN_LAT_LON) * sign) / TERRITORY_SIZE_IN_LAT_LON) * TERRITORY_SIZE_IN_LAT_LON;
	}

	/**
	 * Compute the left longitude of the territory from the longitude passed in
	 * parameter. With a deformation for a square territory.
	 * 
	 * @param longitude
	 *            is a longitude in the territory
	 * @return the left longitude of the territory
	 */
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

	/**
	 * Compute the top latitude of the territory from the latitude passed in
	 * parameter. With a deformation for a square territory.
	 * 
	 * @param latitude
	 *            is a longitude in the territory
	 * @return the top latitude of the territory
	 */
	private double getTopLatitudeTerritoryForLatitude(double latitude)
	{
		Location.distanceBetween(0D, 0D, latitude, 0D, results);
		float lat = results[0];
		int sign = (latitude >= 0) ? 1 : -1;
		Location.distanceBetween(0D, 0D, sign * (results[0]) * deltaLat, 0D, results);
		double delta = (1 - lat / results[0]) / results[0];
		lat -= delta * lat * results[0];
		return sign * (lat - (lat % TERRITORY_SIZE_IN_METER)) * deltaLat;
	}

	/**
	 * Compute the delta latitude, use after for have a square territory.
	 * 
	 * @return
	 */
	private double computeLatitudeDeltaForMeters()
	{
		double delta = 10D;
		Location.distanceBetween(0D, 0D, delta, 0D, results);
		double ratio = (1D / results[0]);
		delta *= ratio;
		return delta;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the revenue
	 */
	public int getRevenue()
	{
		return revenue;
	}

	/**
	 * @return the timeOwned
	 */
	public long getTimeOwned()
	{
		return timeOwned;
	}

	/**
	 * @return the center's latitude
	 */
	public double getLatitude()
	{
		return latitudes[CENTER];
	}

	/**
	 * @return the center's longitude
	 */
	public double getLongitude()
	{
		return longitudes[CENTER];
	}

	/**
	 * @return the type
	 */
	public TerritoyType getType()
	{
		return type;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner()
	{
		return owner;
	}

	/**
	 * @return the amount of money earned since capturing date
	 */
	public long getTotalGain()
	{
		return timeOwned * revenue;
	}

	/**
	 * @return the polygon
	 */
	public Polygon getPolygon()
	{
		return polygon;
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/**
	 * Add the territory to the map
	 * @param map is the map that will display the territory 
	 */
	public void setMap(GoogleMap map)
	{
		if (polygonOptions == null) createPolygonOptions(this instanceof SpecialTerritory);
		polygon = map.addPolygon(polygonOptions);
		polygon.setVisible(false);
		polygonOptions = null;
	}

	/**
	 * Set the territory visible
	 * @param visible
	 */
	public void setVisible(boolean visible)
	{
		polygon.setVisible(visible);
	}

	/**
	 * Change border size. And remove polygon if this is a free territory
	 * @param highlighted
	 */
	public void setHighlighted(boolean highlighted)
	{
		if (!highlighted && type == TerritoyType.Free)
		{
			if (polygon != null) polygon.remove();
			polygon = null;
		}
		else
		{
			polygon.setStrokeWidth(highlighted ? BORDER_WIDTH_HIGHLIGHTED : borderWidth);
		}
	}
}
