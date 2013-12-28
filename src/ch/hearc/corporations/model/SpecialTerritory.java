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


/**
 * @author Alexandre
 * 
 */
public class SpecialTerritory extends Territory
{
	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public SpecialTerritory(double latitude, double longitude, Player owner, long timeOwned)
	{
		super(latitude, longitude, owner, timeOwned);
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public void capture()
	{

	}
}
