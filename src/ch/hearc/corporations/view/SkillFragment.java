/*=====================================================================*
| This file declares the following classes:
|    SkillFragment.java
|
| Description of the class SkillFragment.java :
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ch.hearc.corporations.R;
import ch.hearc.corporations.model.Skill;

/**
 * @author Alexandre
 * 
 */
public class SkillFragment extends Fragment
{
	private Skill	skill;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.skill_fragment, container, false);
	}

	public void setSkill(Skill skill)
	{
		this.skill = skill;
		update();
	}

	private void update()
	{
		((TextView) getView().findViewById(R.id.skill_name)).setText(skill.getType().getName());
		((TextView) getView().findViewById(R.id.skill_level)).setText(skill.getType().getName() + "level");
		((Button) getView().findViewById(R.id.upgrade_skill)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.e("upgrade", skill.getType().getName());
			}
		});
		;

	}
}
