/*=====================================================================*
| This file declares the following classes:
|    ProfileActivity.java
|
| Description of the class ProfileActivity.java :
| View class for displays all profile info and update skills
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 12 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.view;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.SkillType;

/**
 * @author Alexandre
 * 
 */
public class ProfileActivity extends Activity
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private SkillFragment[]	skillFragments;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		skillFragments = new SkillFragment[6];
		setBackground(CorporationsConfiguration.BACKGROUND_OWN);
		setProfileInfo();
		initSkills();
		addFragmentsToView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_activity, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle presses on the action bar items
		switch (item.getItemId())
		{
			case R.id.action_logout:
				setResult(MainActivity.CLOSE_FACEBOOK_SESSION);
				finish();
				return true;
			case R.id.action_leaderboard:
				DataLoader.getInstance().getLeaderboard(0, 1000, new DataLoaderAdapter() {

					@Override
					public void leaderboardFetched(List<Player> players, Status status)
					{
						if (status == Status.OK)
						{
							Intent intent = new Intent(ProfileActivity.this, LeaderboardActivity.class);
							startActivity(intent);
						}
					}

				});
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Refresh all the informations of the player
	 */
	public void refreshProfileInfo()
	{
		TextView profileInfo = (TextView) findViewById(R.id.profile_info);
		Profile profile = AccountController.getInstance().getProfile();
		StringBuilder stringBuilder = new StringBuilder("Current money: " + Tools.formatMoney(profile.getCurrentMoney()));
		stringBuilder.append("\nTotal revenue: " + Tools.formatMoney(profile.getCurrentRevenue()));
		stringBuilder.append("\nTrip money earned: " + Tools.formatMoney(profile.getTripMoneyEarned()));
		stringBuilder.append("\nTotal gain: " + Tools.formatMoney(profile.getTotalGain()));
		stringBuilder.append("\nAllies: " + profile.getNumberAllies());
		stringBuilder.append("\nTerrotories owned: " + profile.getNumberTerritories());
		stringBuilder.append("\nExperience points: " + profile.getExperiencePoints());
		stringBuilder.append("\nRank: " + profile.getRank());
		profileInfo.setText(stringBuilder);
	}

	/**
	 * Update the skill fragments description
	 */
	public void displayed()
	{
		for (SkillFragment skillFragment : skillFragments)
		{
			skillFragment.updateLevel();
		}
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Set the image of the player
	 */
	private void setProfileInfo()
	{
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture_view);
		profilePictureView.setProfileId(AccountController.getInstance().getFacebookID());
		profilePictureView.setBorderWidth(4);
		profilePictureView.setBorderColor(getResources().getColor(R.color.white));
		refreshProfileInfo();
	}

	/**
	 * Add the skill's fragments to the view.
	 */
	private void addFragmentsToView()
	{
		// Update the layout
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		// The id specified here identifies which ViewGroup to
		// append the Fragment to.
		// ViewGroup skillsLayout = (ViewGroup)
		// view.findViewById(R.id.skills_layout);
		for (SkillFragment skillFragment : skillFragments)
		{
			ft.add(R.id.skills_layout, skillFragment);
		}
		ft.commit();
	}

	/**
	 * Create the skill fragments
	 */
	private void initSkills()
	{
		skillFragments[SkillType.purchasePrice.getNumber()] = new SkillFragment();
		skillFragments[SkillType.purchasePrice.getNumber()].setSkillType(SkillType.purchasePrice);
		skillFragments[SkillType.purchaseDistance.getNumber()] = new SkillFragment();
		skillFragments[SkillType.purchaseDistance.getNumber()].setSkillType(SkillType.purchaseDistance);
		skillFragments[SkillType.experienceLimit.getNumber()] = new SkillFragment();
		skillFragments[SkillType.experienceLimit.getNumber()].setSkillType(SkillType.experienceLimit);
		skillFragments[SkillType.moneyLimit.getNumber()] = new SkillFragment();
		skillFragments[SkillType.moneyLimit.getNumber()].setSkillType(SkillType.moneyLimit);
		skillFragments[SkillType.experienceQuantityFound.getNumber()] = new SkillFragment();
		skillFragments[SkillType.experienceQuantityFound.getNumber()].setSkillType(SkillType.experienceQuantityFound);
		skillFragments[SkillType.alliancePrice.getNumber()] = new SkillFragment();
		skillFragments[SkillType.alliancePrice.getNumber()].setSkillType(SkillType.alliancePrice);
	}

	/**
	 * Set a gradient background
	 * 
	 * @param gradientDrawable
	 *            the color
	 */
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
