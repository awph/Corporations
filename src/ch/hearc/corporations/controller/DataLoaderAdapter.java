/*=====================================================================*
| This file declares the following classes:
|    DataLoaderAdapter.java
|
| Description of the class DataLoaderAdapter.java :
|
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

	public void leaderboardFetched(List<Player> players)
	{
	}

	public void profileFetched(Status status)
	{
	}

	public void tripsFetched(List<Trip> trips)
	{
	}

	public void territoriesFetched(List<Territory> territories)
	{
	}

	public void territoryPurchased(PurchasableTerritory territory)
	{
	}

	public void territoryCaptured(SpecialTerritory territory)
	{
	}

	public void connectionFinished(Status status)
	{
	}

	public void allianceUpdated(int status)
	{
	}

	public void profileUpdated(Status status)
	{
	}

	public void tripUpdated(int status)
	{
	}
}
