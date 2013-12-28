package ch.hearc.corporations.model;

import ch.hearc.corporations.Corporations;
import ch.hearc.corporations.R;

public enum SkillType
{

	purchasePrice(Corporations.getAppContext().getResources().getString(R.string.purchase_price_skill_name), 0), purchaseDistance(Corporations.getAppContext().getResources()
			.getString(R.string.purchase_distance_skill_name), 1), experienceLimit(Corporations.getAppContext().getResources().getString(R.string.experience_limit_skill_name), 2), moneyLimit(
			Corporations.getAppContext().getResources().getString(R.string.money_limit_skill_name), 3), experienceQuantityFound(Corporations.getAppContext().getResources()
			.getString(R.string.experience_quantity_found_skill_name), 4), alliancePrice(Corporations.getAppContext().getResources().getString(R.string.alliance_price_skill_name), 5);

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private String	name;
	private int		number;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	private SkillType(String name, int number)
	{
		this.name = name;
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
}
