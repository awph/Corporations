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
import ch.hearc.corporations.model.Profile;
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

	public void profileFetched(Profile profile)
	{
	}

	public void tripsFetched(List<Trip> trips)
	{
	}

	public void territoriesFetched(List<Territory> territories)
	{
	}

	public void territoryPurchased(Territory territory)
	{
	}

	public void territoryCaptured(Territory territory)
	{
	}

	public void connexionFinished(Profile profile)
	{
	}
}
