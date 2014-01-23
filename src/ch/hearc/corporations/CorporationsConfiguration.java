/*=====================================================================*
| This file declares the following classes:
|    CorporationsConfiguration.java
|
| Description of the class CorporationsConfiguration.java :
| This class contain info for all the app.
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 27 nov. 2013
|
 *=====================================================================*/

package ch.hearc.corporations;

import android.graphics.drawable.GradientDrawable;

/**
 * @author Alexandre
 * 
 */
public final class CorporationsConfiguration
{
	public static final String				SERVER_PATH						= "https://corporation-perezapp.rhcloud.com/";
	public static final String				API_PATH						= SERVER_PATH + "api.php";
	
	public static final GradientDrawable	BACKGROUND_TRANSPARENT_FREE		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE3A3F44, 0xEE707A85 });
	public static final GradientDrawable	BACKGROUND_TRANSPARENT_ENEMY	= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEEDF5D07, 0xEEFF0000 });
	public static final GradientDrawable	BACKGROUND_TRANSPARENT_ALLY		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE2980B9, 0xEE54AEEB });
	public static final GradientDrawable	BACKGROUND_TRANSPARENT_OWN		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE399A48, 0xEE6BB471 });
	public static final GradientDrawable	BACKGROUND_OWN					= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xFF399A48, 0xFF6BB471 });
	public static final GradientDrawable	BACKGROUND_TRIP					= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xFFF2D121, 0xFFF6F53D });

}
