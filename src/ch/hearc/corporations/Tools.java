 /*=====================================================================*
 | This file declares the following classes:
 |    Tools.java
 |
 | Description of the class Tools.java :
 |
 |
 | <p>Copyright : EIAJ, all rights reserved</p>
 | @autor : Alexandre
 | @version : 24 déc. 2013
 |
 *=====================================================================*/

package ch.hearc.corporations;

/**
 * @author Alexandre
 *
 */
public class Tools
{
	public static String formatMoney(long money)
	{
		return "$" +String.format("%,d", money);
	}
}
