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

import java.util.HashSet;
import java.util.Set;

import android.widget.Toast;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.DataLoaderUtil;
import ch.hearc.corporations.view.TerritoryInfoFragment.Callback;

import com.facebook.Request;
import com.facebook.Response;

/**
 * @author Alexandre
 * 
 */
public class Player
{

	/*------------------------------------------------------------------*\
	|*							Protected Attributes					*|
	\*------------------------------------------------------------------*/

	protected String		userId;
	protected String		name;
	protected int			rank;
	protected int			numberAllies;
	protected int			numberTerritories;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private boolean			ally;				// Alliance current logged
												// player
												// (profile) -> this player

	private Set<Territory>	territories;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

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
		this.territories = new HashSet<Territory>();
		loadFullName();
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public void updateAlliance(final Callback callback)
	{
		final boolean isAlly = this.ally;
		DataLoader.getInstance().updateAlliance(this, new DataLoaderAdapter() {
			@Override
			public void allianceUpdated(int status)
			{
				if (status == DataLoaderUtil.ResultKeys.StatusKey.ALREADY_EXISTS || status == DataLoaderUtil.ResultKeys.StatusKey.DONT_EXISTS)
				{
					int messageId = R.string.alliance_update_error_message;
					if (isAlly) messageId = R.string.unalliance_update_error_message;
					Toast.makeText(Corporations.getAppContext(), Corporations.getAppContext().getResources().getString(messageId), Toast.LENGTH_LONG).show();
				}
				else
				{
					Player.this.ally = !Player.this.ally;
					for (Territory territory : territories)
						territory.updateType();
					callback.update();
					AccountController.getInstance().updateProfile();
				}
			}
		});
	}

	public void addTerritory(Territory territory)
	{
		territories.add(territory);
	}

	@Override
	public String toString()
	{
		return "Player [userID=" + userId + ", name=" + name + ", rank=" + rank + ", numberAllies=" + numberAllies + ", nomberTerritories=" + numberTerritories + ", ally=" + ally + "]";
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

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

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the rank
	 */
	public int getRank()
	{
		return rank;
	}

	/**
	 * @return the userID
	 */
	public String getUserId()
	{
		return userId;
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
	 * @return the nomberTerritories
	 */
	public int getNumberTerritories()
	{
		return numberTerritories;
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(int rank)
	{
		this.rank = rank;
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
	 * @param ally
	 *            the ally to set
	 */
	public void setAlly(boolean ally)
	{
		this.ally = ally;
	}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	/**
	 * @return the ally
	 */
	public boolean isAlly()
	{
		return ally;
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
	 * @param numberTerritories
	 *            the numberTerritories to set
	 */
	public void setNumberTerritories(int numberTerritories)
	{
		this.numberTerritories = numberTerritories;
	}

}
