/*=====================================================================*
| This file declares the following classes:
|    ProfileFragment.java
|
| Description of the class ProfileFragment.java :
|
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
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Skill;

/**
 * @author Alexandre
 * 
 */
public class ProfileActivity extends Activity
{
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
				// TODO
				return true;
			case R.id.action_settings:
				// TODO
				return true;
			case R.id.action_leaderboard:
				DataLoader.getInstance().getLeaderboard(0, 1000, new DataLoaderAdapter() {

					@Override
					public void leaderboardFetched(List<Player> players)
					{
						Intent intent = new Intent(ProfileActivity.this, LeaderboardActivity.class);
						startActivity(intent);
					}

				});
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void setProfileInfo()
	{
		ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profile_picture_view);
		profilePictureView.setProfileId(AccountController.getInstance().getFacebookID());
		profilePictureView.setBorderWidth(4);
		profilePictureView.setBorderColor(getResources().getColor(R.color.white));
		TextView textView = (TextView) findViewById(R.id.profile_info);
		Profile profile = AccountController.getInstance().getProfile();
		StringBuilder stringBuilder = new StringBuilder("Current money: " + Tools.formatMoney(profile.getCurrentMoney()));
		stringBuilder.append("\nTotal revenue: " + Tools.formatMoney(profile.getCurrentRevenue()));
		stringBuilder.append("\nTrip money earned: " + Tools.formatMoney(profile.getTripMoneyEarned()));
		stringBuilder.append("\nTotal gain: " + Tools.formatMoney(profile.getTotalGain()));
		stringBuilder.append("\nAllies: " + profile.getNumberAllies());
		stringBuilder.append("\nTerrotories owned: " + profile.getNumberTerritories());
		stringBuilder.append("\nExperience points: " + profile.getExperiencePoints());
		stringBuilder.append("\nRank: " + profile.getRank());
		textView.setText(stringBuilder);
	}

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

	public void displayed()
	{
		for (SkillFragment skillFragment : skillFragments)
		{
			skillFragment.updateLevel();
		}
	}

	private void initSkills()
	{
		Skill[] skills = AccountController.getInstance().getProfile().getSkills();

		skillFragments[Skill.SKILL_PURCHASE_PRICE] = new SkillFragment();
		skillFragments[Skill.SKILL_PURCHASE_PRICE].setSkill(skills[Skill.SKILL_PURCHASE_PRICE]);
		skillFragments[Skill.SKILL_PURCHASE_DISTANCE] = new SkillFragment();
		skillFragments[Skill.SKILL_PURCHASE_DISTANCE].setSkill(skills[Skill.SKILL_PURCHASE_DISTANCE]);
		skillFragments[Skill.SKILL_EXPERIENCE_LIMIT] = new SkillFragment();
		skillFragments[Skill.SKILL_EXPERIENCE_LIMIT].setSkill(skills[Skill.SKILL_EXPERIENCE_LIMIT]);
		skillFragments[Skill.SKILL_MONEY_LIMIT] = new SkillFragment();
		skillFragments[Skill.SKILL_MONEY_LIMIT].setSkill(skills[Skill.SKILL_MONEY_LIMIT]);
		skillFragments[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND] = new SkillFragment();
		skillFragments[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND].setSkill(skills[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND]);
		skillFragments[Skill.SKILL_ALLIANCE_PRICE] = new SkillFragment();
		skillFragments[Skill.SKILL_ALLIANCE_PRICE].setSkill(skills[Skill.SKILL_ALLIANCE_PRICE]);
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
}
