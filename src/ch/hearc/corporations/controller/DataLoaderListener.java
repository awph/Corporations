package ch.hearc.corporations.controller;

import java.util.List;

import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Territory;
import ch.hearc.corporations.model.Trip;



public interface DataLoaderListener
{
	void leaderboardFetched(List<Player> players);
	void profileFetched(Profile profile);
	void territoriesFetched(List<Territory> territories);
	void tripsFetched(List<Trip> trips);
	void territoryPurchased(Territory territory);
	void territoryCaptured(Territory territory);
	void connexionFinished(Profile profile);
}
