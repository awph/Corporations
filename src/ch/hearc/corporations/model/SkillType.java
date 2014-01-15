package ch.hearc.corporations.model;

import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.R;

public enum SkillType
{

	purchasePrice(Corporations.getAppContext().getResources().getString(R.string.purchase_price_skill_name), Corporations.getAppContext().getResources().getString(R.string.purchase_price_skill_description), 0), 
	purchaseDistance(Corporations.getAppContext().getResources().getString(R.string.purchase_distance_skill_name), Corporations.getAppContext().getResources().getString(R.string.purchase_distance_skill_description), 1), 
	experienceLimit(Corporations.getAppContext().getResources().getString(R.string.experience_limit_skill_name), Corporations.getAppContext().getResources().getString(R.string.experience_limit_skill_description), 2), 
	moneyLimit(Corporations.getAppContext().getResources().getString(R.string.money_limit_skill_name),Corporations.getAppContext().getResources().getString(R.string.money_limit_skill_description), 3), 
	experienceQuantityFound(Corporations.getAppContext().getResources().getString(R.string.experience_quantity_found_skill_name), Corporations.getAppContext().getResources().getString(R.string.experience_quantity_found_skill_description), 4), 
	alliancePrice(Corporations.getAppContext().getResources().getString(R.string.alliance_price_skill_name), Corporations.getAppContext().getResources().getString(R.string.alliance_price_skill_description), 5);

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private String	name;
	private String	description;
	private int		number;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	private SkillType(String name, String description, int number)
	{
		this.name = name;
		this.description = description;
		this.number = number;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public String getName()
	{
		return name;
	}

	public int getNumber()
	{
		return number;
	}

	public String getDescription()
	{
		return description;
	}

	public float getExplicitValueSkills()
	{
		float resultValue = 0;
		switch (number)
		{
			case 0: // purchase_price_skill_name
				resultValue = 1.8f;
				break;
			case 1: // purchase_distance_skill_name
				resultValue = 20;
				break;
			case 2: // experience_limit_skill_name
				resultValue = 25;
				break;
			case 3: // money_limit_skill_name
				resultValue = 10000;
				break;
			case 4: // experience_quantity_found_skill_name
				resultValue = 2;
				break;
			case 5: // alliance_price_skill_name
				resultValue = 1.5f;
				break;

			default:
				break;
		}

		return resultValue;
	}
}
