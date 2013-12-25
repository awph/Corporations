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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;

/**
 * @author Alexandre
 * 
 */
public class PlayerArrayAdapter extends ArrayAdapter<Player>
{
	private List<Player>	players;

	public PlayerArrayAdapter(Context context, List<Player> players)
	{
		super(context, R.layout.leaderboard_row_fragment, players);
		Collections.sort(players, new Comparator<Player>() {

			@Override
			public int compare(Player player1, Player player2)
			{
				int rank1 = player1.getRank();
				int rank2 = player2.getRank();
				return rank1 < rank2 ? -1 : (rank1 == rank2 ? 0 : 1);
			}
		});
		this.players = players;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.leaderboard_row_fragment, parent, false);
		}
		TextView rank = (TextView) row.findViewById(R.id.rank_textview);
		TextView name = (TextView) row.findViewById(R.id.name_textView);
		TextView numberTerritories = (TextView) row.findViewById(R.id.number_territories_textview);

		Player player = players.get(position);
		rank.setText(Integer.toString(player.getRank()));
		name.setText(player.getName());
		numberTerritories.setText(Integer.toString(player.getNumberTerritories()));
		Resources resources = Corporations.getAppContext().getResources();
		if (player.getUserID().equals(AccountController.getInstance().getProfile().getUserID()))
		{
			rank.setTextColor(resources.getColor(R.color.green));
			name.setTextColor(resources.getColor(R.color.green));
			numberTerritories.setTextColor(resources.getColor(R.color.green));
		}
		else if (player.isAlly())
		{
			rank.setTextColor(resources.getColor(R.color.blue));
			name.setTextColor(resources.getColor(R.color.blue));
			numberTerritories.setTextColor(resources.getColor(R.color.blue));
		}
		else
		{
			rank.setTextColor(resources.getColor(R.color.black));
			name.setTextColor(resources.getColor(R.color.black));
			numberTerritories.setTextColor(resources.getColor(R.color.black));
		}
		return row;
	}

}
