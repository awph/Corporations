package ch.hearc.corporations.controller;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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

	public Object[] getTerritoryPolygoneForLocation(final LatLng location, final GoogleMap map)
	{
		if (!fetchRunning)
		{
			fetchRunning = true;
			DataLoader.getInstance().getTerritoriesForLocation(location, 0, new DataLoaderAdapter() { // TODO

						@Override
						public void territoriesFetched(List<Territory> territories)
						{
							TerritoriesManager.this.territories.addAll(territories);
							TerritoriesManager.this.setVisibleTerritories(location, map);
							// fetchRunning = false;
						}
					});
		}

		// fillSetForLocation(location);
		// SortedSet<Territory> territories = new TreeSet<Territory>();
		// territories.addAll(this.territories);
		// this.territories = territories;
		return this.territories.toArray();
	}

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

	private void setVisibleTerritories(LatLng location, GoogleMap map)
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
