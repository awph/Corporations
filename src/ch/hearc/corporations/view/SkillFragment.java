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

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;
import ch.hearc.corporations.model.Profile;
import ch.hearc.corporations.model.Skill;
import ch.hearc.corporations.model.SkillType;

/**
 * @author Alexandre
 * 
 */
public class SkillFragment extends Fragment
{
	private SkillType	skillType;
	private View		view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.skill_fragment, container, false);

		final Skill skill = AccountController.getInstance().getProfile().getSkill(skillType);

		((TextView) view.findViewById(R.id.skill_name)).setText(skill.getType().getName());

		updateLevel();

		((Button) view.findViewById(R.id.upgrade_skill)).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setTitle(getActivity().getResources().getString(R.string.improve_alert_dialog_title));
				alertDialog.setMessage(String.format(getActivity().getResources().getString(R.string.improve_alert_dialog_message), skill.getName(), skill.getLevel(), skill.getPrice()));
				alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getActivity().getResources().getString(android.R.string.yes), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Profile profile = new Profile(AccountController.getInstance().getProfile());
						profile.getSkill(skill.getType()).upgrade();
						DataLoader.getInstance().updateProfile(profile, new DataLoaderAdapter() {

							@Override
							public void profileUpdated(Status status)
							{
								SkillFragment.this.updateLevel();

							}
						});
					}
				});
				alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getActivity().getResources().getString(android.R.string.no), new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// Nothing to do
					}
				});
				alertDialog.show();
			}
		});

		return view;
	}

	public void setSkillType(SkillType skillType)
	{
		this.skillType = skillType;
	}

	public void updateLevel()
	{
		Skill skill = AccountController.getInstance().getProfile().getSkill(skillType);
		((TextView) view.findViewById(R.id.skill_level)).setText(Integer.toString(skill.getLevel()));
	}
}
