/*=====================================================================*
| This file declares the following classes:
|    SpecialTerritory.java
|
| Description of the class SpecialTerritory.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Alexandre
 * 
 */
public class SpecialTerritory extends Territory
{

	public SpecialTerritory(double latitude, double longitude, Player owner, long timeOwned)
	{
		super(latitude, longitude, owner, timeOwned);
	}

	public void capture()
	{

	}
}
