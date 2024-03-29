/*=====================================================================*
| This file declares the following classes:
|    TerritoryInfoFragment.java
|
| Description of the class TerritoryInfoFragment.java :
| View class for display the info of the selected territory
|
| <p>Copyright : EIAJ, all rights reserved</p>
| @autor : Alexandre
| @version : 3 d�c. 2013
|
 *=====================================================================*/
package ch.hearc.corporations.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import ch.hearc.corporations.CorporationsConfiguration;
import ch.hearc.corporations.R;
import ch.hearc.corporations.Tools;
import ch.hearc.corporations.controller.AccountController;
import ch.hearc.corporations.model.Player;
import ch.hearc.corporations.model.PurchasableTerritory;
import ch.hearc.corporations.model.SkillType;
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
	private boolean									isCurrentTerritoryConnected;
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

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// Hide the view
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
				territory.setHighlighted(false);
				ft.hide(TerritoryInfoFragment.this);
				ft.commit();
				territory = null;
				displayed = false;
			}
		});

		return view;
	}

	/*------------------------------------------------------------------*\
	|*							Public Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Method used when need to update the territory info, change selected territory.
	 * @param territory the selected territory
	 * @param isConnected true if the territory is connected to ally or one of our
	 * @return true if the fragment need to be display
	 */
	public boolean updateTerritoryInfo(Territory territory, boolean isConnected)
	{
		isCurrentTerritoryConnected = isConnected;
		territory.setVisible(true);
		if (this.territory != null) this.territory.setHighlighted(false);
		if (!displayed || !territory.equals(this.territory))
		{
			displayed = true;
			this.territory = territory;
			loadInfo();
		}
		else
		{
			this.territory = null;
			displayed = false;
		}

		return displayed;
	}

	/*------------------------------------------------------------------*\
	|*							Private Methods							*|
	\*------------------------------------------------------------------*/

	/**
	 * Refresh view element
	 */
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
					+ String.format(getResources().getString(R.string.ally), (owner.isAlly() ? getResources().getString(R.string.yes) : getResources().getString(R.string.no))));
			infos.append("\n" + String.format(getResources().getString(R.string.revenue), Tools.formatMoney(territory.getRevenue())));
			if (territory instanceof PurchasableTerritory)
			{
				infos.append("\n" + String.format(getResources().getString(R.string.sale_price), Tools.formatMoney(((PurchasableTerritory) territory).getSalePrice())));
				if(!owner.isAlly())
					setBuyButton();
				else
					hideButton(firstButton);
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

	/**
	 * Hide the button passed in parameter
	 * @param button to hide
	 */
	private void hideButton(Button button)
	{
		button.setVisibility(View.GONE);
	}

	/**
	 * Show the button passed in parameter
	 * @param button to show
	 */
	private void showButton(Button button)
	{
		button.setVisibility(View.VISIBLE);
	}

	/**
	 * Set and update the ask for alliance button
	 * @param ally is true if it's a ally
	 */
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

	/**
	 * Set the change price button
	 */
	private void setChangePriceButton()
	{
		showButton(firstButton);
		firstButton.setText(getResources().getString(R.string.change_sale_price));
		firstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				AlertDialog changePriceAlert = new AlertDialog.Builder(getActivity()).create();

				changePriceAlert.setTitle(getActivity().getResources().getString(R.string.new_price_alert_dialog_title));
				changePriceAlert.setMessage(getActivity().getResources().getString(R.string.new_price_alert_dialog_message));

				final EditText input = new EditText(getActivity());

				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				changePriceAlert.setView(input);

				changePriceAlert.setButton(DialogInterface.BUTTON_POSITIVE, getActivity().getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						long newPrice = 0;
						try
						{
							newPrice = Long.parseLong(input.getText().toString());

							((PurchasableTerritory) territory).changePrice(newPrice, new Callback() {

								@Override
								public void update()
								{
									TerritoryInfoFragment.this.loadInfo();
								}
							});
						}
						catch (NumberFormatException e)
						{
							return;
						}
					}
				});

				changePriceAlert.setButton(DialogInterface.BUTTON_NEGATIVE, getActivity().getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// nothing
					}
				});

				changePriceAlert.show();
			}
		});
	}

	/**
	 * Set the capture button
	 */
	private void setCaptureButton()
	{
		showButton(firstButton);
		firstButton.setText(getResources().getString(R.string.capture));
		firstButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				Location currentLocation = ((MainActivity) getActivity()).getCurrentLocation();
				if (currentLocation == null)
				{
					Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.verify_gps_title), getActivity().getResources().getString(R.string.verify_gps_message));
					return;
				}
				if (!((SpecialTerritory) territory).capture(new Callback() {

					@Override
					public void update()
					{
						TerritoryInfoFragment.this.loadInfo();
					}
				}, currentLocation))
				{
					Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.not_in_zone_alert_dialog_title),
							getActivity().getResources().getString(R.string.not_in_zone_alert_dialog_message));
				}
			}
		});
	}

	/**
	 * Set the buy button, only if it price is > 0.
	 * If it's < 0 the territory can't be buy
	 */
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
					if (isCurrentTerritoryConnected)
					{
						if (!territory.isTooFar())
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
						else
						{
							Tools.showInfoAlertDialog(
									getActivity(),
									getActivity().getResources().getString(R.string.too_far_alert_dialog_title),
									String.format(getActivity().getResources().getString(R.string.too_far_alert_dialog_message),
											(int) AccountController.getInstance().getProfile().getSkill(SkillType.purchaseDistance).getValue()));
						}
					}
					else
					{
						Tools.showInfoAlertDialog(getActivity(), getActivity().getResources().getString(R.string.territory_not_connnected_alert_dialog_title),
								getActivity().getResources().getString(R.string.territory_not_connnected_alert_dialog_message));
					}
				}
			});
		}
		else
		{
			hideButton(firstButton);
		}
	}

	/**
	 * Set a gradient background 
	 * @param gradientDrawable the color
	 */
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
