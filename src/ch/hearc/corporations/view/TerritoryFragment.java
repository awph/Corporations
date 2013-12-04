package ch.hearc.corporations.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ch.hearc.corporations.R;

public class TerritoryFragment extends Fragment
{
	public static final String						TAG	= TerritoryFragment.class.getSimpleName();

	private RoundedFacebookProfilePictureImageView	profileView;
	private View									view;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.territory_fragment, container, false);
		GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] { 0xEE5aab63, 0xEE379946 });
		// gd.setCornerRadius(0f);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
			view.setBackgroundDrawable(gd);
		else
			view.setBackground(gd);
		profileView = (RoundedFacebookProfilePictureImageView) view.findViewById(R.id.profilePicture);
		profileView.setBorderColor(Color.WHITE);
		profileView.setBorderWidth(1);
		profileView.setProfileId("thedarkmammouth");// sebyx";//diego.antognini";//thedarkmammouth";

		return view;
	}
}
