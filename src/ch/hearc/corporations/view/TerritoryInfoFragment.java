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
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SpecialTerritory;
import ch.hearc.corporations.model.Territory;

public class TerritoryInfoFragment extends Fragment
{

	/*------------------------------------------------------------------*\
	|*							Private Attributes						*|
	\*------------------------------------------------------------------*/

	private RoundedFacebookProfilePictureImageView	profileView;
	private TextView								infoTextView;
	private Button									firstButton;
	private Button									secondButton;
	private View									view;
	private Territory								territory;
	private boolean									displayed;

	/*------------------------------*\
	|*			  Static			*|
	\*------------------------------*/

	public static final String						TAG	= TerritoryInfoFragment.class.getSimpleName();

	/*------------------------------*\
	|*			  Interface			*|
	\*------------------------------*/

	public interface Callback
	{
		void update();
	}

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

		return view;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	public boolean updateTerritoryInfo(Territory territory)
	{
		territory.setVisible(true);
		if (this.territory != null) this.territory.setHighlighted(false);
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

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	private void loadInfo()
	{
		Player owner = territory.getOwner();
		if (owner != null)
		{
			profileView.setProfileId(owner.getUserId());
			profileView.setVisibility(View.VISIBLE);
		}
		else
			profileView.setVisibility(View.GONE);
		StringBuilder infos = new StringBuilder();
		if (owner == null)
		{
			if (territory instanceof PurchasableTerritory)
			{
				infos.append(String.format(getResources().getString(R.string.sale_price), Tools.formatMoney(((PurchasableTerritory) territory).getSalePrice())));
				setBuyButton();
				hideButton(secondButton);
			}
			else
			{
				infos.append(String.format(getResources().getString(R.string.revenue), Tools.formatMoney(territory.getRevenue())));
				setCaptureButton();
				hideButton(secondButton);
			}

			setBackground(CorporationsConfiguration.BACKGROUND_TRANSPARENT_FREE);
		}
		else if (owner.getUserId().equals(AccountController.getInstance().getFacebookID()))
		{
			infos.append(String.format(getResources().getString(R.string.revenue), Tools.formatMoney(territory.getRevenue())));
			infos.append("\n" + String.format(getResources().getString(R.string.total_gain), Tools.formatMoney(territory.getTotalGain())));
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("\n" + String.format(getResources().getString(R.string.purchasing_price), Tools.formatMoney(((PurchasableTerritory) territory).getPurchasingPrice())));
				infos.append("\n" + String.format(getResources().getString(R.string.sale_price), Tools.formatMoney(((PurchasableTerritory) territory).getSalePrice())));
				setChangePriceButton();
				hideButton(secondButton);
			}
			else
			{
				hideButton(firstButton);
				hideButton(secondButton);
			}

			setBackground(CorporationsConfiguration.BACKGROUND_TRANSPARENT_OWN);
		}
		else
		{
			infos.append(String.format(getResources().getString(R.string.owner), owner.getName()));
			infos.append("\n"
					+ String.format(getResources().getString(R.string.ally), (owner.isAlly() ? getResources().getString(android.R.string.yes) : getResources().getString(android.R.string.no))));
			infos.append("\n" + String.format(getResources().getString(R.string.revenue), Tools.formatMoney(territory.getRevenue())));
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("\n" + String.format(getResources().getString(R.string.sale_price), Tools.formatMoney(((PurchasableTerritory) territory).getSalePrice())));
				setBuyButton();
			}
			else
			{
				setCaptureButton();
			}
			if (owner.isAlly())
			{
				setBackground(CorporationsConfiguration.BACKGROUND_TRANSPARENT_ALLY);
				setAskAllianceButton(true);
			}
			else
			{
				setBackground(CorporationsConfiguration.BACKGROUND_TRANSPARENT_ENEMY);
				setAskAllianceButton(false);
			}
		}
		infoTextView.setText(infos);
	}

	private void hideButton(Button button)
	{
		button.setVisibility(View.GONE);
	}

	private void showButton(Button button)
	{
		button.setVisibility(View.VISIBLE);
	}

	private void setAskAllianceButton(final boolean ally)
	{
		showButton(secondButton);
		secondButton.setText((ally ? getResources().getString(R.string.revoke_alliance) : getResources().getString(R.string.ask_alliance)));
		secondButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				territory.getOwner().updateAlliance(new Callback() {

					@Override
					public void update()
					{
						TerritoryInfoFragment.this.loadInfo();
					}
				});
			}
		});
	}

	private void setChangePriceButton()
	{
		showButton(firstButton);
		firstButton.setText(getResources().getString(R.string.change_sale_price));
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
		firstButton.setText(getResources().getString(R.string.capture));
		firstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				if (!((SpecialTerritory) territory).capture(new Callback() {

					@Override
					public void update()
					{
						TerritoryInfoFragment.this.loadInfo();
					}
				}))
				{
					Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.not_in_zone_alert_dialog_title),
							getActivity().getResources().getString(R.string.not_in_zone_alert_dialog_message));
				}
			}
		});
	}

	private void setBuyButton()
	{
		if (((PurchasableTerritory) territory).getSalePrice() > 0)
		{
			showButton(firstButton);
			firstButton.setText(getResources().getString(R.string.buy));
			firstButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0)
				{
					if (!((PurchasableTerritory) territory).buy(new Callback() {

						@Override
						public void update()
						{
							TerritoryInfoFragment.this.loadInfo();
						}
					}))
					{
						Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.not_enough_money_alert_dialog_title),
								getActivity().getResources().getString(R.string.not_enough_money_alert_dialog_message));
					}
				}
			});
		}
		else
		{
			hideButton(firstButton);
		}
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setBackground(GradientDrawable gradientDrawable)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
			view.setBackgroundDrawable(gradientDrawable);
		else
			view.setBackground(gradientDrawable);
	}
}
