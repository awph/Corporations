package ch.hearc.corporations.model;

import com.google.android.gms.maps.model.LatLng;

public class MercatorProjection
{

	private double	x;
	private double	y;

	private MercatorProjection(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public static MercatorProjection fromLatLng(LatLng point)
	{
		double x = point.longitude;
		double y = Math.toDegrees(Math.log(Math.tan(Math.PI / 4 + Math.toRadians(point.latitude / 2))));
		return new MercatorProjection(x, y);
	}

	public LatLng getLatLng()
	{
		return new LatLng(x, y);
	}
}
