/*=====================================================================*
| This file declares the following classes:
|    MainActivity.java
|
| Description of the class MainActivity.java :
| View class for displays the correct frament (login or territories)
| Contains the operation to do when the app start.
| Contains some method for locate player.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.view;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;
import ch.hearc.corporations.controller.TripManager;
import ch.hearc.corporations.model.Trip;
import ch.hearc.corporations.service.OnBootBroadcastReceiver;
import ch.hearc.corporations.service.TripService;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity implements LocationListener
{
	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static final int			CLOSE_FACEBOOK_SESSION	= 100;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private boolean					isResumed				= false;
	private Fragment[]				fragments				= new Fragment[FRAGMENT_COUNT];
	private UiLifecycleHelper		uiHelper;
	private Session.StatusCallback	callback				= new Session.StatusCallback() {
																@Override
																public void call(Session session, SessionState state, Exception exception)
																{
																	onSessionStateChange(session, state, exception);
																}
															};
	private LocationRequest			locationRequest;
	private LocationClient			locationClient;
	private LocationClient			tempLocationClient;
	private Location				currentLocation;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final int		LOGIN_FRAGMENT			= 0;
	private static final int		TERRITORIES_FRAGMENT	= 1;
	private static final int		FRAGMENT_COUNT			= TERRITORIES_FRAGMENT + 1;
	private static final long		FIVE_MINUTES			= 5 * 60 * 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Location
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(FIVE_MINUTES);
		locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		locationRequest.setFastestInterval(FIVE_MINUTES);

		ConnectionCallbacks connectionCallbacks = new GooglePlayServicesClient.ConnectionCallbacks() {
			@Override
			public void onDisconnected()
			{
			}

			@Override
			public void onConnected(Bundle connectionHint)
			{
				locationClient.requestLocationUpdates(locationRequest, MainActivity.this);
				showLoginButton(true);
			}
		};

		locationClient = new LocationClient(this, connectionCallbacks, new OnConnectionFailedListener() {

			@Override
			public void onConnectionFailed(ConnectionResult arg0)
			{
				Tools.showInfoAlertDialog(MainActivity.this, getResources().getString(R.string.verify_gps_title), getResources().getString(R.string.verify_gps_message));
			}
		});

		// Start trip tracking if it's not running
		if (!TripService.running) registerReceiver(new OnBootBroadcastReceiver(), new IntentFilter(Intent.ACTION_TIME_TICK));

		// Timer for update profile info
		Timer timer = new Timer("profile updater");
		timer.schedule(new TimerTask() {

			@Override
			public void run()
			{
				AccountController.getInstance().updateProfile();
			}
		}, FIVE_MINUTES, FIVE_MINUTES);

		uploadTrips();

		getActionBar().hide();

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

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		showLoginButton(false);
		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS)
			locationClient.connect();
		else
			Toast.makeText(this, getString(R.string.update_google_play_service), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onResume()
	{
		super.onResume();

		Session session = Session.getActiveSession();

		isResumed = true;
		uiHelper.onResume();
		onSessionStateChange(session, session.getState(), null);
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
		showLoginButton(false);
		if (resultCode == CLOSE_FACEBOOK_SESSION)
		{
			Session session = Session.getActiveSession();
			if (session != null && session.isOpened())
			{
				session.closeAndClearTokenInformation();
			}
			showFragment(LOGIN_FRAGMENT, false);
			showLoginButton(true);
		}
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

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * @return the current player location
	 */
	public Location getCurrentLocation()
	{
		if (currentLocation == null) currentLocation = locationClient.getLastLocation();
		return currentLocation;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		this.currentLocation = location;
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Upload the trips finished to the server
	 */
	private void uploadTrips()
	{
		new Thread(new Runnable() {

			@Override
			public void run()
			{
				List<Trip> trips = TripManager.loadTrips(MainActivity.this);
				for (Trip trip : trips)
				{
					if (trip.isFinished() && !trip.isSent()) trip.send();
				}

				TripManager.saveTrips(MainActivity.this, trips);
			}
		}).start();
	}

	/**
	 * Switch fragment displayed.
	 * 
	 * @param fragmentIndex
	 *            is the index of the fragment that you want to display
	 * @param addToBackStack
	 *            true if you want to add the transaction to back stack
	 */
	private void showFragment(int fragmentIndex, boolean addToBackStack)
	{
		if (fragmentIndex == TERRITORIES_FRAGMENT)
		{
			((TerritoriesFragment) fragments[TERRITORIES_FRAGMENT]).setActived(true);
			((TerritoriesFragment) fragments[TERRITORIES_FRAGMENT]).zoomOnHome();
		}
		else
			((TerritoriesFragment) fragments[TERRITORIES_FRAGMENT]).setActived(false);

		showLoginButton(true);

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
		try
		{
			transaction.commit();
		}
		catch (IllegalStateException e)
		{
			// Dont change view
		}
	}

	/**
	 * Facebook method called when the Facebook session change.
	 * @param session the Facebook session
	 * @param state the session state
	 * @param exception Facebook exception
	 */
	private void onSessionStateChange(final Session session, SessionState state, Exception exception)
	{
		// Only make changes if the activity is visible
		if (isResumed)
		{
			FragmentManager manager = getFragmentManager();
			int backStackSize = manager.getBackStackEntryCount();

			for (int i = 0; i < backStackSize; i++)
			{
				manager.popBackStack();
			}

			if (state.isOpened())
			{
				showLoginButton(false);
				Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response)
					{
						if (user != null)
						{
							AccountController.getInstance().setFacebookID(user.getId());
							if (AccountController.getInstance().getHome() == null)
							{
								if (currentLocation == null)
								{

									tempLocationClient = new LocationClient(MainActivity.this, new GooglePlayServicesClient.ConnectionCallbacks() {

										@Override
										public void onDisconnected()
										{
										}

										@Override
										public void onConnected(Bundle arg0)
										{
											currentLocation = tempLocationClient.getLastLocation();
											if (currentLocation == null)
											{
												Tools.showInfoAlertDialog(MainActivity.this, getResources().getString(R.string.verify_gps_title), getResources().getString(R.string.verify_gps_message));
												Session.getActiveSession().closeAndClearTokenInformation();
											}
											else
											{
												loginToServer(session, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
											}
											tempLocationClient.disconnect();
											tempLocationClient = null;
										}
									}, new GooglePlayServicesClient.OnConnectionFailedListener() {

										@Override
										public void onConnectionFailed(ConnectionResult arg0)
										{
											Tools.showInfoAlertDialog(MainActivity.this, getResources().getString(R.string.verify_gps_title), getResources().getString(R.string.verify_gps_message));
										}
									});
									tempLocationClient.connect();
								}
								else
									loginToServer(session, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
							}
							else
							{
								loginToServer(session, AccountController.getInstance().getHome());
							}
						}
					}

					/**
					 * Log to the server
					 * 
					 * @param session
					 *            is the Facebook session for accessToken
					 * @param location
					 *            is the current player location
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
				if (locationClient.isConnected()) showLoginButton(true);
				showFragment(LOGIN_FRAGMENT, false);
			}
		}
	}

	/**
	 * Show or hide the login button and the progess
	 * 
	 * @param show
	 *            true if we want to show the login button
	 */
	private void showLoginButton(boolean show)
	{
		if (show)
		{
			findViewById(R.id.login_button).setVisibility(View.VISIBLE);
			findViewById(R.id.progressBar).setVisibility(View.GONE);
		}
		else
		{
			findViewById(R.id.login_button).setVisibility(View.GONE);
			findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
		}
	}
}
