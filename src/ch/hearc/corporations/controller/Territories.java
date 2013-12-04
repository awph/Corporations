package ch.hearc.corporations.controller;

import java.util.SortedSet;
import java.util.TreeSet;

import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.Territory;
import ch.hearc.corporations.view.TerritoriesFragment;

import com.google.android.gms.maps.model.LatLng;

public class Territories
{
	private SortedSet<Territory>	territories	= new TreeSet<Territory>();

	public Territories()
	{
		if (TerritoriesFragment.currentLocation != null) fillSetForLocation(TerritoriesFragment.currentLocation);
	}

	public Object[] getTerritoryPolygoneForLocation(LatLng location)
	{
		fillSetForLocation(location);
		SortedSet<Territory> territories = new TreeSet<Territory>();
		territories.addAll(this.territories);
		this.territories = territories;
		this.territories.add(new PurchasableTerritory(location));
		return this.territories.toArray();
	}

	private void fillSetForLocation(LatLng currentLocation)
	{
		for(int i = 0; i < 10; ++i)
			for(int j = 0; j < 10; ++j)
			{
				LatLng border = new LatLng(currentLocation.latitude + (i-5) * 0.01D, currentLocation.longitude + (j-5) * 0.01D);
				this.territories.add(new PurchasableTerritory(border));
			}
	}

}
