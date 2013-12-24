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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.model.Skill;

/**
 * @author Alexandre
 * 
 */
public class ProfileFragment extends Fragment
{
	private SkillFragment[]	skillFragments;
	private View			view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		skillFragments = new SkillFragment[6];
		view = inflater.inflate(R.layout.profile_fragment, container, false);
		initSkills();
		addFragmentsToView();
		return view;
	}

	private void addFragmentsToView()
	{
        // Update the layout
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // The id specified here identifies which ViewGroup to
        // append the Fragment to.
		//ViewGroup skillsLayout = (ViewGroup) view.findViewById(R.id.skills_layout);
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
}
