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

}
