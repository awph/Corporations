/*=====================================================================*
| This file declares the following classes:
|    DataLoaderUtil.java
|
| Description of the class DataLoaderUtil.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 17 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

/**
 * @author Alexandre
 * 
 */
public final class DataLoaderUtil
{
	public static final class ResultKeys
	{
		public static final class leaderboard
		{
			public static final String	USER_ID					= "id";
			public static final String	ALLY					= "a";
			public static final String	NUMBER_OF_TERRITORIES	= "nt";
			public static final String	RANK					= "r";
		}

		public static final class profile
		{
			public static final String	USER_ID					= "id";
			public static final String	NUMBER_OF_ALLIANCE		= "na";
			public static final String	NUMBER_OF_TERRITORIES	= "nt";
			public static final String	RANK					= "r";
			public static final String	CURRENT_MONEY			= "cm";
			public static final String	CURRENT_REVENUE			= "cr";
			public static final String	TOTAL_GAIN				= "tg";
			public static final String	EXPERIENCE_POINTS		= "ep";
			public static final String	HOME_LAT				= "hlat";
			public static final String	HOME_LNG				= "hlng";
			public static final String	PPL						= "ppl";
			public static final String	PDL						= "pdl";
			public static final String	ELL						= "ell";
			public static final String	MLL						= "mll";
			public static final String	EGFL					= "egfl";
			public static final String	APL						= "apl";
		}

		public static final class trips
		{
			public static final String	DATE		= "da";
			public static final String	DISTANCE	= "d";
			public static final String	TIME		= "t";
			public static final String	MONEY		= "m";
		}

		public static final class territories
		{
			public static final String	OWNER			= "o";
			public static final String	ALLY			= "a";
			public static final String	REVENUE			= "r";
			public static final String	SPECIAL			= "s";
			public static final String	PURCHASE_PRICE	= "pp";
			public static final String	SALE_PRICE		= "sp";
			public static final String	OWNED_TIME		= "t";
			public static final String	LATITUDE		= "la";
			public static final String	LONGITUDE		= "lo";
		}

		public static final class purchaseTerritory
		{
			public static final String	OWNER		= "o";
			public static final String	ALLY		= "a";
			public static final String	REVENUE		= "r";
			public static final String	LATITUDE	= "la";
			public static final String	LONGITUDE	= "lo";
		}

		public static final class captureTerritory
		{
			public static final String	OWNER		= "o";
			public static final String	ALLY		= "a";
			public static final String	REVENUE		= "r";
			public static final String	LATITUDE	= "la";
			public static final String	LONGITUDE	= "lo";
		}

		public static final class connection
		{
		}

		public static final class updateProfile
		{
		}

		public static final class updateAlliance
		{
		}

		public static final class uploadTrip
		{
		}

		public static final class StatusKey
		{
			public static final int	OK					= 0;
			public static final int	UNKNOW_IDENTIFIER	= 1;
			public static final int	UNKNOW_REQUEST		= 2;
			public static final int	INVALID_PARAMETER	= 3;
			public static final int	UNKNOWN_ERROR		= 4;
			public static final int	OWNER_CHANGE		= 5;
			public static final int	NOT_ENOUGTH_MONEY	= 6;
			public static final int	PRICE_CHANGE		= 7;
			public static final int	ALREADY_EXISTS		= 8;
			public static final int	DONT_EXISTS			= 9;
		}
	}
}
