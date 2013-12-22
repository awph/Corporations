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
	private int	salePrice;
	private int	purchasingPrice;

	public PurchasableTerritory(double latitude, double longitude, Player owner, long timeOwned, int salePrice, int purchasingPrice)
	{
		super(latitude, longitude, owner, timeOwned);
		this.salePrice = salePrice;
		this.purchasingPrice = purchasingPrice;
	}

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

	public void buy()
	{

	}

	public void changePrice()
	{
		// TODO Auto-generated method stub

	}
}
