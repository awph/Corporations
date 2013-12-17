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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.widget.Toast;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.DataLoaderUtil;

/**
 * @author Alexandre
 * 
 */
public class Player
{
	private String			userID;
	private String			name;
	private int				rank;
	private int				numberAllies;
	private int				nomberTerritories;
	private boolean			ally;				// Alliance current logged
												// player
												// (profile) -> this player
	private List<Territory>	territories;

	public Player(String userID, int rank, int numberAllies, int nomberTerritories, boolean ally)
	{
		this.userID = userID;
		this.rank = rank;
		this.numberAllies = numberAllies;
		this.nomberTerritories = nomberTerritories;
		this.ally = ally;
		this.territories = new LinkedList<Territory>();
	}

	@Override
	public String toString()
	{
		return "Player [userID=" + userID + ", name=" + name + ", rank=" + rank + ", numberAllies=" + numberAllies + ", nomberTerritories=" + nomberTerritories + ", ally=" + ally + "]";
	}

	public String getUserID()
	{
		return userID;
	}

	public boolean isAlly()
	{
		return ally;
	}

	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void askAlliance()
	{
		DataLoader.getInstance().updateAlliance(this, new DataLoaderAdapter() {
			@Override
			public void allianceUpdated(int status)
			{
				if (status == DataLoaderUtil.ResultKeys.StatusKey.ALREADY_EXISTS || status == DataLoaderUtil.ResultKeys.StatusKey.DONT_EXISTS)
					Toast.makeText(Corporations.getAppContext(), "Error during allianceUpdate", Toast.LENGTH_LONG).show(); // TODO:
				// better
				// message
				// text
				else
				{
					Player.this.ally = !Player.this.ally;
					for (Territory territory : territories)
						if(territory != null) //TODO: WTF
							territory.updateAlliance();
				}
			}
		});
	}

	public void revokeAlliance()
	{
		askAlliance();
	}

	public void addTerritory(Territory territory)
	{
		int i;
		if(territory == null)
			i = 1;
		territories.add(territory);
	}

}
