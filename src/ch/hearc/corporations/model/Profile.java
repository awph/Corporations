/*=====================================================================*
| This file declares the following classes:
|    Profile.java
|
| Description of the class Profile.java :
| This class represent the profile of the player.
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

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	// identifier for check server side
	private String	identifier;
	private long	currentMoney;
	private long	currentRevenue;
	private long	tripMoneyEarned;
	private long	totalGain;
	private int		experiencePoints;
	private LatLng	home;
	private Skill[]	skills;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public Profile(String userId, int numberAllies, int numberTerritories, int rank, long currentMoney, long currentRevenue, long tripMoneyEarned, long totalGain, int experiencePoints, LatLng home,
			int purchasePriceSkillLevel, int purchaseDistanceSkillLevel, int experienceLimitSkillLevel, int moneyLimitSkillLevel, int experienceQuantityFoundSkillLevel, int alliancePriceSkillLevel)
	{
		super(userId, rank, numberAllies, numberTerritories, false);
		this.currentMoney = currentMoney;
		this.currentRevenue = currentRevenue;
		this.totalGain = totalGain;
		this.experiencePoints = experiencePoints;
		this.home = home;
		this.skills = new Skill[6];
		
		this.skills[SkillType.purchasePrice.getNumber()] = new Skill(SkillType.purchasePrice, purchasePriceSkillLevel);
		this.skills[SkillType.purchaseDistance.getNumber()] = new Skill(SkillType.purchaseDistance, purchaseDistanceSkillLevel);
		this.skills[SkillType.experienceLimit.getNumber()] = new Skill(SkillType.experienceLimit, experienceLimitSkillLevel);
		this.skills[SkillType.moneyLimit.getNumber()] = new Skill(SkillType.moneyLimit, moneyLimitSkillLevel);
		this.skills[SkillType.experienceQuantityFound.getNumber()] = new Skill(SkillType.experienceQuantityFound, experienceQuantityFoundSkillLevel);
		this.skills[SkillType.alliancePrice.getNumber()] = new Skill(SkillType.alliancePrice, alliancePriceSkillLevel);
	}

	public Profile()
	{
		this(null, 0, 0, 0, 0, 0, 0, 0, 0, null, 0, 0, 0, 0, 0, 0);
	}

	public Profile(Profile profile)
	{
		// We dont put the userid in constructor, because we know the name and
		// we dont need to fetch it again
		this(null, profile.numberAllies, profile.numberTerritories, profile.rank, profile.currentMoney, profile.currentRevenue, profile.tripMoneyEarned, profile.totalGain, profile.experiencePoints,
				new LatLng(profile.home.latitude, profile.home.longitude), profile.getSkill(SkillType.purchasePrice).getLevel(), profile.getSkill(SkillType.purchaseDistance).getLevel(), profile
						.getSkill(SkillType.experienceLimit).getLevel(), profile.getSkill(SkillType.moneyLimit).getLevel(), profile.getSkill(SkillType.experienceQuantityFound).getLevel(), profile
						.getSkill(SkillType.alliancePrice).getLevel());
		this.userId = profile.userId;
		this.name = profile.name;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	@Override
	public String toString()
	{
		return "Profile [identifier=" + identifier + ", currentMoney=" + currentMoney + ", totalGain=" + totalGain + ", experiencePoints=" + experiencePoints + ", home=" + home + ", skills="
				+ Arrays.toString(skills) + ", toString()=" + super.toString() + "]";
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the currentMoney
	 */
	public long getCurrentMoney()
	{
		return currentMoney;
	}

	/**
	 * @return the currentRevenue
	 */
	public long getCurrentRevenue()
	{
		return currentRevenue;
	}

	/**
	 * @return the totalGain
	 */
	public long getTotalGain()
	{
		return totalGain;
	}

	/**
	 * @return the experiencePoints
	 */
	public int getExperiencePoints()
	{
		return experiencePoints;
	}

	/**
	 * @return the money earned during trips
	 */
	public long getTripMoneyEarned()
	{
		return tripMoneyEarned;
	}

	/**
	 * @param skillType is the skill wanted
	 * @return the skill that correspond to the argument
	 */
	public Skill getSkill(SkillType skillType)
	{
		return skills[skillType.getNumber()];
	}

	/**
	 * @return the home location
	 */
	public LatLng getHome()
	{
		return home;
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	/**
	 * @param currentMoney
	 *            the currentMoney to set
	 */
	public void setCurrentMoney(long currentMoney)
	{
		this.currentMoney = currentMoney;
	}

	/**
	 * @param currentRevenue
	 *            the currentRevenue to set
	 */
	public void setCurrentRevenue(long currentRevenue)
	{
		this.currentRevenue = currentRevenue;
	}

	/**
	 * @param totalGain
	 *            the totalGain to set
	 */
	public void setTotalGain(long totalGain)
	{
		this.totalGain = totalGain;
	}

	/**
	 * @param experiencePoints
	 *            the experiencePoints to set
	 */
	public void setExperiencePoints(int experiencePoints)
	{
		this.experiencePoints = experiencePoints;
	}

	/**
	 * @param tripMoneyEarned
	 *            the tripMoneyEarned to set
	 */
	public void setTripMoneyEarned(long tripMoneyEarned)
	{
		this.tripMoneyEarned = tripMoneyEarned;
	}

	/**
	 * @param home
	 *            the home to set
	 */
	public void setHome(LatLng home)
	{
		this.home = home;
	}

	/**
	 * @param skills
	 *            the skills to set
	 */
	public void setSkills(Skill[] skills)
	{
		this.skills = skills;
	}

	/**
	 * Set the level of the skill wanted
	 * @param skillType is the skill to update
	 * @param level is the new level
	 */
	public void setSkillLevel(SkillType skillType, int level)
	{
		skills[skillType.getNumber()].setLevel(level);
	}

}
