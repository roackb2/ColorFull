package ntu.im.colorfull;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	
	private static final int VerifySuccess = 1, VerifyFail = 2, CreateNewAccount = 3, ServerError = 4, ConnectionError = 5;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private String hashedPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private TextView errorText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);
		errorText = (TextView) findViewById(R.id.errTxt);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(isNetworkAvailable())
						{
							attemptLogin();
						}
						else
							Toast.makeText(LoginActivity.this, "Network not available", Toast.LENGTH_LONG).show();					
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			
			hashedPassword = String.valueOf(mPassword.hashCode());
			String concated = mEmail + ":" + hashedPassword;
			
			mAuthTask = new UserLoginTask();
			mAuthTask.execute(concated);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<String, Void, String[]> {
		//private String[] result;
		
		
		@Override
		protected String[] doInBackground(String... concated) 
		{
			// TODO: attempt authentication against a network service.
			final String url = "http://210.61.27.43/userVerify/verify.php";
			HttpPost postRequest = new HttpPost(url);
			HttpResponse postResponse = null;
			HttpClient client = new DefaultHttpClient();
			MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
			String response = null;
			
			String[] pieces = concated[0].split(":");
			multipartEntity.addTextBody("email", pieces[0]);
			multipartEntity.addTextBody("hashcode", pieces[1]);
			postRequest.setEntity(multipartEntity.build());
			String[] result = new String[2];
			
			try 
			{
				postResponse = client.execute(postRequest);
			} 
			catch (ClientProtocolException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
											
			if(postResponse.getStatusLine().getStatusCode() == 200)
			{
				try 
				{
					response = EntityUtils.toString(postResponse.getEntity());
				} 
				catch (ParseException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				if(response.equals("success"))
				{
					result[0] = "VerifySuccess";
					result[1] = response;
					
				}
				else if(response.equals("fail"))
				{
					result[0] = "VerifyFail";
					result[1] = response;
				}
				else if(response.equals("createAccount"))
				{
					result[0] = "CreateNewAccount";
					result[1] = response;
				}
				else
				{
					result[0] = "ServerError";
					result[1] = response;
				}
				
			}
			else
			{
				response = String.valueOf(postResponse.getStatusLine().getStatusCode());
				result[0] = "ConnectionError";
				result[1] = response;
			}
/*
			try 
			{
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) 
			{
				return false;
			}

			for (String credential : DUMMY_CREDENTIALS) 
			{
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) 
				{
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}
			}
*/
			// TODO: register the new account here.
			return result;
		}

		@Override
		protected void onPostExecute(final String[] status) {
			mAuthTask = null;
			showProgress(false);

			if (status[0] == "VerifySuccess") 
			{
				Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_LONG).show();
				MainActivity.login();
				String[] pieces = mEmail.split("@");
				MainActivity.setUserId(pieces[0]); 
				finish();
			} 
			else if(status[0] == "VerifyFail")
			{
				Toast.makeText(LoginActivity.this, "Fail: Wrong account or wrong password", Toast.LENGTH_LONG).show();
				errorText.setText("Hint message: Wrong account or wrong password");
			}
			else if(status[0] == "CreateNewAccount")
			{
				Toast.makeText(LoginActivity.this, "New account created", Toast.LENGTH_LONG).show();
				errorText.setText("Hint message: New account created");
				MainActivity.login();
			}
			else if(status[0] == "ServerError")
			{
				Toast.makeText(LoginActivity.this, "Server error: " + status[1], Toast.LENGTH_LONG).show();
				errorText.setText("Hint message: " + status[1]);
			}
			else if(status[0] == "ConnectionError")
			{
				Toast.makeText(LoginActivity.this, "Connection error: " + status[1], Toast.LENGTH_LONG).show();
				errorText.setText("Hint message: " + status[1]);
			}
			else
			{
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
			
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	public boolean isNetworkAvailable()
	{
		ConnectivityManager netManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = netManager.getActiveNetworkInfo();
		
		return netInfo != null && netInfo.isConnected();
	}
}
