package ch.hearc.corporations.view;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import ch.hearc.corporations.controller.PlayersManager;
import ch.hearc.corporations.model.PlayerArrayAdapter;

public class LeaderboardActivity extends ListActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Show the Up button in the action bar.
		setupActionBar();
		PlayerArrayAdapter adapter = new PlayerArrayAdapter(this, PlayersManager.getInstance().getPlayers());

		setListAdapter(adapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar()
	{

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
