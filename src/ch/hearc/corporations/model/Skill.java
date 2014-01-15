/*=====================================================================*
| This file declares the following classes:
|    Skill.java
|
| Description of the class Skill.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

/**
 * @author Alexandre
 * 
 */
public class Skill
{
	/*------------------------------------------------------------------*\
	|*							Public Attributes						*|
	\*------------------------------------------------------------------*/

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static final int	SKILL_PURCHASE_PRICE			= 0;
	public static final int	SKILL_PURCHASE_DISTANCE			= 1;
	public static final int	SKILL_EXPERIENCE_LIMIT			= 2;
	public static final int	SKILL_MONEY_LIMIT				= 3;
	public static final int	SKILL_EXPERIENCE_QUANTITY_FOUND	= 4;
	public static final int	SKILL_ALLIANCE_PRICE			= 5;

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private SkillType		type;
	private int				level;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	public Skill(SkillType type, int level)
	{
		this.type = type;
		this.level = level;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public void upgrade()
	{
		level++;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	public SkillType getType()
	{
		return type;
	}

	public String getDescription()
	{
		return String.format(type.getDescription(), ((int) type.getExplicitValueSkills() * level));
	}

	public float getValue()
	{
		return type.getExplicitValueSkills() * level;
	}

	public int getLevel()
	{
		return level;
	}

	public int getUpdatePrice()
	{
		return (int) (Math.pow(level, 2));
	}

	/*------------------------------*\
	|*				Set				*|
	\*------------------------------*/

	public void setLevel(int level)
	{
		this.level = level;
	}

}
