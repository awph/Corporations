/*=====================================================================*
| This file declares the following classes:
|    Trip.java
|
| Description of the class Trip.java :
|
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import java.util.Calendar;
import java.util.Date;


/**
 * @author Alexandre
 * 
 */
public class Trip
{
	private float	distance;
	private int		secondes;
	private Date	date;

	public Trip(float distance, int secondes)
	{
		this.distance = distance;
		this.secondes = secondes;
		this.date = Calendar.getInstance().getTime();
	}

	/**
	 * @return the distance
	 */
	public float getDistance()
	{
		return distance;
	}

	/**
	 * @return the secondes
	 */
	public int getTime()
	{
		return secondes;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	public int getExperienceEarned()
	{
		// TODO Auto-generated method stub

		return 0;
	}

	public int getMoneyEarned()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public void tripStart()
	{
		// TODO Auto-generated method stub

	}

	public void tripEnd()
	{
		// TODO Auto-generated method stub

	}

}
