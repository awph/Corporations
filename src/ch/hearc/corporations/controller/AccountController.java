package ch.hearc.corporations.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.security.Encrypt;

import com.google.android.gms.maps.model.LatLng;

public class AccountController
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private String						facebookID;
	private String						deviceID;
	private String						identifier;
	private LatLng						home;
	private Profile						profile;
	private ProfileInfoDisplayer		profileInfoDisplayer;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static AccountController	instance				= null;

	private static final String			PREFERENCES_FILENAME	= "CorporationsPref";
	private static final String			FACEBOOK_ID_KEY			= "kFacebookId";
	private static final String			HOME_LAT_KEY			= "kLatHome";
	private static final String			HOME_LNG_KEY			= "kLngHome";

	/*------------------------------*\
	|*			  Interface			*|
	\*------------------------------*/

	public interface ProfileInfoDisplayer
	{
		void updateProfileInfo();
	}

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	private AccountController()
	{
		profile = null;
		// http://android-developers.blogspot.in/2011/03/identifying-app-installations.html
		deviceID = Secure.getString(Corporations.getAppContext().getContentResolver(), Secure.ANDROID_ID);
		restoreFacebookID();
		generateIdentifier();
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public static AccountController getInstance()
	{
		if (instance == null) instance = new AccountController();
		return instance;
	}

	public boolean checkFacebookID(String facebookID)
	{
		return this.facebookID.equals(facebookID);
	}

	public void updateProfile()
	{
		DataLoader.getInstance().getProfile(new DataLoaderAdapter() {

			@Override
			public void profileFetched(Status status)
			{
				profileInfoDisplayer.updateProfileInfo();
			}
		});
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	private void restoreFacebookID()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		facebookID = preferences.getString(FACEBOOK_ID_KEY, null);
		String homeLat = preferences.getString(HOME_LAT_KEY, null);
		String homeLng = preferences.getString(HOME_LNG_KEY, null);
		if (homeLat != null && homeLng != null)
			home = new LatLng(Double.parseDouble(homeLat), Double.parseDouble(homeLng));
		else
			home = null;
	}

	private void saveAccount()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(FACEBOOK_ID_KEY, facebookID);
		if (home != null)
		{
			editor.putString(HOME_LAT_KEY, Double.toString(home.latitude));
			editor.putString(HOME_LNG_KEY, Double.toString(home.longitude));
		}
		editor.commit();
	}

	private void generateIdentifier()
	{
		identifier = null;
		if (facebookID != null && deviceID != null)
		{
			try
			{
				identifier = Encrypt.SHA1(facebookID + deviceID);
			}
			catch (NoSuchAlgorithmException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getIdentifier()
	{
		return identifier;
	}

	public String getFacebookID()
	{
		return facebookID;
	}

	public Profile getProfile()
	{
		if (profile == null) profile = new Profile();
		return profile;
	}

	public LatLng getHome()
	{
		return profile.getHome();
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/**
	 * @param profileInfoDisplayer
	 *            the profileInfoDisplayer to set
	 */
	public void setProfileInfoDisplayer(ProfileInfoDisplayer profileInfoDisplayer)
	{
		this.profileInfoDisplayer = profileInfoDisplayer;
	}

	public void setFacebookID(String facebookID)
	{
		this.facebookID = facebookID;
		generateIdentifier();
		saveAccount();
	}

	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

}
