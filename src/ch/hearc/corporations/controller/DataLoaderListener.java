package ch.hearc.corporations.controller;

import java.util.List;

import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;
import ch.hearc.corporations.model.Trip;

public interface DataLoaderListener
{
	void leaderboardFetched(List<Player> players);
	void profileFetched(Status status);
	void territoriesFetched(List<Territory> territories);
	void tripsFetched(List<Trip> trips);
	void territoryPurchased(PurchasableTerritory territory);
	void territoryCaptured(SpecialTerritory territory);
	void connectionFinished(Status status);
	void allianceUpdated(int status);
	void profileUpdated(Status status);
	void tripUploaded(int status);
	void priceChanged(Status status);
}
