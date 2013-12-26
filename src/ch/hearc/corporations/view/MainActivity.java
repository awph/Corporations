package ch.hearc.corporations.view;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity
{
	private boolean					isResumed				= false;
	private static final int		LOGIN_FRAGMENT			= 0;
	private static final int		TERRITORIES_FRAGMENT	= 1;
	private static final int		FRAGMENT_COUNT			= TERRITORIES_FRAGMENT + 1;

	private Fragment[]				fragments				= new Fragment[FRAGMENT_COUNT];

	private UiLifecycleHelper		uiHelper;
	private Session.StatusCallback	callback				= new Session.StatusCallback() {
																@Override
																public void call(Session session, SessionState state, Exception exception)
																{
																	onSessionStateChange(session, state, exception);
																}
															};
	LocationClient					locationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		FragmentManager fm = getFragmentManager();
		fragments[LOGIN_FRAGMENT] = fm.findFragmentById(R.id.loginFragment);
		fragments[TERRITORIES_FRAGMENT] = fm.findFragmentById(R.id.territoriesFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		transaction.show(fragments[LOGIN_FRAGMENT]);
		transaction.commit();

		getActionBar().hide();
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack)
	{
		if (fragmentIndex == TERRITORIES_FRAGMENT)
			((TerritoriesFragment) fragments[TERRITORIES_FRAGMENT]).setActived(true);
		else
			((TerritoriesFragment) fragments[TERRITORIES_FRAGMENT]).setActived(false);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		int currentFragment = -1;
		for (int i = 0; i < fragments.length; ++i)
			if (fragments[i].isVisible()) currentFragment = i;
		transaction.show(fragments[fragmentIndex]);
		if (currentFragment >= 0 && currentFragment != fragmentIndex) transaction.hide(fragments[currentFragment]);

		if (addToBackStack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	private void onSessionStateChange(final Session session, SessionState state, Exception exception)
	{
		// Only make changes if the activity is visible
		if (isResumed)
		{
			FragmentManager manager = getFragmentManager();
			// Get the number of entries in the back stack
			int backStackSize = manager.getBackStackEntryCount();
			// Clear the back stack
			for (int i = 0; i < backStackSize; i++)
			{
				manager.popBackStack();
			}
			if (state.isOpened())
			{
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response)
					{
						if (user != null)
						{
							AccountController.getInstance().setFacebookID(user.getId());
							if (AccountController.getInstance().getHome() == null)
							{
								locationClient = new LocationClient(MainActivity.this, new GooglePlayServicesClient.ConnectionCallbacks() {

									@Override
									public void onDisconnected()
									{
										// TODO Auto-generated method stub

									}

									@Override
									public void onConnected(Bundle arg0)
									{
										Location location = locationClient.getLastLocation();
										loginToServer(session, new LatLng(location.getLatitude(), location.getLongitude()));
										locationClient.disconnect();
									}
								}, new GooglePlayServicesClient.OnConnectionFailedListener() {

									@Override
									public void onConnectionFailed(ConnectionResult arg0)
									{
										// TODO Auto-generated method stub

									}
								});
								locationClient.connect();
							}
							else
							{
								loginToServer(session, AccountController.getInstance().getHome());
							}
							Log.i(FACEBOOK_TAG, "User Name " + user.getFirstName() + " " + user.getLastName());
						}
					}

					/**
					 * @param session
					 * @param location
					 */
					private void loginToServer(final Session session, LatLng location)
					{
						DataLoader.getInstance().loginToServer(session.getAccessToken(), location, new DataLoaderAdapter() {

							@Override
							public void connectionFinished(Status status)
							{
								showFragment(TERRITORIES_FRAGMENT, false);
							}
						});
					}
				}).executeAsync();
			}
			else if (state.isClosed())
			{
				// If the session state is closed:
				// Show the login fragment
				showFragment(LOGIN_FRAGMENT, false);
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();

		Session session = Session.getActiveSession();

		if (session != null && session.isOpened() && AccountController.getInstance().getProfile() != null)
		{
			// if the session is already open,
			// try to show the selection fragment
			showFragment(TERRITORIES_FRAGMENT, false);
		}
		else
		{
			// otherwise present the splash screen
			// and ask the person to login.
			showFragment(LOGIN_FRAGMENT, false);
		}

		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public void generateKeyHash()
	{
		PackageInfo info;
		try
		{
			info = getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES);

			for (Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d(TAG, "KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	private static final String	FACEBOOK_TAG	= "Log : Facebook";

	private static final String	TAG				= "Log : MainActivity";

	private static final String	PACKAGE_NAME	= "ch.hearc.corporations";
}
