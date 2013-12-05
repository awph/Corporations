/*=====================================================================*
| This file declares the following classes:
|    Profile.java
|
| Description of the class Profile.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import java.util.Arrays;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Alexandre
 * 
 */
public class Profile extends Player
{
	private String	identifier;
	private int		currentMoney;
	private int		totalGain;
	private int		experiencePoints;
	private LatLng	home;
	private Skill[]	skills;

	public Profile(String userID, int numberAllies, int numberTerritories, int rank, int currentMoney, int currentRevenue, int totalGain, int experiencePoints, LatLng home,
			int purchasePriceSkillLevel, int purchaseDistanceSkillLevel, int experienceLimitSkillLevel, int moneyLimitSkillLevel, int experienceQuantityFoundSkillLevel, int alliancePriceSkillLevel)
	{
		super(userID, rank, numberAllies, numberTerritories, false);
		this.currentMoney = currentMoney;
		this.totalGain = totalGain;
		this.experiencePoints = experiencePoints;
		this.home = home;
		this.skills = new Skill[6];
		this.skills[SKILL_PURCHASE_PRICE] = new Skill(SkillType.purchasePrice, purchasePriceSkillLevel);
		this.skills[SKILL_PURCHASE_DISTANCE] = new Skill(SkillType.purchaseDistance, purchaseDistanceSkillLevel);
		this.skills[SKILL_EXPERIENCE_LIMIT] = new Skill(SkillType.experienceLimit, experienceLimitSkillLevel);
		this.skills[SKILL_MONEY_LIMIT] = new Skill(SkillType.moneyLimit, moneyLimitSkillLevel);
		this.skills[SKILL_EXPERIENCE_QUANTITY_FOUND] = new Skill(SkillType.experienceQuantityFound, experienceQuantityFoundSkillLevel);
		this.skills[SKILL_ALLIANCE_PRICE] = new Skill(SkillType.alliancePrice, alliancePriceSkillLevel);
	}
	
	
	
	@Override
	public String toString()
	{
		return "Profile [identifier=" + identifier + ", currentMoney=" + currentMoney + ", totalGain=" + totalGain + ", experiencePoints=" + experiencePoints + ", home=" + home + ", skills="
				+ Arrays.toString(skills) + ", toString()=" + super.toString() + "]";
	}

	public LatLng getHome()
	{
		return home;
	}



	private static final int	SKILL_PURCHASE_PRICE			= 0;
	private static final int	SKILL_PURCHASE_DISTANCE			= 1;
	private static final int	SKILL_EXPERIENCE_LIMIT			= 2;
	private static final int	SKILL_MONEY_LIMIT				= 3;
	private static final int	SKILL_EXPERIENCE_QUANTITY_FOUND	= 4;
	private static final int	SKILL_ALLIANCE_PRICE			= 5;
}
