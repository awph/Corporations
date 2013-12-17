package ch.hearc.corporations.view;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import ch.hearc.corporations.R;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;

public class TerritoryInfoFragment extends Fragment
{
	public static final String						TAG					= TerritoryInfoFragment.class.getSimpleName();
	private static final GradientDrawable			BACKGROUND_FREE		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE3A3F44, 0xEE707A85 });
	private static final GradientDrawable			BACKGROUND_ENEMY	= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEEDF5D07, 0xEEFF0000 });
	private static final GradientDrawable			BACKGROUND_ALLY		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE2980B9, 0xEE54AEEB });
	private static final GradientDrawable			BACKGROUND_OWN		= new GradientDrawable(GradientDrawable.Orientation.BR_TL, new int[] { 0xEE399A48, 0xEE6BB471 });

	private RoundedFacebookProfilePictureImageView	profileView;
	private TextView								infoTextView;
	private Button									firstButton;
	private Button									secondButton;
	private View									view;
	private Territory								territory;
	private boolean									displayed;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		view = inflater.inflate(R.layout.territory_info_fragment, container, false);
		infoTextView = (TextView) view.findViewById(R.id.info_text_territory_info);
		firstButton = (Button) view.findViewById(R.id.first_button_territory_info);
		secondButton = (Button) view.findViewById(R.id.second_button_territory_info);
		profileView = (RoundedFacebookProfilePictureImageView) view.findViewById(R.id.player_image_territory_info);
		profileView.setBorderColor(Color.WHITE);
		profileView.setBorderWidth(1);
		profileView.setProfileId("thedarkmammouth");// sebyx";//diego.antognini";//thedarkmammouth";

		return view;
	}

	public boolean updateTerritoryInfo(Territory territory)
	{
		if (!displayed || !territory.equals(this.territory))
		{
			displayed = true;
			this.territory = territory;
			loadInfo();
		}
		else
			displayed = false;

		return displayed;
	}

	private void loadInfo()
	{
		StringBuilder infos = new StringBuilder();
		if (territory.getOwner() == null)
		{
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("Sale price : $" + ((PurchasableTerritory) territory).getSalePrice());
				setBuyButton();
				hideButton(secondButton);
			}
			else
			{
				infos.append("Revenue : $" + territory.getRevenue());
				setCaptureButton();
				hideButton(secondButton);
			}

			setBackground(BACKGROUND_FREE);
		}
		else if (territory.getOwner().getUserID().equals(AccountController.getInstance().getFacebookID()))
		{
			infos.append("Revenue: $" + ((PurchasableTerritory) territory).getRevenue());
			infos.append("\nTotal gain: $" + ((PurchasableTerritory) territory).getTotalGain());
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("\nPurchasing price: $" + ((PurchasableTerritory) territory).getPurchasingPrice());
				infos.append("\nSale price: $" + ((PurchasableTerritory) territory).getSalePrice());
				setChangePriceButton();
				hideButton(secondButton);
			}
			else
			{
				hideButton(firstButton);
				hideButton(secondButton);
			}

			setBackground(BACKGROUND_OWN);
		}
		else
		{
			infos.append("Owner: " + territory.getOwner().getName());
			infos.append("\nAlly: " + (territory.getOwner().isAlly() ? "Yes" : "No"));
			infos.append("\nRevenue: $" + territory.getRevenue());
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("\nSale price: $" + ((PurchasableTerritory) territory).getSalePrice());
				setBuyButton();
			}
			else
			{
				hideButton(firstButton);
				hideButton(secondButton);
			}
			if (territory.getOwner().isAlly())
			{
				setBackground(BACKGROUND_ALLY);
				setAskAllianceButton(true);
			}
			else
			{
				setBackground(BACKGROUND_ENEMY);
				setAskAllianceButton(false);
			}
		}
		infoTextView.setText(infos);
	}

	private void hideButton(Button button)
	{
		button.setVisibility(View.INVISIBLE);
	}

	private void showButton(Button button)
	{
		button.setVisibility(View.VISIBLE);
	}

	private void setAskAllianceButton(final boolean ally)
	{
		showButton(secondButton);
		secondButton.setText((ally ? "Revoke alliance" : "Ask alliance"));
		secondButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				if (ally)
					territory.getOwner().askAlliance();
				else
					territory.getOwner().revokeAlliance();
			}
		});
	}

	private void setChangePriceButton()
	{
		showButton(firstButton);
		firstButton.setText("Change sale price");
		firstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				((PurchasableTerritory) territory).changePrice();
			}
		});
	}

	private void setCaptureButton()
	{
		showButton(firstButton);
		firstButton.setText("Capture");
		firstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				((SpecialTerritory) territory).capture();
			}
		});
	}

	private void setBuyButton()
	{
		if (((PurchasableTerritory) territory).getSalePrice() > 0)
		{
			showButton(firstButton);
			firstButton.setText("Buy");
			firstButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0)
				{
					((PurchasableTerritory) territory).buy();
				}
			});
		}
		else
		{
			hideButton(firstButton);
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setBackground(GradientDrawable gradientDrawable)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
			view.setBackgroundDrawable(gradientDrawable);
		else
			view.setBackground(gradientDrawable);
	}
}
