package ch.hearc.corporations.view;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.TerritoriesManager;
import ch.hearc.corporations.model.MercatorProjection;
import ch.hearc.corporations.model.Territory;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class TerritoriesFragment extends Fragment
{
	private GoogleMap			map;
	private boolean				actived;
	public static LatLng		currentLocation	= new LatLng(47.039340, 6.799249);
	private static final LatLng	HOUSE			= new LatLng(47.039340, 6.799249);
	public static final int		BORDER_COLOR	= Color.argb(150, 0, 0, 0);
	private TerritoriesManager			territories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.territories_fragment, container, false);
		actived = false;
		territories = new TerritoriesManager();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

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
					Object[] territories = TerritoriesFragment.this.territories.getTerritoryPolygoneForLocation(target, map);
					//map.clear();
					//for (int i = 0; i < 70 && i < territories.length; ++i)
						//map.addPolygon(((Territory) territories[i]).getPolygon());
				}
			}
		});
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(HOUSE, 13));
		addTerritoryInfoListener(map, (TerritoryInfoFragment) getFragmentManager().findFragmentById(R.id.territory));

		Button showProfileButton = (Button) view.findViewById(R.id.showProfile);
		showProfileButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				((MainActivity) getActivity()).showProfileFragment();
			}
		});
		return view;
	}

	void addTerritoryInfoListener(final GoogleMap map, final TerritoryInfoFragment fragment)
	{
		getFragmentManager().beginTransaction().hide(fragment).commit();
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng location)
			{
				Territory territory = territories.getTerritoryForLocation(location, map);
				boolean showFragment = fragment.updateTerritoryInfo(territory);
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
					territory.setHighlighted(false);//TODO
					ft.hide(fragment);
				}
				ft.commit();
			}
		});
	}

	private void addOverlay(GoogleMap map, LatLng center)
	{
		currentLocation = center;
		for (int i = -5; i < 5; ++i)
		{
			for (int j = -5; j < 5; ++j)
			{
				map.addPolygon(new PolygonOptions()
						.add(MercatorProjection.fromLatLng(new LatLng(center.latitude + i * 0.001, center.longitude + j * 0.001)).getLatLng(),
								MercatorProjection.fromLatLng(new LatLng(center.latitude + (i + 1) * 0.001, center.longitude + j * 0.001)).getLatLng(),
								MercatorProjection.fromLatLng(new LatLng(center.latitude + (i + 1) * 0.001, center.longitude + (j + 1) * 0.001)).getLatLng(),
								MercatorProjection.fromLatLng(new LatLng(center.latitude + i * 0.001, center.longitude + (j + 1) * 0.001)).getLatLng()).strokeColor(BORDER_COLOR).fillColor(0)
						.strokeWidth(1f));
			}
		}
	}

	public void setActived(boolean actived)
	{
		this.actived = actived;
	}

	private static final String	FACEBOOK_LOG	= "Log : Facebook";

	private static final String	LOG				= "Log : MainActivity";

	private static final String	PACKAGE_NAME	= "com.example.howtoandroid";
}
