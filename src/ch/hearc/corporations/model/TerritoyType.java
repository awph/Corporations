package ch.hearc.corporations.model;

import android.graphics.Color;

public enum TerritoyType
{
	Mine(Color.argb(100, 57, 154, 72)), Ally(Color.argb(100, 41, 128, 185)), Enemy(Color.argb(100, 255, 0, 0)), Free(Color.argb(0, 0, 0, 0));

	private int	color;

	private TerritoyType(int color)
	{
		this.color = color;
	}

	public int getColor()
	{
		return color;
	}
}
