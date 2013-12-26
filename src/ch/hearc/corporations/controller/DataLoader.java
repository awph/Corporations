package ch.hearc.corporations.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import ch.hearc.corporations.CorporationsConfiguration;
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

	public void getLeaderboard(int from, int to, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_WHAT, DataLoaderUtil.RequestParameters.Leaderboard.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Leaderboard.KEY_START, Integer.toString(from)); // TODO:
																										// test
																										// if
																										// need
																										// -1
		parameters.put(DataLoaderUtil.RequestParameters.Leaderboard.KEY_LIMIT, Integer.toString(to - from));
		request(parameters, ApiRequestType.leaderboard, dataLoaderListener);
	}

	public void getProfile(LatLng coordinate, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Profile.KEY_WHAT, DataLoaderUtil.RequestParameters.Profile.WHAT);
		request(parameters, ApiRequestType.profile, dataLoaderListener);
	}

	public void getTrips(int number, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Trips.KEY_WHAT, DataLoaderUtil.RequestParameters.Trips.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Trips.KEY_LIMIT, Integer.toString(number));
		request(parameters, ApiRequestType.trips, dataLoaderListener);
	}

	public void getTerritoriesForLocation(LatLng coordinate, int number, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_WHAT, DataLoaderUtil.RequestParameters.Territories.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LATITUDE, String.format("%1$.4f", coordinate.latitude));
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LONGITUDE, String.format("%1$.4f", coordinate.longitude));
		parameters.put(DataLoaderUtil.RequestParameters.Territories.KEY_LIMIT, "1000");
		request(parameters, ApiRequestType.territoriesFetching, dataLoaderListener);
	}

	public void purchaseTerritory(PurchasableTerritory territory, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_WHAT, DataLoaderUtil.RequestParameters.PurchaseTerritory.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_LATITUDE, String.format("%1$.4f", territory.getLatitude()));
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_LONGITUDE, String.format("%1$.4f", territory.getLongitude()));
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_OWNER, territory.getOwner().getUserId());
		parameters.put(DataLoaderUtil.RequestParameters.PurchaseTerritory.KEY_PRICE, Integer.toString(territory.getSalePrice()));
		request(parameters, ApiRequestType.territoryPurchasing, dataLoaderListener);
	}

	public void captureTerritory(Territory territory, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_WHAT, DataLoaderUtil.RequestParameters.CaptureTerritory.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_LATITUDE, String.format("%1$.4f", territory.getLatitude()));
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_LONGITUDE, String.format("%1$.4f", territory.getLongitude()));
		parameters.put(DataLoaderUtil.RequestParameters.CaptureTerritory.KEY_OWNER, territory.getOwner().getUserId());
		request(parameters, ApiRequestType.territoryCapturing, dataLoaderListener);
	}

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

	public void updateAlliance(Player player, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_WHAT, DataLoaderUtil.RequestParameters.UpdateAlliance.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_ALLY, player.getUserId());
		parameters.put(DataLoaderUtil.RequestParameters.UpdateAlliance.KEY_CREATE_OR_DELETE, player.isAlly() ? "0" : "1");
		request(parameters, ApiRequestType.updateAlliance, dataLoaderListener);
	}

	public void uploadTrip(Trip trip, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_WHAT, DataLoaderUtil.RequestParameters.UploadTrip.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_DATE, Long.toString(trip.getDate().getTime()));
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_TIME, Integer.toString(trip.getTime()));
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_DISTANCE, Float.toString(trip.getDistance()));
		parameters.put(DataLoaderUtil.RequestParameters.UploadTrip.KEY_MONEY_EARNED, Integer.toString(trip.getMoneyEarned()));
		request(parameters, ApiRequestType.uploadTrip, dataLoaderListener);
	}

	public void updateProfile(Profile profile, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_WHAT, DataLoaderUtil.RequestParameters.UpdateProfile.WHAT);
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_CURRENT_MONEY, Long.toString(profile.getCurrentMoney()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_TOTAL_GAIN, Long.toString(profile.getTotalGain()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_POINTS, Integer.toString(profile.getExperiencePoints()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_PURCHASE_PRICE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.purchasePrice).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_PURCHASE_DISTANCE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.purchaseDistance).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_LIMIT_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.experienceLimit).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_MONEY_LIMIT_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.moneyLimit).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_EXPERIENCE_QUANTITY_FOUND_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.experienceQuantityFound).getLevel()));
		parameters.put(DataLoaderUtil.RequestParameters.UpdateProfile.KEY_ALLIANCE_PRICE_SKILL_LEVEL, Integer.toString(profile.getSkill(SkillType.alliancePrice).getLevel()));
		request(parameters, ApiRequestType.updateProfile, dataLoaderListener);
	}

	private DataLoader()
	{
	}

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
		Log.e("DataLoader: request", url.toString());

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

	protected void requestFinished(JSONObject result, ApiRequestType apiRequestType, DataLoaderListener listener)
	{
		String status = null;
		try
		{
			status = result.getString(DataLoaderUtil.ResultKeys.STATUS);
			Log.e("status", result.getString(DataLoaderUtil.ResultKeys.STATUS));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		switch (apiRequestType)
		{
			case leaderboard:
				try
				{
					List<Player> players = new ArrayList<Player>();
					JSONArray jsonArray = result.getJSONArray("results");
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
					listener.leaderboardFetched(players);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
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
					JSONArray jsonArray = result.getJSONArray("results");
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String ownerUserId = jsonObject.getString("o");
						Boolean ally = jsonObject.getString("a").equals("1");
						Boolean special = jsonObject.getString("s").equals("1");
						long timeOwned = Long.valueOf(jsonObject.getString("t"));
						Player player = PlayersManager.getInstance().createOrGetPlayerForUserID(ownerUserId, ally);
						double latitude = Double.valueOf(jsonObject.getString("la"));
						double longitude = Double.valueOf(jsonObject.getString("lo"));
						Territory territory;
						if (special)
							territory = new SpecialTerritory(latitude, longitude, player, timeOwned);
						else
						{
							int salePrice = Integer.valueOf(jsonObject.getString("sp"));
							int purchasingPrice = Integer.valueOf(jsonObject.getString("pp"));
							territory = new PurchasableTerritory(latitude, longitude, player, timeOwned, salePrice, purchasingPrice);
						}
						territories.add(territory);
					}
					listener.territoriesFetched(territories);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				break;
			case trips:

				break;
			case territoryPurchasing:

				break;
			case territoryCapturing:

				break;
			case connection:
				updateProfile(result);
				listener.connectionFinished(null);
				break;
			case updateAlliance:
				int int_status = -1; // TODO: better
				if (status.equals("OK"))
					int_status = DataLoaderUtil.ResultKeys.StatusKey.OK;
				else if (status.equals("ALREADY_EXISTS"))
					int_status = DataLoaderUtil.ResultKeys.StatusKey.ALREADY_EXISTS;
				else if (status.equals("DONT_EXISTS")) int_status = DataLoaderUtil.ResultKeys.StatusKey.DONT_EXISTS;

				listener.allianceUpdated(int_status);
				break;
			case updateProfile:
				updateProfile(result);
				listener.profileUpdated(null);
				break;
			case uploadTrip:

				break;

			default:
				break;
		}
	}

	private void updateProfile(JSONObject jsonObject)
	{
		Profile profile = AccountController.getInstance().getProfile();
		try
		{
			jsonObject = jsonObject.getJSONObject("results");
			String userId = jsonObject.getString("id");
			int numberAllies = Integer.valueOf(jsonObject.getString("na"));
			int numberTerritories = Integer.valueOf(jsonObject.getString("nt"));
			int rank = Integer.valueOf(jsonObject.getString("r"));
			int currentMoney = Integer.valueOf(jsonObject.getString("cm"));
			int currentRevenue = Integer.valueOf(jsonObject.getString("cr"));
			int totalGain = Integer.valueOf(jsonObject.getString("tg"));
			int experiencePoints = Integer.valueOf(jsonObject.getString("ep"));
			LatLng home = new LatLng(Double.valueOf(jsonObject.getString("hlat")), Double.valueOf(jsonObject.getString("hlng")));
			int purchasePriceSkillLevel = Integer.valueOf(jsonObject.getString("ppl"));
			int purchaseDistanceSkillLevel = Integer.valueOf(jsonObject.getString("pdl"));
			int experienceLimitSkillLevel = Integer.valueOf(jsonObject.getString("ell"));
			int moneyLimitSkillLevel = Integer.valueOf(jsonObject.getString("mll"));
			int experienceQuantityFoundSkillLevel = Integer.valueOf(jsonObject.getString("eqfl"));
			int alliancePriceSkillLevel = Integer.valueOf(jsonObject.getString("apl"));

			profile.setUserId(userId);
			profile.setNumberAllies(numberAllies);
			profile.setNumberTerritories(numberTerritories);
			profile.setRank(rank);
			profile.setCurrentMoney(currentMoney);
			profile.setCurrentRevenue(currentRevenue);
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
			Log.e("DataLoader: result", result);
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
