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

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;

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

		((TextView) findViewById(R.id.distance_text_view)).setText(String.format(getResources().getString(R.string.trip_detail_activity_distance), getIntent().getExtras().getFloat(DISTANCE) / 1000));
		((TextView) findViewById(R.id.earn_experience_text_view)).setText(String.format(getResources().getString(R.string.trip_detail_activity_experience_points),
				Integer.toString(getIntent().getExtras().getInt(EXPERIENCE))));
		((TextView) findViewById(R.id.earn_money_text_view)).setText(String.format(getResources().getString(R.string.trip_detail_activity_money_earn),
				Tools.formatMoney(getIntent().getExtras().getInt(MONEY))));

		((TextView) findViewById(R.id.trip_date_text_view)).setText(String.format(getResources().getString(R.string.trip_detail_activity_date),
				SimpleDateFormat.getDateInstance().format(((Date) getIntent().getExtras().get(DATE)))));
		((TextView) findViewById(R.id.trip_time_text_view)).setText(String.format(getResources().getString(R.string.trip_detail_activity_time), Long.toString(getIntent().getExtras().getLong(TIME))));

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
	public static final String	DATE		= "date";
}
