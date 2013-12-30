/*=====================================================================*
| This file declares the following classes:
|    SpecialTerritory.java
|
| Description of the class SpecialTerritory.java :
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
public class SpecialTerritory extends Territory
{
	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public SpecialTerritory(double latitude, double longitude, Player owner, long timeOwned, int revenue)
	{
		super(latitude, longitude, owner, timeOwned, revenue);
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public boolean capture(final Callback callback)
	{
		boolean canCapture = true; // TODO: check position
		if (canCapture)
		{
			DataLoader.getInstance().captureTerritory(this, new DataLoaderAdapter() {

				@Override
				public void territoryCaptured(SpecialTerritory territory)
				{
					SpecialTerritory.this.revenue = territory.revenue;
					SpecialTerritory.this.timeOwned = territory.timeOwned;
					SpecialTerritory.this.owner = territory.owner;
					SpecialTerritory.this.owner.addTerritory(SpecialTerritory.this);
					SpecialTerritory.this.updateType();
					AccountController.getInstance().updateProfile();
					callback.update();
				}
			});
		}
		return canCapture;
	}
}
