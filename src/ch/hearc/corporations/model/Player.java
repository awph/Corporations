 /*=====================================================================*
 | This file declares the following classes:
 |    Player.java
 |
 | Description of the class Player.java :
 |
 |
 | <p>Copyright : EIAJ, all rights reserved</p>
 | @autor : Alexandre
 | @version : 3 déc. 2013
 |
 *=====================================================================*/

package ch.hearc.corporations.model;

/**
 * @author Alexandre
 *
 */
public class Player
{
	private String userID;
	private String name;
	private int rank;
	private int numberAllies;
	private int nomberTerritories;
	private boolean ally; // Alliance current logged player (profile) -> this player
	
	public Player(String userID, int rank, int numberAllies, int nomberTerritories, boolean ally)
	{
		this.userID = userID;
		this.rank = rank;
		this.numberAllies = numberAllies;
		this.nomberTerritories = nomberTerritories;
		this.ally = ally;
	}

	@Override
	public String toString()
	{
		return "Player [userID=" + userID + ", name=" + name + ", rank=" + rank + ", numberAllies=" + numberAllies + ", nomberTerritories=" + nomberTerritories + ", ally=" + ally + "]";
	}
	

}
