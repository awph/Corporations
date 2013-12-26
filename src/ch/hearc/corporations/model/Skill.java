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
	public static final int	SKILL_PURCHASE_PRICE			= 0;
	public static final int	SKILL_PURCHASE_DISTANCE			= 1;
	public static final int	SKILL_EXPERIENCE_LIMIT			= 2;
	public static final int	SKILL_MONEY_LIMIT				= 3;
	public static final int	SKILL_EXPERIENCE_QUANTITY_FOUND	= 4;
	public static final int	SKILL_ALLIANCE_PRICE			= 5;
	
	private SkillType	type;
	private int			level;

	public Skill(SkillType type, int level)
	{
		this.type = type;
		this.level = level;
	}

	public SkillType getType()
	{
		return type;
	}

	public int getLevel()
	{
		return level;
	}

	public Object getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPrice()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void upgrade()
	{
		level++;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

}
