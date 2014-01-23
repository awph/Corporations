/*=====================================================================*
| This file declares the following classes:
|    Trip.java
|
| Description of the class Trip.java :
| This class contain all info for a trip.
| Implement methods for compute trip info receive from trip service. 
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 déc. 2013
|
 *=====================================================================*/

package ch.hearc.corporations.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.location.Location;
import android.util.Log;
import ch.hearc.corporations.controller.DataLoader;
import ch.hearc.corporations.controller.DataLoaderAdapter;
import ch.hearc.corporations.controller.Status;

/**
 * @author Alexandre
 * 
 */
public class Trip implements Serializable
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	/**
	 * Auto generated
	 */
	private static final long	serialVersionUID	= -4387390245062411595L;
	private float				distance;
	private long				secondes;
	private long				money;
	private int					experience;
	private Date				date;

	private long				startTime;
	private boolean				finished;
	private boolean				sent;											// Push
																				// to
																				// the
																				// server
	private List<Double>		latitudes;
	private List<Double>		longitudes;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

	/**
	 * Constructor used when trip is fetched from the server, and just need to stock info.
	 */
	public Trip(float distance, long secondes, long money, int experience, Date date)
	{
		this.finished = true;
		this.sent = true;
		this.distance = distance;
		this.secondes = secondes;
		this.money = money;
		this.experience = experience;
		this.date = date;
	}

	/**
	 * Constructor used when we need a new trip. And record id.
	 */
	public Trip()
	{
		this.date = Calendar.getInstance().getTime();
		this.startTime = System.currentTimeMillis();
		this.finished = false;
		this.sent = false;
		this.latitudes = new ArrayList<Double>();
		this.longitudes = new ArrayList<Double>();
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Add the last position to the trip (when the player is back home).
	 * And compute time.
	 * @param latitude the last latitude
	 * @param longitude the last longitude
	 */
	public void endTrip(double latitude, double longitude)
	{
		if (this.latitudes.size() <= 0 && this.longitudes.size() <= 0) return;
		addLocation(latitude, longitude);
		this.finished = true;
		long endTime = System.currentTimeMillis();
		this.secondes = (endTime - startTime) / 1000;
	}

	/**
	 * Add a checkpoint to the trip
	 * @param latitude
	 * @param longitude
	 */
	public void addLocation(double latitude, double longitude)
	{
		this.latitudes.add(latitude);
		this.longitudes.add(longitude);
		computeLastDelta();
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Compute the distance of the last to location and add it the total
	 */
	private void computeLastDelta()
	{
		if (this.latitudes.size() <= 1 && this.longitudes.size() <= 1) return;

		float[] results = new float[3];
		Location.distanceBetween(this.latitudes.get(this.latitudes.size() - 2), this.longitudes.get(this.longitudes.size() - 2), this.latitudes.get(this.latitudes.size() - 1),
				this.longitudes.get(this.longitudes.size() - 1), results);
		distance += results[0];
	}

	/*------------------------------*\
	|*				Get				*|
	\*------------------------------*/

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
	public long getTime()
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

	/**
	 * @return the experience points earned
	 */
	public int getExperienceEarned()
	{
		return experience;
	}

	/**
	 * @return the amount of money earned
	 */
	public long getMoneyEarned()
	{
		return money;
	}

	/*------------------------------*\
	|*				Is				*|
	\*------------------------------*/

	/**
	 * @return true if this trip is finished
	 */
	public boolean isFinished()
	{
		return finished;
	}

	/**
	 * @return true if this trip is sent to the server
	 */
	public boolean isSent()
	{
		return sent;
	}

	/**
	 * /!\ This method wait for the server response. (Sync)
	 * 
	 * Send the trip to the server in current thread. Need to be called by a non-main thread.
	 * We need to wait for the server response for remove the trip on the device
	 */
	public void send()
	{
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run()
			{
				DataLoader.getInstance().uploadTrip(Trip.this, new DataLoaderAdapter() {

					@Override
					public void tripUploaded(Status status)
					{
						if (status == Status.OK) Trip.this.sent = true;
					}
				});
				while (!Trip.this.sent)
					;
			}
		});
		thread.start();
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			Log.e("Trip -> send fail", e.toString());
		}
	}
}
