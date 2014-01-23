/*=====================================================================*
| This file declares the following enum:
|    TerritoyType.java
|
| Description of the emum TerritoyType.java :
| This enum contains the different type of territory. Each type has a different color.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import android.graphics.Color;

public enum TerritoyType
{
	Mine(Color.argb(100, 57, 154, 72)), Ally(Color.argb(100, 41, 128, 185)), Enemy(Color.argb(100, 255, 0, 0)), Free(Color.argb(0, 0, 0, 0));

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private int					color;
	private static final int	SPECIAL_ALPHA_OFFSET	= 0x55000000;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	private TerritoyType(int color)
	{
		this.color = color;
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

	/** 
	 * @param isSpecial true if it's a special territory
	 * @return the color of the territory, the alpha is bigger if it's a special one.
	 */
	public int getColor(boolean isSpecial)
	{
		return color + (isSpecial ? SPECIAL_ALPHA_OFFSET : 0);
	}
}
