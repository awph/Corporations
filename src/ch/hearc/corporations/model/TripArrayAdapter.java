/*=====================================================================*
| This file declares the following classes:
|    PlayerArrayAdapter.java
|
| Description of the class PlayerArrayAdapter.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 24 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hearc.corporations.R;
import ch.hearc.corporations.view.TripDetailActivity;

/**
 * @author Alexandre
 * 
 */
public class TripArrayAdapter extends ArrayAdapter<Trip>
{

	/*------------------------------------------------------------------*\
	|*							Private Attributs						*|
	\*------------------------------------------------------------------*/

	private List<Trip>	trips;
	private Context		context;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public TripArrayAdapter(Context context, List<Trip> trips)
	{
		super(context, R.layout.trip_row_fragment, trips);
		Collections.sort(trips, new Comparator<Trip>() {

			@Override
			public int compare(Trip trip1, Trip trip2)
			{
				return trip1.getDate().compareTo(trip2.getDate());
			}
		});
		this.trips = trips;
		this.context = context;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.trip_row_fragment, parent, false);
		}
		TextView date = (TextView) row.findViewById(R.id.date_textview);
		TextView distance = (TextView) row.findViewById(R.id.distance_textView);

		final Trip trip = trips.get(position);
		DateFormat dateFormat = SimpleDateFormat.getDateInstance();
		date.setText(dateFormat.format(trip.getDate()));
		distance.setText(String.format("%.2fkm", trip.getDistance() / 1000.));

		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(context, TripDetailActivity.class);
				intent.putExtra(TripDetailActivity.DISTANCE, trip.getDistance());
				intent.putExtra(TripDetailActivity.EXPERIENCE, trip.getExperienceEarned());
				intent.putExtra(TripDetailActivity.MONEY, trip.getMoneyEarned());
				intent.putExtra(TripDetailActivity.TIME, trip.getTime());
				context.startActivity(intent);
			}
		});

		return row;
	}
}
