/*=====================================================================*
| This file declares the following classes:
|    PlayersManager.java
|
| Description of the class PlayersManager.java :
| This class contains all the players once. Very useful for manage alliance
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 17 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hearc.corporations.model.Player;

/**
 * @author Alexandre
 * 
 */
public class PlayersManager
{
	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private static PlayersManager	instance;
	private Map<String, Player>		players;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	private PlayersManager()
	{
		players = new HashMap<String, Player>();
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * @return the static instance of PlayersManager
	 */
	public static PlayersManager getInstance()
	{
		if (instance == null)
		{
			instance = new PlayersManager();
		}
		return instance;
	}

	/**
	 * Add a player to the list of players if it's not already in, and return
	 * it.
	 * 
	 * @param userID
	 *            the player userID
	 * @param isAlly
	 *            true if it's a ally
	 * @return the player that correspond to the userID
	 */
	public Player createOrGetPlayerForUserID(String userID, boolean isAlly)
	{
		if (players.containsKey(userID))
			return players.get(userID);
		else
		{
			players.put(userID, new Player(userID, 0, 0, 0, isAlly));
			return players.get(userID);
		}
	}

	/**
	 * @param userID
	 *            the userID of the player that we want
	 * @return the corresponding player
	 */
	public Player getPlayerForUserID(String userID)
	{
		return players.get(userID);
	}

	/**
	 * @return the list of all players
	 */
	public List<Player> getPlayers()
	{
		return new ArrayList<Player>(players.values());
	}

	/**
	 * Add a player to the list of players if it's not already in, update it
	 * otherwise. And return the player
	 * 
	 * @param userId
	 *            the player userID
	 * @param rank
	 *            the player rank
	 * @param numberTerritories
	 *            the player quantity of territories
	 * @param ally
	 *            true if it's an ally
	 * @return the corresponding player
	 */
	public Player createOrUpdatePlayerForUserID(String userId, int rank, int numberTerritories, Boolean ally)
	{
		Player player = players.get(userId);
		if (player == null)
		{
			player = new Player(userId, rank, 0, numberTerritories, ally);
			players.put(userId, player);
		}
		else
		{
			player.setRank(rank);
			player.setNumberTerritories(numberTerritories);
			player.setAlly(ally);
		}
		return player;
	}

}
