package ch.hearc.corporations.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import ch.hearc.corporations.R;

import com.facebook.FacebookException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class LoginActivity extends Activity
{

	private String					TAG			= "LoginActivity";
	private Session.StatusCallback	callback	= new Session.StatusCallback() {
													@Override
													public void call(Session session, SessionState state, Exception exception)
													{
														onSessionStateChange(session, state, exception);
													}
												};
	private UiLifecycleHelper		uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setOnErrorListener(new OnErrorListener() {

			@Override
			public void onError(FacebookException error)
			{
				Log.i(TAG, "Error " + error.getMessage());
			}
		});
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception)
	{
		if (state.isOpened())
		{
			Log.i(TAG, "Logged in...");
			LoginActivity.this.finish();
		}
		else if (state.isClosed())
		{
			Log.i(TAG, "Logged out...");
		}
	}

	@Override
	public void onBackPressed()
	{
	}

	@Override
	public void onResume()
	{
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

}
