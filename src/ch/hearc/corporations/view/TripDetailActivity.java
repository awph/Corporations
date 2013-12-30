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

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
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
	}

	public static final String	DISTANCE	= "distance";
	public static final String	EXPERIENCE	= "experience";
	public static final String	MONEY		= "money";
	public static final String	TIME		= "time";
}
