/*=====================================================================*
| This file declares the following classes:
|    DataLoaderAdapter.java
|
| Description of the class DataLoaderAdapter.java :
| Class for hide unwanted method of the DataLoaderListener
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.util.List;

import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;
import ch.hearc.corporations.model.Trip;

/**
 * @author Alexandre
 * 
 */
public abstract class DataLoaderAdapter implements DataLoaderListener
{

	public void leaderboardFetched(List<Player> players, Status status)
	{
	}

	public void profileFetched(Status status)
	{
	}

	public void tripsFetched(List<Trip> trips, Status status)
	{
	}

	public void territoriesFetched(List<Territory> territories, Status status)
	{
	}

	public void territoryPurchased(PurchasableTerritory territory, Status status)
	{
	}

	public void territoryCaptured(SpecialTerritory territory, Status status)
	{
	}

	public void connectionFinished(Status status)
	{
	}

	public void allianceUpdated(Status status)
	{
	}

	public void profileUpdated(Status status)
	{
	}

	public void tripUploaded(Status status)
	{
	}

	public void priceChanged(Status status)
	{
	}
}
