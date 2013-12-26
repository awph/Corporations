/*=====================================================================*
| This file declares the following classes:
|    Player.java
|
| Description of the class Player.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
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
	protected String		userId;
	protected String		name;
	protected int			rank;
	protected int			numberAllies;
	protected int			numberTerritories;
	private boolean			ally;				// Alliance current logged
												// player
												// (profile) -> this player
	private List<Territory>	territories;

	public Player()
	{
		this(null, 0, 0, 0, false);
	}

	public Player(String userID, int rank, int numberAllies, int numberTerritories, boolean ally)
	{
		this.userId = userID;
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
		return "Player [userID=" + userId + ", name=" + name + ", rank=" + rank + ", numberAllies=" + numberAllies + ", nomberTerritories=" + numberTerritories + ", ally=" + ally + "]";
	}

	/**
	 * @return the rank
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
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
	 * @param ally
	 *            the ally to set
	 */
	public void setAlly(boolean ally)
	{
		this.ally = ally;
	}

	/**
	 * @return the userID
	 */
	public String getUserId()
	{
		return userId;
	}

	/**
	 * @param userID
	 *            the userID to set
	 */
	public void setUserId(String userID)
	{
		this.userId = userID;
		loadFullName();
	}

	/**
	 * @param numberAllies
	 *            the numberAllies to set
	 */
	public void setNumberAllies(int numberAllies)
	{
		this.numberAllies = numberAllies;
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
	 * @param numberTerritories
	 *            the numberTerritories to set
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
		if (userId == null) return;
		Request.newGraphPathRequest(null, userId, new Request.Callback() {

			@Override
			public void onCompleted(Response response)
			{
				if (response.getGraphObject() != null) name = response.getGraphObject().getProperty("name").toString();
			}
		}).executeAsync();
	}

}
