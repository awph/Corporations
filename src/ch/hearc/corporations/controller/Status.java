/*=====================================================================*
| This file declares the following enum:
|    Status.java
|
| Description of the emum Status.java :
| This enum contains the different possible status response of the api.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

public enum Status
{
	OK,					// 0
	UNKNOW_IDENTIFIER,	// 1
	UNKNOW_REQUEST,		// 2
	INVALID_PARAMETER,	// 3
	UNKNOWN_ERROR,		// 4
	OWNER_CHANGE,		// 5
	NOT_ENOUGTH_MONEY,	// 6
	PRICE_CHANGE,		// 7
	ALREADY_EXISTS,		// 8
	DONT_EXISTS;		// 9
}
