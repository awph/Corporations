package ch.hearc.corporations.model;

import android.graphics.Color;

public enum SkillType
{

	purchasePrice("purchasePrice"), purchaseDistance("purchaseDistance"), experienceLimit("experienceLimit"), moneyLimit("moneyLimit"), experienceQuantityFound("experienceQuantityFound"), alliancePrice("alliancePrice");

	private String name;

	private SkillType(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
}
