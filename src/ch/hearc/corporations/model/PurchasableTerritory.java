/*=====================================================================*
| This file declares the following classes:
|    PurchasableTerritory.java
|
| Description of the class PurchasableTerritory.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.view.TerritoryInfoFragment.Callback;

/**
 * @author Alexandre
 * 
 */
public class PurchasableTerritory extends Territory
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private int	salePrice;
	private int	purchasingPrice;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public PurchasableTerritory(double latitude, double longitude, Player owner, long timeOwned, int salePrice, int purchasingPrice, int revenue)
	{
		super(latitude, longitude, owner, timeOwned, revenue);
		this.salePrice = salePrice;
		this.purchasingPrice = purchasingPrice;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public boolean buy(final Callback callback)
	{
		boolean canBuyTerritory = AccountController.getInstance().getProfile().getCurrentMoney() >= salePrice;// TODO
																												// border
																												// territory
		if (canBuyTerritory)
		{
			DataLoader.getInstance().purchaseTerritory(this, new DataLoaderAdapter() {

				@Override
				public void territoryPurchased(PurchasableTerritory territory)
				{
					PurchasableTerritory.this.salePrice = territory.salePrice;
					PurchasableTerritory.this.purchasingPrice = territory.purchasingPrice;
					PurchasableTerritory.this.revenue = territory.revenue;
					PurchasableTerritory.this.timeOwned = territory.timeOwned;
					PurchasableTerritory.this.owner = territory.owner;
					PurchasableTerritory.this.updateType();
					AccountController.getInstance().updateProfile();
					callback.update();
				}
			});
		}
		return canBuyTerritory;
	}

	public void changePrice()
	{
		// TODO Auto-generated method stub

	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the salePrice
	 */
	public int getSalePrice()
	{
		return salePrice;
	}

	/**
	 * @return the purchasingPrice
	 */
	public int getPurchasingPrice()
	{
		return purchasingPrice;
	}
}
