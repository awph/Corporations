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
import java.util.List;

import com.facebook.Request;
import com.facebook.Response;

import android.widget.Toast;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.DataLoaderUtil;
import ch.hearc.corporations.view.TerritoryInfoFragment.Callback;

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
	private int				numberTerritories;
	private boolean			ally;				// Alliance current logged
												// player
												// (profile) -> this player
	private List<Territory>	territories;

	public Player(String userID, int rank, int numberAllies, int numberTerritories, boolean ally)
	{
		this.userID = userID;
		this.rank = rank;
		this.numberAllies = numberAllies;
		this.numberTerritories = numberTerritories;
		this.ally = ally;
		this.territories = new ArrayList<Territory>();
		loadFullName();
	}

	@Override
	public String toString()
	{
		return "Player [userID=" + userID + ", name=" + name + ", rank=" + rank + ", numberAllies=" + numberAllies + ", nomberTerritories=" + numberTerritories + ", ally=" + ally + "]";
	}
	
	/**
	 * @return the rank
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(int rank)
	{
		this.rank = rank;
	}

	/**
	 * @return the ally
	 */
	public boolean isAlly()
	{
		return ally;
	}

	/**
	 * @param ally the ally to set
	 */
	public void setAlly(boolean ally)
	{
		this.ally = ally;
	}

	/**
	 * @return the userID
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the numberAllies
	 */
	public int getNumberAllies()
	{
		return numberAllies;
	}

	/**
	 * @param numberTerritories the numberTerritories to set
	 */
	public void setNumberTerritories(int numberTerritories)
	{
		this.numberTerritories = numberTerritories;
	}

	/**
	 * @return the nomberTerritories
	 */
	public int getNumberTerritories()
	{
		return numberTerritories;
	}

	public void updateAlliance(final Callback callback)
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
					callback.update();
					for (Territory territory : territories)
						if (territory != null) // TODO: WTF
							territory.updateAlliance();
				}
			}
		});
	}

	public void addTerritory(Territory territory)
	{
		territories.add(territory);
	}

	private void loadFullName()
	{
		Request.newGraphPathRequest(null, userID, new Request.Callback() {

			@Override
			public void onCompleted(Response response)
			{
				if (response.getGraphObject() != null) name = response.getGraphObject().getProperty("name").toString();
			}
		}).executeAsync();
	}

}
