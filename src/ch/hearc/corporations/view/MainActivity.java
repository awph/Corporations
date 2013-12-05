package ch.hearc.corporations.view;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Territory;

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
	private Session					session;
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
		Log.d(LOG, "Before layout");
		setContentView(R.layout.main);
		Log.d(LOG, "After layout");

		FragmentManager fm = getFragmentManager();
		fragments[LOGIN_FRAGMENT] = fm.findFragmentById(R.id.splashFragment);
		fragments[TERRITORIES_FRAGMENT] = fm.findFragmentById(R.id.territoriesFragment);

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			transaction.hide(fragments[i]);
		}
		transaction.commit();

		getActionBar().hide();
		generateKeyHash();
		/*
		DataLoader.getInstance().getTerritoriesForLocation(new LatLng(47.597, 6.99), new DataLoaderAdapter() {

			@Override
			public void territoriesFetched(List<Territory> territories)
			{
			}
		});*/
	}

	private void showFragment(int fragmentIndex, boolean addToBackStack)
	{
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++)
		{
			if (i == fragmentIndex)
			{
				transaction.show(fragments[i]);
			}
			else
			{
				transaction.hide(fragments[i]);
			}
		}
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
							Log.i(FACEBOOK_LOG, "User Name " + user.getFirstName() + " " + user.getLastName());
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
							public void connexionFinished(Profile profile)
							{
								showFragment(TERRITORIES_FRAGMENT, false);
								AccountController.getInstance().setProfile(profile);
								AccountController.getInstance().setHome(profile.getHome());
								Log.e("Login", AccountController.getInstance().getProfile().toString());
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

	/*
	 * @Override protected void onResumeFragments() { super.onResumeFragments();
	 * Session session = Session.getActiveSession();
	 * 
	 * if (session != null && session.isOpened()) { // if the session is already
	 * open, // try to show the selection fragment
	 * showFragment(TERRITORIES_FRAGMENT, false); } else { // otherwise present
	 * the splash screen // and ask the person to login.
	 * showFragment(LOGIN_FRAGMENT, false); } }
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_logout:
				showFragment(LOGIN_FRAGMENT, true);
				return true;
		}
		return false;
	}

	@Override
	public void onResume()
	{
		super.onResume();

		Session session = Session.getActiveSession();

		if (session != null && session.isOpened())
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
				Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
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

	private static final String	FACEBOOK_LOG	= "Log : Facebook";

	private static final String	LOG				= "Log : MainActivity";

	private static final String	PACKAGE_NAME	= "ch.hearc.corporations";
}
