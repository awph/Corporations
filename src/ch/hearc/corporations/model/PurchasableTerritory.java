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

	public PurchasableTerritory(double latitude, double longitude, Player owner, long timeOwned, int salePrice, int purchasingPrice)
	{
		super(latitude, longitude, owner, timeOwned);
		this.salePrice = salePrice;
		this.purchasingPrice = purchasingPrice;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public void buy()
	{

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
