/*=====================================================================*
| This file declares the following classes:
|    DataLoader.java
|
| Description of the class DataLoader.java :
| This class contains all methods for use api services.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SkillType;
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;
import ch.hearc.corporations.model.Trip;

import com.google.android.gms.maps.model.LatLng;

public class DataLoader
{

	private static DataLoader	instance	= null;

	public static DataLoader getInstance()
	{
		if (instance == null) instance = new DataLoader();
		return instance;
	}

	/**
	 * Fetch the leaderboad
	 * 
	 * @param from
	 *            first player rank
	 * @param to
	 *            last player rank
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void getLeaderboard(int from, int to, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_WHAT, DataLoaderUtil.RequestParameters.Leaderboard.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Leaderboard.KEY_START, Integer.toString(from));
		parameters.put(DataLoaderUtil.RequestParameters.Leaderboard.KEY_LIMIT, Integer.toString(to - from));
		request(parameters, ApiRequestType.leaderboard, dataLoaderListener);
	}

	/**
	 * Fetch the player profile
	 * 
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void getProfile(DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Profile.KEY_WHAT, DataLoaderUtil.RequestParameters.Profile.WHAT);
		request(parameters, ApiRequestType.profile, dataLoaderListener);
	}

	/**
	 * Fetch the trips
	 * 
	 * @param number
	 *            quantity of trips
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void getTrips(int number, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Trips.KEY_WHAT, DataLoaderUtil.RequestParameters.Trips.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Trips.KEY_LIMIT, Integer.toString(number));
		request(parameters, ApiRequestType.trips, dataLoaderListener);
	}

	/**
	 * Fetch the territories from server for passed location
	 * 
	 * @param coordinate
	 *            location that you want territory
	 * @param number
	 *            quantity of territories
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void getTerritoriesForLocation(LatLng coordinate, int number, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_WHAT, DataLoaderUtil.RequestParameters.Territories.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LATITUDE, String.format("%1$.4f", coordinate.latitude));
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LONGITUDE, String.format("%1$.4f", coordinate.longitude));
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LIMIT, "1000");
		request(parameters, ApiRequestType.territoriesFetching, dataLoaderListener);
	}

	/**
	 * Purchase territory on server
	 * 
	 * @param territory
	 *            the territory to purchase
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void purchaseTerritory(PurchasableTerritory territory, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_WHAT, DataLoaderUtil.RequestParameters.PurchaseTerritory.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_LATITUDE, String.format("%1$.4f", territory.getLatitude()));
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_LONGITUDE, String.format("%1$.4f", territory.getLongitude()));
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_OWNER, (territory.getOwner() != null ? territory.getOwner().getUserId() : "-1"));
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_PRICE, Long.toString(territory.getSalePrice()));
		request(parameters, ApiRequestType.territoryPurchasing, dataLoaderListener);
	}

	/**
	 * Capture territory on sever
	 * 
	 * @param territory
	 *            the territory to capture
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void captureTerritory(Territory territory, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_WHAT, DataLoaderUtil.RequestParameters.CaptureTerritory.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_LATITUDE, String.format("%1$.4f", territory.getLatitude()));
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_LONGITUDE, String.format("%1$.4f", territory.getLongitude()));
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_OWNER, territory.getOwner().getUserId());
		request(parameters, ApiRequestType.territoryCapturing, dataLoaderListener);
	}

	/**
	 * Login to the server
	 * 
	 * @param facebookToken
	 *            the Facebook Access token
	 * @param home
	 *            the home location
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void loginToServer(String facebookToken, LatLng home, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Connection.KEY_WHAT, DataLoaderUtil.RequestParameters.Connection.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Connection.KEY_USER_ID, AccountController.getInstance().getFacebookID());
		parameters.put(DataLoaderUtil.RequestParameters.Connection.KEY_TOKEN, facebookToken);
		parameters.put(DataLoaderUtil.RequestParameters.Connection.KEY_HOME_LATITUDE, String.format("%1$.4f", home.latitude));
		parameters.put(DataLoaderUtil.RequestParameters.Connection.KEY_HOME_LONGITUDE, String.format("%1$.4f", home.longitude));
		request(parameters, ApiRequestType.connection, dataLoaderListener);
	}

	/**
	 * Update alliance between the current player and the player passed in
	 * parameter
	 * 
	 * @param player
	 *            the player that we want to ally with
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void updateAlliance(Player player, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_WHAT, DataLoaderUtil.RequestParameters.UpdateAlliance.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_ALLY, player.getUserId());
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_CREATE_OR_DELETE, player.isAlly() ? "0" : "1");
		request(parameters, ApiRequestType.updateAlliance, dataLoaderListener);
	}

	/**
	 * Upload trip to the server
	 * 
	 * @param trip
	 *            the trip to upload
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void uploadTrip(Trip trip, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_WHAT, DataLoaderUtil.RequestParameters.UploadTrip.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_DATE, new java.sql.Date(trip.getDate().getTime()).toString());
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_TIME, Long.toString(trip.getTime()));
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_DISTANCE, Float.toString(trip.getDistance()));
		request(parameters, ApiRequestType.uploadTrip, dataLoaderListener);
	}

	/**
	 * Update profile on server
	 * 
	 * @param profile
	 *            the profile updated
	 * @param experiencePoints
	 *            new experiences points
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void updateProfile(Profile profile, int experiencePoints, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_WHAT, DataLoaderUtil.RequestParameters.UpdateProfile.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_POINTS_PRICE, Integer.toString(experiencePoints));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_PURCHASE_PRICE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.purchasePrice).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_PURCHASE_DISTANCE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.purchaseDistance).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_LIMIT_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.experienceLimit).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_MONEY_LIMIT_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.moneyLimit).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_QUANTITY_FOUND_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.experienceQuantityFound).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_ALLIANCE_PRICE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.alliancePrice).getLevel()));
		request(parameters, ApiRequestType.updateProfile, dataLoaderListener);
	}

	/**
	 * Change the price of the territory on the server
	 * 
	 * @param territory
	 *            territory that we want to change price
	 * @param newPrice
	 *            the new price that we want
	 * @param dataLoaderListener
	 *            callback listener
	 */
	public void changeTerritoryPrice(PurchasableTerritory territory, long newPrice, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.ChangePrice.KEY_WHAT, DataLoaderUtil.RequestParameters.ChangePrice.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.ChangePrice.KEY_NEW_PRICE, Long.toString(newPrice));
		parameters.put(DataLoaderUtil.RequestParameters.ChangePrice.KEY_LATITUDE, String.format("%1$.4f", territory.getLatitude()));
		parameters.put(DataLoaderUtil.RequestParameters.ChangePrice.KEY_LONGITUDE, String.format("%1$.4f", territory.getLongitude()));
		request(parameters, ApiRequestType.changePrice, dataLoaderListener);
	}

	private DataLoader()
	{
	}

	/**
	 * Create and execute the query to the API
	 * 
	 * @param arguments
	 *            the arguments for the query
	 * @param apiRequestType
	 *            the type of query
	 * @param listener
	 *            callback listener
	 */
	private void request(Map<String, String> arguments, final ApiRequestType apiRequestType, final DataLoaderListener listener)
	{
		arguments.put(DataLoaderUtil.RequestParameters.All.KEY_IDENTIFIER, AccountController.getInstance().getIdentifier());
		StringBuilder url = new StringBuilder(CorporationsConfiguration.API_PATH);
		url.append("?");
		for (Entry<String, String> entry : arguments.entrySet())
		{
			url.append(entry.getKey());
			url.append("=");
			url.append(entry.getValue());
			url.append("&");
		}
		url.deleteCharAt(url.length() - 1);

		new AsyncTask<String, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(String... params)
			{
				return getJson(params[0]);
			}

			protected void onPostExecute(JSONObject result)
			{
				requestFinished(result, apiRequestType, listener);
			}

		}.execute(url.toString());
	}

	/**
	 * Evaluate the response of the server in JSON and call the callback with
	 * correct info
	 * 
	 * @param result
	 *            the result in JSON receive from the server
	 * @param apiRequestType
	 *            type of request
	 * @param listener
	 *            the callback
	 */
	protected void requestFinished(JSONObject result, ApiRequestType apiRequestType, DataLoaderListener listener)
	{
		Status status = null;
		try
		{
			status = Status.valueOf(result.getString(DataLoaderUtil.ResultKeys.STATUS));
		}
		catch (JSONException e)
		{
			Log.e(TAG, e.getMessage());
		}
		catch (NullPointerException e)
		{
			Toast.makeText(Corporations.getAppContext(), Corporations.getAppContext().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
			return;
		}
		switch (apiRequestType)
		{
			case leaderboard:
				try
				{
					List<Player> players = new ArrayList<Player>();
					JSONArray jsonArray = result.getJSONArray(DataLoaderUtil.ResultKeys.RESULTS);
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						int rank = Integer.parseInt(jsonObject.getString(DataLoaderUtil.ResultKeys.Leaderboard.RANK));
						String userId = jsonObject.getString(DataLoaderUtil.ResultKeys.Leaderboard.USER_ID);
						int numberTerritories = Integer.parseInt(jsonObject.getString(DataLoaderUtil.ResultKeys.Leaderboard.NUMBER_OF_TERRITORIES));
						Boolean ally = jsonObject.getString(DataLoaderUtil.ResultKeys.Leaderboard.ALLY).equals("1");
						Player player = PlayersManager.getInstance().createOrUpdatePlayerForUserID(userId, rank, numberTerritories, ally);
						players.add(player);
					}
					listener.leaderboardFetched(players, status);
				}
				catch (JSONException e)
				{
					Log.e(TAG, e.getMessage());
				}
				break;
			case profile:
				updateProfile(result);
				listener.profileFetched(null);
				break;
			case territoriesFetching:
				try
				{
					List<Territory> territories = new ArrayList<Territory>();
					JSONArray jsonArray = result.getJSONArray(DataLoaderUtil.ResultKeys.RESULTS);
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String ownerUserId = jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.OWNER);
						Boolean ally = jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.ALLY).equals("1");
						Boolean special = jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.SPECIAL).equals("1");
						long timeOwned = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.OWNED_TIME));
						int revenue = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.REVENUE));
						Player player = PlayersManager.getInstance().createOrGetPlayerForUserID(ownerUserId, ally);
						double latitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.LATITUDE));
						double longitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.LONGITUDE));
						Territory territory;
						if (special)
							territory = new SpecialTerritory(latitude, longitude, player, timeOwned, revenue);
						else
						{
							long salePrice = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.SALE_PRICE));
							long purchasingPrice = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Territories.PURCHASE_PRICE));
							territory = new PurchasableTerritory(latitude, longitude, player, timeOwned, salePrice, purchasingPrice, revenue);
						}
						territories.add(territory);
					}
					listener.territoriesFetched(territories, status);
				}
				catch (JSONException e)
				{
					Log.e(TAG, e.getMessage());
				}
				break;
			case trips:
				try
				{
					List<Trip> trips = new ArrayList<Trip>();
					JSONArray jsonArray = result.getJSONArray(DataLoaderUtil.ResultKeys.RESULTS);
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						float distance = Float.parseFloat(jsonObject.getString(DataLoaderUtil.ResultKeys.Trips.DISTANCE));
						long secondes = Long.parseLong(jsonObject.getString(DataLoaderUtil.ResultKeys.Trips.TIME));
						long money = Long.parseLong(jsonObject.getString(DataLoaderUtil.ResultKeys.Trips.MONEY));
						int experience = Integer.parseInt(jsonObject.getString(DataLoaderUtil.ResultKeys.Trips.EXPERIENCE));

						Date date = java.sql.Date.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Trips.DATE).split(" ")[0]);
						Trip trip = new Trip(distance, secondes, money, experience, date);
						trips.add(trip);
					}
					listener.tripsFetched(trips, status);
				}
				catch (JSONException e)
				{
					Log.e(TAG, e.getMessage());
				}
				break;
			case territoryPurchasing:
				try
				{
					JSONObject jsonObject = result.getJSONObject(DataLoaderUtil.ResultKeys.RESULTS);
					String ownerUserId = jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.OWNER);
					Boolean ally = jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.ALLY).equals("1");
					long timeOwned = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.OWNED_TIME));
					Player player = PlayersManager.getInstance().createOrGetPlayerForUserID(ownerUserId, ally);
					double latitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.LATITUDE));
					double longitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.LONGITUDE));
					int revenue = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.REVENUE));
					long salePrice = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.SALE_PRICE));
					long purchasingPrice = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.PurchaseTerritory.PURCHASE_PRICE));
					PurchasableTerritory territory = new PurchasableTerritory(latitude, longitude, player, timeOwned, salePrice, purchasingPrice, revenue);
					listener.territoryPurchased(territory, status);
				}
				catch (JSONException e)
				{
					Log.e(TAG, e.getMessage());
				}
				break;
			case territoryCapturing:
				try
				{
					JSONObject jsonObject = result.getJSONObject(DataLoaderUtil.ResultKeys.RESULTS);
					String ownerUserId = jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.OWNER);
					Boolean ally = jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.ALLY).equals("1");
					long timeOwned = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.OWNED_TIME));
					Player player = PlayersManager.getInstance().createOrGetPlayerForUserID(ownerUserId, ally);
					double latitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.LATITUDE));
					double longitude = Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.LONGITUDE));
					int revenue = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.CaptureTerritory.REVENUE));
					SpecialTerritory territory = new SpecialTerritory(latitude, longitude, player, timeOwned, revenue);
					listener.territoryCaptured(territory, status);
				}
				catch (JSONException e)
				{
					Log.e(TAG, e.getMessage());
				}
				break;
			case connection:
				updateProfile(result);
				listener.connectionFinished(status);
				break;
			case updateAlliance:
				listener.allianceUpdated(status);
				break;
			case updateProfile:
				updateProfile(result);
				listener.profileUpdated(status);
				break;
			case uploadTrip:
				listener.tripUploaded(status);
				break;
			case changePrice:
				listener.priceChanged(status);
				break;

			default:
				break;
		}
	}

	/**
	 * Update the profile from the info get in the json
	 * 
	 * @param jsonObject
	 *            the json
	 */
	private void updateProfile(JSONObject jsonObject)
	{
		Profile profile = AccountController.getInstance().getProfile();
		try
		{
			jsonObject = jsonObject.getJSONObject(DataLoaderUtil.ResultKeys.RESULTS);
			String userId = jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.USER_ID);
			int numberAllies = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.NUMBER_OF_ALLIANCE));
			int numberTerritories = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.NUMBER_OF_TERRITORIES));
			int rank = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.RANK));
			long currentMoney = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.CURRENT_MONEY));
			long currentRevenue = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.CURRENT_REVENUE));
			long tripMoneyEarned = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.TRIP_MONEY_EARNED));
			long totalGain = Long.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.TOTAL_GAIN));
			int experiencePoints = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.EXPERIENCE_POINTS));
			LatLng home = new LatLng(Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.HOME_LAT)), Double.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.HOME_LNG)));
			int purchasePriceSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.PPL));
			int purchaseDistanceSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.PDL));
			int experienceLimitSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.ELL));
			int moneyLimitSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.MLL));
			int experienceQuantityFoundSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.EQFL));
			int alliancePriceSkillLevel = Integer.valueOf(jsonObject.getString(DataLoaderUtil.ResultKeys.Profile.APL));

			profile.setUserId(userId);
			profile.setNumberAllies(numberAllies);
			profile.setNumberTerritories(numberTerritories);
			profile.setRank(rank);
			profile.setCurrentMoney(currentMoney);
			profile.setCurrentRevenue(currentRevenue);
			profile.setTripMoneyEarned(tripMoneyEarned);
			profile.setTotalGain(totalGain);
			profile.setExperiencePoints(experiencePoints);
			profile.setHome(home);
			profile.setSkillLevel(SkillType.purchasePrice, purchasePriceSkillLevel);
			profile.setSkillLevel(SkillType.purchaseDistance, purchaseDistanceSkillLevel);
			profile.setSkillLevel(SkillType.experienceLimit, experienceLimitSkillLevel);
			profile.setSkillLevel(SkillType.moneyLimit, moneyLimitSkillLevel);
			profile.setSkillLevel(SkillType.experienceQuantityFound, experienceQuantityFoundSkillLevel);
			profile.setSkillLevel(SkillType.alliancePrice, alliancePriceSkillLevel);
		}
		catch (JSONException e)
		{
			Log.e(TAG, e.toString());
		}
	}

	/**
	 * Based on https://gist.github.com/zvineyard/2012742
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject getJson(String url)
	{
		InputStream is = null;
		String result = "";
		JSONObject jsonObject = null;

		// HTTP
		try
		{
			HttpClient httpclient = new DefaultHttpClient(); // for port 80
																// requests!
			HttpPost httpPost = new HttpPost(url);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}
		catch (Exception e)
		{
			Log.e(DataLoader.class.getSimpleName() + "->getJson", e.toString());
			return null;
		}

		// Read response to string
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
		}
		catch (Exception e)
		{
			Log.e(DataLoader.class.getSimpleName() + "->getJson", e.toString());
			return null;
		}

		// Convert string to object
		try
		{
			jsonObject = new JSONObject(result);
		}
		catch (JSONException e)
		{

			Log.e(DataLoader.class.getSimpleName() + "->getJson", result.toString());
			Log.e(DataLoader.class.getSimpleName() + "->getJson", e.toString());
			return null;
		}

		return jsonObject;
	}

	private static final String	TAG	= DataLoader.class.getSimpleName();
}
