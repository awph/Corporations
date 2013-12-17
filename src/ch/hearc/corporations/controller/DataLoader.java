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
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;

import com.google.android.gms.maps.model.LatLng;

public class DataLoader
{

	private static DataLoader	instance	= null;

	public static DataLoader getInstance()
	{
		if (instance == null) instance = new DataLoader();
		return instance;
	}

	public void getTerritoriesForLocation(LatLng coordinate, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("what", "territories");
		parameters.put("lat", String.format("%1$.4f", coordinate.latitude));
		parameters.put("lng", String.format("%1$.4f", coordinate.longitude));
		parameters.put("limit", "1000");
		request(parameters, ApiRequestType.territoriesFetching, dataLoaderListener);
	}

	public void loginToServer(String facebookToken, LatLng home, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("what", "connection");
		parameters.put("user_id", AccountController.getInstance().getFacebookID());
		parameters.put("token", facebookToken);
		parameters.put("lat", String.format("%1$.4f", home.latitude));
		parameters.put("lng", String.format("%1$.4f", home.longitude));
		request(parameters, ApiRequestType.connection, dataLoaderListener);
	}

	public void updateAlliance(Player player, DataLoaderListener dataLoaderListener)
	{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("what", "updateAlliance");
		parameters.put("ally", player.getUserID());
		parameters.put("createOrDelete", player.isAlly() ? "0" : "1");
		request(parameters, ApiRequestType.updateAlliance, dataLoaderListener);
	}

	private DataLoader()
	{
	}

	private void request(Map<String, String> arguments, final ApiRequestType apiRequestType, final DataLoaderListener listener)
	{
		arguments.put("identifier", AccountController.getInstance().getIdentifier());
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
		switch (apiRequestType)
		{
			case leaderboard:

				break;
			case profile:

				break;
			case territoriesFetching:
				try
				{
					List<Territory> territories = new ArrayList<Territory>();
					Log.e("status", result.getString("status"));
					JSONArray jsonArray = result.getJSONArray("results");
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String ownerUserId = jsonObject.getString("o");
						Boolean ally = jsonObject.getString("a").equals("1");
						Boolean special = jsonObject.getString("s").equals("1");
						long timeOwned = Long.valueOf(jsonObject.getString("t"));
						Player player = PlayersManager.getInstance().createOrGetPlayerForUserID(ownerUserId, ally);
						LatLng location = new LatLng(Double.valueOf(jsonObject.getString("la")), Double.valueOf(jsonObject.getString("lo")));
						Territory territory;
						if (special)
							territory = new SpecialTerritory(location, player, timeOwned);
						else
						{
							int salePrice = Integer.valueOf(jsonObject.getString("sp"));
							int purchasingPrice = Integer.valueOf(jsonObject.getString("pp"));
							territory = new PurchasableTerritory(location, player, timeOwned, salePrice, purchasingPrice);
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
				try
				{
					Log.e("status", result.getString("status"));
					JSONObject jsonObject = result.getJSONObject("results");
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
					Profile profile = new Profile(userId, numberAllies, numberTerritories, rank, currentMoney, currentRevenue, totalGain, experiencePoints, home, purchasePriceSkillLevel,
							purchaseDistanceSkillLevel, experienceLimitSkillLevel, moneyLimitSkillLevel, experienceQuantityFoundSkillLevel, alliancePriceSkillLevel);
					listener.connectionFinished(profile);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				break;
			case updateAlliance:
				try
				{
					String status = result.getString("status");
					int int_status = -1; // TODO: better
					if (status.equals("OK"))
						int_status = DataLoaderUtil.ResultKeys.StatusKey.OK;
					else if (status.equals("ALREADY_EXISTS"))
						int_status = DataLoaderUtil.ResultKeys.StatusKey.ALREADY_EXISTS;
					else if (status.equals("DONT_EXISTS")) int_status = DataLoaderUtil.ResultKeys.StatusKey.DONT_EXISTS;

					listener.allianceUpdated(int_status);
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
				break;
			case updateProfile:

				break;
			case uploadTrip:

				break;

			default:
				break;
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
}
