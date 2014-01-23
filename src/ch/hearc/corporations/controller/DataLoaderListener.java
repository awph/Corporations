/*=====================================================================*
| This file declares the following classes:
|    DataLoaderListener.java
|
| Description of the class DataLoaderListener.java :
| Class that contain all the API function callback
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

public interface DataLoaderListener
{
	void leaderboardFetched(List<Player> players, Status status);
	void profileFetched(Status status);
	void territoriesFetched(List<Territory> territories, Status status);
	void tripsFetched(List<Trip> trips, Status status);
	void territoryPurchased(PurchasableTerritory territory, Status status);
	void territoryCaptured(SpecialTerritory territory, Status status);
	void connectionFinished(Status status);
	void allianceUpdated(Status status);
	void profileUpdated(Status status);
	void tripUploaded(Status status);
	void priceChanged(Status status);
}
