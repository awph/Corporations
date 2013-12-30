/*=====================================================================*
| This file declares the following classes:
|    TripActivity.java
|
| Description of the class TripActivity.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 30 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.view;

import android.app.ListActivity;
import android.os.Bundle;
import ch.hearc.corporations.controller.TripManager;
import ch.hearc.corporations.model.TripArrayAdapter;

/**
 * @author Alexandre
 * 
 */
public class TripActivity extends ListActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		TripArrayAdapter adapter = new TripArrayAdapter(this, TripManager.loadTrips(this));
		setListAdapter(adapter);
	}
}
