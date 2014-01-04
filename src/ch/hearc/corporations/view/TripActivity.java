/*=====================================================================*
| This file declares the following classes:
|    TripActivity.java
|
| Description of the class TripActivity.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 d�c. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.view;

import java.util.List;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.model.Trip;
import ch.hearc.corporations.model.TripArrayAdapter;

/**
 * @author Alexandre
 * 
 */
public class TripActivity extends ListActivity
{

	/*------------------------------------------------------------------*\
	|*							Protected Methods						*|
	\*------------------------------------------------------------------*/

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DataLoader.getInstance().getTrips(0, new DataLoaderAdapter() {

			@Override
			public void tripsFetched(List<Trip> trips)
			{
				TripArrayAdapter adapter = new TripArrayAdapter(TripActivity.this, trips);
				setListAdapter(adapter);
			}
		});
		setBackground(CorporationsConfiguration.BACKGROUND_TRIP);
	}

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setBackground(GradientDrawable gradientDrawable)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
		{
			View someView = findViewById(R.id.profile_info);
			View rootView = someView.getRootView();
			rootView.setBackgroundDrawable(gradientDrawable);
		}
		else
			getWindow().getDecorView().setBackground(gradientDrawable);
	}
}
