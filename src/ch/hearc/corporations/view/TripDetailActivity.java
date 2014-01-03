/*=====================================================================*
| This file declares the following classes:
|    TripDetailActivity.java
|
| Description of the class TripDetailActivity.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;

/**
 * @author Alexandre
 * 
 */
public class TripDetailActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_detail_activity);
		((TextView) findViewById(R.id.distance_text_view)).setText(Float.toString(getIntent().getExtras().getFloat(DISTANCE)));
		((TextView) findViewById(R.id.earn_experience_text_view)).setText(Integer.toString(getIntent().getExtras().getInt(EXPERIENCE)));
		((TextView) findViewById(R.id.earn_money_text_view)).setText(Integer.toString(getIntent().getExtras().getInt(MONEY)));
		((TextView) findViewById(R.id.trip_time_text_view)).setText(Long.toString(getIntent().getExtras().getLong(TIME)));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setBackground(CorporationsConfiguration.BACKGROUND_TRIP);
	}

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

	public static final String	DISTANCE	= "distance";
	public static final String	EXPERIENCE	= "experience";
	public static final String	MONEY		= "money";
	public static final String	TIME		= "time";
}
