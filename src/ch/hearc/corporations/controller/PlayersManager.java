/*=====================================================================*
| This file declares the following classes:
|    PlayersManager.java
|
| Description of the class PlayersManager.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 17 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.util.HashMap;
import java.util.Map;

import ch.hearc.corporations.model.Player;

/**
 * @author Alexandre
 * 
 */
public class PlayersManager
{
	private static PlayersManager instance; 
	private Map<String, Player>	players;

	public static PlayersManager getInstance()
	{
		if(instance == null)
		{
			instance = new PlayersManager();
		}
		return instance;
	}
	
	private PlayersManager()
	{
		players = new HashMap<String, Player>();
	}

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
	
	public Player getPlayerForUserID(String userID)
	{
		return players.get(userID);
	}

}
