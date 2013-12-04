package ch.hearc.corporations.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
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
import ch.hearc.corporations.ApiRequestType;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.model.Profile;

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
		AccountController accountController = AccountController.getInstance();
		parameters.put("what", "connexion");
		parameters.put("identifier", accountController.getIdentifier());
		parameters.put("user_id", accountController.getFacebookID());
		parameters.put("token", facebookToken);
		parameters.put("lat", String.format("%1$.4f", home.latitude));
		parameters.put("lng", String.format("%1$.4f", home.longitude));
		request(parameters, ApiRequestType.territoriesFetching, dataLoaderListener);
	}

	private DataLoader()
	{

	}

	private void request(Map<String, String> arguments, final ApiRequestType apiRequestType, final DataLoaderListener listener)
	{
		arguments.put("identifier", "425ffb07ca9578dae37832fc167c79629f5cc2b1");// AccountController.getInstance().getIdentifier());
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
					Log.e("status", result.getString("status"));
					JSONArray jsonArray = result.getJSONArray("results");
					Log.e("results", jsonArray.toString());
					for (int i = 0; i < jsonArray.length(); ++i)
					{
						Log.e("results" + i, jsonArray.getJSONObject(i).toString());
					}

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
			case connexion:
				try
				{
					Log.e("status", result.getString("status"));
					JSONObject jsonObject = result.getJSONObject("results");
					String userId = jsonObject.getString("id");
					int numberAllies = jsonObject.getInt("na");
					int numberTerritories = jsonObject.getInt("nt");
					int rank = jsonObject.getInt("r");
					int currentMoney = jsonObject.getInt("cm");
					int currentRevenue = jsonObject.getInt("cr");
					int totalGain = jsonObject.getInt("tg");
					int experiencePoints = jsonObject.getInt("ep");
					LatLng home = new LatLng(jsonObject.getDouble("hlat"), jsonObject.getDouble("hlng"));
					int purchasePriceSkillLevel = jsonObject.getInt("ppl");
					int purchaseDistanceSkillLevel = jsonObject.getInt("pdl");
					int experienceLimitSkillLevel = jsonObject.getInt("ell");
					int moneyLimitSkillLevel = jsonObject.getInt("mll");
					int experienceQuantityFoundSkillLevel = jsonObject.getInt("eqfl");
					int alliancePriceSkillLevel = jsonObject.getInt("apl");
					Profile profile = new Profile(userId, numberAllies, numberTerritories, rank, currentMoney, currentRevenue, totalGain, experiencePoints, home, purchasePriceSkillLevel,
							purchaseDistanceSkillLevel, experienceLimitSkillLevel, moneyLimitSkillLevel, experienceQuantityFoundSkillLevel, alliancePriceSkillLevel);
					listener.connexionFinished(profile);
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
			return null;
		}

		// Convert string to object
		try
		{
			jsonObject = new JSONObject(result);
		}
		catch (JSONException e)
		{
			return null;
		}

		return jsonObject;
	}
}
