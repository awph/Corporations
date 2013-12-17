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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Skill;

/**
 * @author Alexandre
 *
 */
public class ProfileFragment extends Fragment
{
	SkillFragment[] skillFragments;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		skillFragments = new SkillFragment[6];
		skillFragments[Skill.SKILL_PURCHASE_PRICE] = (SkillFragment) getFragmentManager().findFragmentById(R.id.purchase_price_skill_fragment);
		skillFragments[Skill.SKILL_PURCHASE_DISTANCE] = (SkillFragment) getFragmentManager().findFragmentById(R.id.purchase_distance_skill_fragment);
		skillFragments[Skill.SKILL_EXPERIENCE_LIMIT] = (SkillFragment) getFragmentManager().findFragmentById(R.id.experience_limit_skill_fragment);
		skillFragments[Skill.SKILL_MONEY_LIMIT] = (SkillFragment) getFragmentManager().findFragmentById(R.id.money_limit_skill_fragment);
		skillFragments[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND] = (SkillFragment) getFragmentManager().findFragmentById(R.id.experience_quantity_found_skill_fragment);
		skillFragments[Skill.SKILL_ALLIANCE_PRICE] = (SkillFragment) getFragmentManager().findFragmentById(R.id.alliance_price_skill_fragment);
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }
	
	public void displayed()
	{
		initSkills();
	}

	private void initSkills()
	{
		Skill[] skills = AccountController.getInstance().getProfile().getSkills();
		skillFragments[Skill.SKILL_PURCHASE_PRICE].setSkill(skills[Skill.SKILL_PURCHASE_PRICE]);
		skillFragments[Skill.SKILL_PURCHASE_DISTANCE].setSkill(skills[Skill.SKILL_PURCHASE_DISTANCE]);
		skillFragments[Skill.SKILL_EXPERIENCE_LIMIT].setSkill(skills[Skill.SKILL_EXPERIENCE_LIMIT]);
		skillFragments[Skill.SKILL_MONEY_LIMIT].setSkill(skills[Skill.SKILL_MONEY_LIMIT]);
		skillFragments[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND].setSkill(skills[Skill.SKILL_EXPERIENCE_QUANTITY_FOUND]);
		skillFragments[Skill.SKILL_ALLIANCE_PRICE].setSkill(skills[Skill.SKILL_ALLIANCE_PRICE]);
	}
}
