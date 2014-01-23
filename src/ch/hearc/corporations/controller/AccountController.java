/*=====================================================================*
| This file declares the following classes:
|    AccountController.java
|
| Description of the class AccountController.java :
| This class let access to the profile info for all the app.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

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

	/**
	 * Callback service for update player infos
	 */
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
		restoreHome();
		generateIdentifier();
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * @return the static instance of the class
	 */
	public static AccountController getInstance()
	{
		if (instance == null) instance = new AccountController();
		return instance;
	}

	/**
	 * Check if the Facebook ID is the same that the one is saved.
	 * 
	 * @param facebookID
	 *            is the new one
	 * @return true if they are the same
	 */
	public boolean checkFacebookID(String facebookID)
	{
		return this.facebookID.equals(facebookID);
	}

	/**
	 * Re-load the profile info from the server
	 */
	public void updateProfile()
	{
		DataLoader.getInstance().getProfile(new DataLoaderAdapter() {

			@Override
			public void profileFetched(Status status)
			{
				if (profileInfoDisplayer != null) profileInfoDisplayer.updateProfileInfo();
				AccountController.this.home = AccountController.this.profile.getHome();
				AccountController.this.saveHome();
			}
		});
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Restore FacebookID from the preferences
	 */
	private void restoreFacebookID()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		facebookID = preferences.getString(FACEBOOK_ID_KEY, null);
	}

	/**
	 * Restore the home location from the preferences
	 */
	private void restoreHome()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		String homeLat = preferences.getString(HOME_LAT_KEY, null);
		String homeLng = preferences.getString(HOME_LNG_KEY, null);
		if (homeLat != null && homeLng != null)
			home = new LatLng(Double.parseDouble(homeLat), Double.parseDouble(homeLng));
		else
			home = null;
	}

	/**
	 * Save FacebookID in the preferences
	 */
	private void saveAccount()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(FACEBOOK_ID_KEY, facebookID);
		editor.commit();
	}

	/**
	 * Save the home location in the preferences
	 */
	private void saveHome()
	{
		SharedPreferences preferences = Corporations.getAppContext().getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		if (home != null)
		{
			editor.putString(HOME_LAT_KEY, Double.toString(home.latitude));
			editor.putString(HOME_LNG_KEY, Double.toString(home.longitude));
		}
		editor.commit();
	}

	/**
	 * Generate the identifier for the unique server identification. Make a SHA1
	 * with the FacebookId and the device ID
	 */
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

	/**
	 * @return the unique identifier
	 */
	public String getIdentifier()
	{
		return identifier;
	}

	/**
	 * @return the Facebook ID
	 */
	public String getFacebookID()
	{
		return facebookID;
	}

	/**
	 * @return the player profile
	 */
	public Profile getProfile()
	{
		if (profile == null) profile = new Profile();
		return profile;
	}

	/**
	 * @return the home location
	 */
	public LatLng getHome()
	{
		return home;
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/**
	 * Set the callback function for update profile info
	 * 
	 * @param profileInfoDisplayer
	 *            the profileInfoDisplayer to set
	 */
	public void setProfileInfoDisplayer(ProfileInfoDisplayer profileInfoDisplayer)
	{
		this.profileInfoDisplayer = profileInfoDisplayer;
	}

	/**
	 * Set the Facebook ID
	 * 
	 * @param facebookID
	 *            the facebookID to set
	 */
	public void setFacebookID(String facebookID)
	{
		this.facebookID = facebookID;
		generateIdentifier();
		saveAccount();
	}

	/**
	 * Set the profile
	 * 
	 * @param profile
	 *            the profile to set
	 */
	public void setProfile(Profile profile)
	{
		this.profile = profile;
	}

}
