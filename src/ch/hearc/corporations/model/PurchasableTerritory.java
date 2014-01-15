/*=====================================================================*
| This file declares the following classes:
|    PurchasableTerritory.java
|
| Description of the class PurchasableTerritory.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import com.google.android.gms.maps.model.LatLng;

import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;
import ch.hearc.corporations.view.TerritoryInfoFragment.Callback;

/**
 * @author Alexandre
 * 
 */
public class PurchasableTerritory extends Territory
{

	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static final int	NO_PRICE	= -1;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private long			salePrice;
	private long			purchasingPrice;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public PurchasableTerritory(double latitude, double longitude, Player owner, long timeOwned, long salePrice, long purchasingPrice, int revenue)
	{
		super(latitude, longitude, owner, timeOwned, revenue);
		this.salePrice = (salePrice == NO_PRICE) ? computePrice() : salePrice;
		this.purchasingPrice = purchasingPrice;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public boolean buy(final Callback callback)
	{
		boolean canBuyTerritory = AccountController.getInstance().getProfile().getCurrentMoney() >= salePrice;

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
					PurchasableTerritory.this.owner.addTerritory(PurchasableTerritory.this);
					PurchasableTerritory.this.updateType();
					AccountController.getInstance().updateProfile();
					callback.update();
				}
			});
		}
		return canBuyTerritory;
	}

	public void changePrice(final long newPrice, final Callback callback)
	{
		DataLoader.getInstance().changeTerritoryPrice(this, newPrice, new DataLoaderAdapter() {

			@Override
			public void priceChanged(Status status)
			{
				PurchasableTerritory.this.salePrice = newPrice;
				callback.update();
			}
		});
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	private int computePrice()
	{
		float distance = Tools.distanceBetween(AccountController.getInstance().getHome(), new LatLng(getLatitude(), getLongitude()));
		int price = (int) (10 * distance);
		price -= price * (AccountController.getInstance().getProfile().getSkill(SkillType.purchasePrice).getValue() / 100);
		return price;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the salePrice
	 */
	public long getSalePrice()
	{
		return salePrice;
	}

	/**
	 * @return the purchasingPrice
	 */
	public long getPurchasingPrice()
	{
		return purchasingPrice;
	}
}
