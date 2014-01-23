/*=====================================================================*
| This file declares the following classes:
|    Skill.java
|
| Description of the class Skill.java :
| Represent a skill of the player.
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
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private SkillType	type;
	private int			level;

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

	/**
	 * Level up the level
	 */
	public void upgrade()
	{
		level++;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/**
	 * @return the type of the level
	 */
	public SkillType getType()
	{
		return type;
	}

	/**
	 * @return the description of the level, with this advantage
	 */
	public String getDescription()
	{
		return String.format(type.getDescription(), ((int) type.getExplicitValueSkills() * level));
	}

	/**
	 * @return the value of the advantage for the skill
	 */
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
