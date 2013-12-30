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
	public static final class RequestParameters
	{
		public static class All
		{
			public static final String	KEY_WHAT		= "what";
			public static final String	KEY_IDENTIFIER	= "identifier";
		}

		public static final class Leaderboard extends All
		{
			public static final String	WHAT		= "leaderboard";
			public static final String	KEY_START	= "start";
			public static final String	KEY_LIMIT	= "limit";
		}

		public static class Profile extends All
		{
			public static final String	WHAT	= "profile";
		}

		public static final class Trips extends All
		{
			public static final String	WHAT		= "trips";
			public static final String	KEY_LIMIT	= "limit";
		}

		public static final class Territories extends All
		{
			public static final String	WHAT			= "territories";
			public static final String	KEY_LATITUDE	= "lat";
			public static final String	KEY_LONGITUDE	= "lng";
			public static final String	KEY_LIMIT		= "limit";
		}

		public static class PurchaseTerritory extends All
		{
			public static final String	WHAT			= "purchaseTerritory";
			public static final String	KEY_LATITUDE	= "lat";
			public static final String	KEY_LONGITUDE	= "lng";
			public static final String	KEY_OWNER		= "owner";
			public static final String	KEY_PRICE		= "price";
		}

		public static final class CaptureTerritory extends All
		{
			public static final String	WHAT			= "captureTerritory";
			public static final String	KEY_LATITUDE	= "lat";
			public static final String	KEY_LONGITUDE	= "lng";
			public static final String	KEY_OWNER		= "owner";
		}

		public static final class Connection extends All
		{
			public static final String	WHAT				= "connection";
			public static final String	KEY_USER_ID			= "user_id";
			public static final String	KEY_TOKEN			= "token";
			public static final String	KEY_HOME_LATITUDE	= "lat";
			public static final String	KEY_HOME_LONGITUDE	= "lng";
		}

		public static final class UpdateProfile extends All
		{
			public static final String	WHAT										= "updateProfile";
			public static final String	KEY_CURRENT_MONEY							= "currentMoney";
			public static final String	KEY_TOTAL_GAIN								= "totalGain";
			public static final String	KEY_EXPERIENCE_POINTS						= "experiencePoints";
			public static final String	KEY_PURCHASE_PRICE_SKILL_LEVEL				= "purchasePriceSkillLevel";
			public static final String	KEY_PURCHASE_DISTANCE_SKILL_LEVEL			= "purchaseDistanceSkillLevel";
			public static final String	KEY_EXPERIENCE_LIMIT_SKILL_LEVEL			= "experienceLimitSkillLevel";
			public static final String	KEY_MONEY_LIMIT_SKILL_LEVEL					= "moneyLimitSkillLevel";
			public static final String	KEY_EXPERIENCE_QUANTITY_FOUND_SKILL_LEVEL	= "experienceQuantityFoundSkillLevel";
			public static final String	KEY_ALLIANCE_PRICE_SKILL_LEVEL				= "alliancePriceSkillLevel";
		}

		public static final class UpdateAlliance extends All
		{
			public static final String	WHAT					= "updateAlliance";
			public static final String	KEY_ALLY				= "ally";
			public static final String	KEY_CREATE_OR_DELETE	= "createOrDelete";
		}

		public static final class UploadTrip extends All
		{
			public static final String	WHAT				= "uploadTrip";
			public static final String	KEY_DISTANCE		= "distance";
			public static final String	KEY_TIME			= "secondes";
			public static final String	KEY_MONEY_EARNED	= "moneyEarned";
			public static final String	KEY_DATE			= "date";
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

	public static final class ResultKeys
	{
		public static final class Leaderboard
		{
			public static final String	USER_ID					= "id";
			public static final String	ALLY					= "a";
			public static final String	NUMBER_OF_TERRITORIES	= "nt";
			public static final String	RANK					= "r";
		}

		public static class Profile
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
			public static final String	EQFL					= "eqfl";
			public static final String	APL						= "apl";
		}

		public static final class Trips
		{
			public static final String	DATE		= "da";
			public static final String	DISTANCE	= "d";
			public static final String	TIME		= "t";
			public static final String	MONEY		= "m";
		}

		public static class Territories
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

		public static final class PurchaseTerritory extends Territories
		{
		}

		public static final class CaptureTerritory extends Territories
		{
		}

		public static final class Connection extends Profile
		{
		}

		public static final class UpdateProfile extends Profile
		{
		}

		public static final class UpdateAlliance
		{
		}

		public static final class UploadTrip
		{
		}

		public static final String	STATUS	= "status";
		public static final String	RESULTS	= "results";

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
