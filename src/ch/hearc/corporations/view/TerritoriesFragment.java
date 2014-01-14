package ch.hearc.corporations.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.AccountController.ProfileInfoDisplayer;
import ch.hearc.corporations.controller.TerritoriesManager;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Territory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class TerritoriesFragment extends Fragment implements ProfileInfoDisplayer
{

	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static LatLng		currentLocation		= new LatLng(47.039340, 6.799249);
	public static final int		POSITIV_REVENUE		= Color.argb(255, 91, 139, 34);
	public static final int		NEGATIVE_REVENUE	= Color.argb(255, 176, 30, 30);

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private GoogleMap			map;
	private boolean				actived;
	private TerritoriesManager	territories;
	private ProfilePictureView	profilePictureView;
	private TextView			moneyTextView;
	private TextView			revenuTextView;
	private TextView			experiencePointsTextView;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	private static final LatLng	HOUSE				= new LatLng(47.039340, 6.799249);

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.territories_fragment, container, false);

		actived = false;
		territories = new TerritoriesManager();

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		moneyTextView = (TextView) view.findViewById(R.id.money_text_view);
		revenuTextView = (TextView) view.findViewById(R.id.revenue_text_view);
		experiencePointsTextView = (TextView) view.findViewById(R.id.experience_text_view);

		AccountController.getInstance().setProfileInfoDisplayer(this);

		map.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position)
			{
				currentLocation = position.target;
				refreshPolygoneForLocation(position.target);
			}

			private void refreshPolygoneForLocation(LatLng target)
			{
				if (TerritoriesFragment.this.actived)
				{
					TerritoriesFragment.this.territories.getTerritoryPolygoneForLocation(target, map);
					// Object[] territories =
					// TerritoriesFragment.this.territories.getTerritoryPolygoneForLocation(target,
					// map);
					// map.clear();
					// for (int i = 0; i < 70 && i < territories.length; ++i)
					// map.addPolygon(((Territory)
					// territories[i]).getPolygon());
				}
			}
		});
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(HOUSE, 13));
		addTerritoryInfoListener(map, (TerritoryInfoFragment) getFragmentManager().findFragmentById(R.id.territory));

		profilePictureView = (ProfilePictureView) view.findViewById(R.id.profile_picture_view);
		profilePictureView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), ProfileActivity.class);
				getActivity().startActivityForResult(intent, 0);
			}
		});

		ImageView tripImageView = (ImageView) view.findViewById(R.id.trip_image_view);
		tripImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), TripActivity.class);
				startActivity(intent);
			}
		});
		return view;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public void setActived(boolean actived)
	{
		this.actived = actived;
		updateProfileInfo();
		profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
		profilePictureView.setProfileId(AccountController.getInstance().getFacebookID());
	}

	public void updateProfileInfo()
	{
		Profile profile = AccountController.getInstance().getProfile();
		if (profile != null)
		{
			moneyTextView.setText("$" + profile.getCurrentMoney());
			long revenue = profile.getCurrentRevenue();
			if (revenue >= 0)
				revenuTextView.setTextColor(POSITIV_REVENUE);
			else
				revenuTextView.setTextColor(NEGATIVE_REVENUE);

			revenuTextView.setText((revenue >= 0 ? "+" : "-") + "$" + revenue);
			experiencePointsTextView.setText("Exp. " + profile.getExperiencePoints());
		}
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	private void addTerritoryInfoListener(final GoogleMap map, final TerritoryInfoFragment fragment)
	{
		getFragmentManager().beginTransaction().hide(fragment).commit();
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(final LatLng location)
			{
				final Territory territory = territories.getTerritoryForLocation(location, map);
				if (territory.getOwner() != null)
					showInfoView(map, fragment, location, territory, territories.isConnected(territory));
				else
				{
					map.snapshot(new SnapshotReadyCallback() {

						@Override
						public void onSnapshotReady(Bitmap bitmap)
						{
							if (territory.isInWater(map.getProjection(), bitmap))
								Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.in_water_alert_dialog_title),
										getActivity().getResources().getString(R.string.in_water_alert_dialog_message));
							else
								showInfoView(map, fragment, location, territory, territories.isConnected(territory));
						}
					});
				}
			}

			/**
			 * @param map
			 * @param fragment
			 * @param location
			 * @param territory
			 * @param connected
			 */
			private void showInfoView(final GoogleMap map, final TerritoryInfoFragment fragment, LatLng location, Territory territory, boolean isConnected)
			{
				boolean showFragment = fragment.updateTerritoryInfo(territory, isConnected);
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				if (showFragment)
				{
					ft.show(fragment);
					territory.setHighlighted(true);
					map.animateCamera(CameraUpdateFactory.newLatLng(location));
				}
				else
				{
					territory.setHighlighted(false);// TODO
					ft.hide(fragment);
				}
				ft.commit();
			}
		});
	}
}
