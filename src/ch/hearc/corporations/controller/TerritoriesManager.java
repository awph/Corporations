/*=====================================================================*
| This file declares the following classes:
|    TerritoriesManager.java
|
| Description of the class TerritoriesManager.java :
| This class contains all the logic, for pulling territories from the
| server, store the territories and make a connect between view and model 
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import ch.hearc.corporations.Tools;
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.Territory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

public class TerritoriesManager
{
	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private SortedSet<Territory>	territories	= new TreeSet<Territory>();
	private Boolean					fetchRunning;
	private float					farthestTerritory;
	private LatLng					lastFetchingLocation;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public TerritoriesManager()
	{
		fetchRunning = false;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Check if the position request is out the radius of already fetched
	 * territories. If not fetch territories from the server. Then add the
	 * territories to the map.
	 * 
	 * @param location
	 *            location that you want territories
	 * @param map
	 *            on witch one you want to add territories
	 */
	public void fetchTerritoriesForLocation(final LatLng location, final GoogleMap map)
	{
		if (!fetchRunning)
		{
			synchronized (fetchRunning)
			{
				fetchRunning = true;
			}
			if (lastFetchingLocation != null && farthestTerritory < Tools.distanceBetween(lastFetchingLocation.latitude, lastFetchingLocation.longitude, location.latitude, location.longitude))
			{

				synchronized (fetchRunning)
				{
					fetchRunning = false;
				}
				return;
			}
			lastFetchingLocation = location;
			DataLoader.getInstance().getTerritoriesForLocation(location, 0, new DataLoaderAdapter() {

				@Override
				public void territoriesFetched(List<Territory> territories, Status status)
				{
					if (status == Status.OK)
					{
						TerritoriesManager.this.territories.addAll(territories);
						TerritoriesManager.this.setVisibleTerritories(map);
						for (Territory territory : territories)
						{
							Math.max(farthestTerritory, Tools.distanceBetween(location.latitude, location.longitude, territory.getLatitude(), territory.getLongitude()));
						}
						fetchRunning = false;
					}
				}
			});
		}
	}

	/**
	 * Return the territory for location passed in parameter. In there are no
	 * territory create a free one.
	 * 
	 * @param location
	 *            location of the territory that you want.
	 * @param map
	 *            map on witch the territory is
	 * @return the territory wanted
	 */
	public Territory getTerritoryForLocation(LatLng location, GoogleMap map)
	{
		Territory territory = null;
		for (Territory existingTerritory : territories)
		{
			if (existingTerritory.isInBounds(location.latitude, location.longitude))
			{
				territory = existingTerritory;
				break;
			}
		}
		if (territory == null) territory = new PurchasableTerritory(location.latitude, location.longitude, null, 0, PurchasableTerritory.NO_PRICE, 0, 0);
		this.territories.add(territory);
		if (territory.getPolygon() == null) territory.setMap(map);
		return territory;
	}

	/**
	 * Check if the territory is connected of one of the player or ally.
	 * 
	 * @param territory
	 *            that we want to know if it's connected
	 * @return true if the territory is connected
	 */
	public boolean isConnected(Territory territory)
	{
		double latitude = territory.getLatitude();
		double longitude = territory.getLongitude();

		for (Territory existingTerritory : territories)
		{
			if (existingTerritory.isAConnectedTerritory(latitude + Territory.TERRITORY_SIZE_IN_LAT_LON, longitude)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude + Territory.TERRITORY_SIZE_IN_LAT_LON, longitude + Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude, longitude + Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude - Territory.TERRITORY_SIZE_IN_LAT_LON, longitude + Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude - Territory.TERRITORY_SIZE_IN_LAT_LON, longitude)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude - Territory.TERRITORY_SIZE_IN_LAT_LON, longitude - Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude, longitude - Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
			if (existingTerritory.isAConnectedTerritory(latitude + Territory.TERRITORY_SIZE_IN_LAT_LON, longitude - Territory.TERRITORY_SIZE_IN_LAT_LON)) return true;
		}
		return false;
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Set all territory visible, and add they to the map
	 * 
	 * @param map
	 *            the map witch has the territories
	 */
	private void setVisibleTerritories(GoogleMap map)
	{
		for (Territory territory : territories)
		{
			if (territory.getPolygon() == null) territory.setMap(map);
			territory.setVisible(true);
		}
	}

	@Deprecated
	@SuppressWarnings("unused")
	private void fillSetForLocation(LatLng currentLocation)
	{
		for (int i = 0; i < 10; ++i)
			for (int j = 0; j < 10; ++j)
				this.territories.add(new PurchasableTerritory(currentLocation.latitude + (i - 5) * 0.01D, currentLocation.longitude + (j - 5) * 0.01D, new Player("0", 0, 0, 0, true), 0, 0, 0, 0));

	}
}
