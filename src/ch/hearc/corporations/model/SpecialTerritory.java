/*=====================================================================*
| This file declares the following classes:
|    SpecialTerritory.java
|
| Description of the class SpecialTerritory.java :
| This class represent a special territory and the capture action.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import android.location.Location;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;
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

	/**
	 * Capture the territory, so check if it's possible, and request the server if it is.
	 * @param callback is use for notify the controller that the territory is captured
	 * @param location is the location of the player
	 * @return true if it can be captured
	 */
	public boolean capture(final Callback callback, Location location)
	{
		// Check if the player is in the territory
		boolean canCapture = isInBounds(location.getLatitude(), location.getLongitude());
		if (canCapture)
		{
			DataLoader.getInstance().captureTerritory(this, new DataLoaderAdapter() {

				@Override
				public void territoryCaptured(SpecialTerritory territory, Status status)
				{
					if (status == Status.OK)
					{
						SpecialTerritory.this.revenue = territory.revenue;
						SpecialTerritory.this.timeOwned = territory.timeOwned;
						SpecialTerritory.this.owner = territory.owner;
						SpecialTerritory.this.owner.addTerritory(SpecialTerritory.this);
						SpecialTerritory.this.updateType();
						AccountController.getInstance().updateProfile();
						callback.update();
					}
				}
			});
		}
		return canCapture;
	}
}
