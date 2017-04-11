package com.example.infinit.acount;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infinit.acount.model.LoginModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.example.infinit.acount.R.id.secondBar;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends AccountAuthenticatorActivity {

	public static final String ARG_ACCOUNT_TYPE = "accountType";
	public static final String ARG_AUTH_TOKEN_TYPE = "authTokenType";
	public static final String ARG_IS_ADDING_NEW_ACCOUNT = "isAddingNewAccount";
	public static final String PARAM_USER_PASSWORD = "password";
Context c;
	private AccountManager mAccountManager;

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	//private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	RequestQueue queue ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mAccountManager = AccountManager.get(this);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
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

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		if (null != mEmail) {
			if (!mEmail.isEmpty()) {
				mPasswordView.requestFocus();
			}
		}
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
//		if (mAuthTask != null) {
//			return;
//		}

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
		}
//		} else if (mPassword.length() < 4) {
//			mPasswordView.setError(getString(R.string.error_invalid_password));
//			focusView = mPasswordView;
//			cancel = true;
//		}

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
			final String username = mEmailView.getText().toString();
			final String password = mPasswordView.getText().toString();
//			mAuthTask = new UserLoginTask(username,password);
//			mAuthTask.execute();

          queue= Volley.newRequestQueue(this);
			StringRequest postRequest = new StringRequest(Request.Method.POST, "http://beforelive.in/dev/dealer/public/login", new com.android.volley.Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					try {
						//Do it with this it will work
						JSONObject person = new JSONObject(response);
						String id = person.getString("id");
						String type_id = person.getString("type_id");

						String authToken = null;
						SharedPreferences sharedpreferences = getSharedPreferences("loginSP", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.clear();
						editor.putString("user", id);
						editor.putString("type_id", type_id);
						editor.commit();
						if (type_id.equals("1")) {
							SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
							SharedPreferences.Editor ed = sp.edit();
							ed.putString("username", username);
							ed.putString("password", password);
							ed.putString("userID", id);
							ed.commit();
							queue= Volley.newRequestQueue(getApplicationContext());
							StringRequest postRequest = new StringRequest(Request.Method.POST, "http://beforelive.in/dev/dealer/public/oauth/token", new com.android.volley.Response.Listener<String>() {
								@Override
								public void onResponse(String response) {

									JSONObject person2 = null;
									try {
										person2 = new JSONObject(response);
										String token = person2.getString("access_token");
										String tokenrefresh = person2.getString("refresh_token");
										String tokenexpire = person2.getString("expires_in");
										SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
										SharedPreferences.Editor ed = sp.edit();
										ed.putString("token", token);
										ed.putString("tokenexpire", tokenexpire);
										ed.putString("tokenrefresh", tokenrefresh);
										ed.commit();


									} catch (JSONException e) {
										e.printStackTrace();
									}



								}
							},new com.android.volley.Response.ErrorListener()
							{
								@Override
								public void onErrorResponse(VolleyError error) {
									// error

									showProgress(false);
									Log.d("Error.Response", String.valueOf(error));
								}
							}
							) {
								@Override
								protected Map<String, String> getParams()
								{
									Map<String, String> params = new HashMap<String, String>();
									params.put("username", username);
									params.put("password", password);
									params.put("client_secret", "anchupass");
									params.put("client_id", "anchuclient");
									params.put("grant_type", "password");

									return params;
								}
							};
							queue.add(postRequest);



							Intent res = new Intent();
							//authToken = AccountUtils.mServerAuthenticator.signIn(mEmail, mPassword);
							res.putExtra(AccountManager.KEY_ACCOUNT_NAME, mEmail);
							res.putExtra(AccountManager.KEY_ACCOUNT_TYPE, AccountUtils.ACCOUNT_TYPE);
							res.putExtra(AccountManager.KEY_AUTHTOKEN, sp.getString("token","0"));
							res.putExtra(PARAM_USER_PASSWORD, mPassword);
							if (null == AccountManager.KEY_AUTHTOKEN) {
								mPasswordView.setError(getString(R.string.error_incorrect_password));
								mPasswordView.requestFocus();
							} else {
								finishLogin(res);
							}
						}


					} catch (JSONException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}

//					Toast.makeText(c,"success",Toast.LENGTH_LONG).show();

				}
			},new com.android.volley.Response.ErrorListener()
			{
				@Override
				public void onErrorResponse(VolleyError error) {
					// error
//					Toast.makeText(c,"Wrong user/password",Toast.LENGTH_LONG).show();
				showProgress(false);
						Log.d("Error.Response", String.valueOf(error));
				}
			}
			) {
				@Override
				protected Map<String, String> getParams()
				{
					Map<String, String> params = new HashMap<String, String>();
					params.put("username", username);
					params.put("password", password);

					return params;
				}
			};
			queue.add(postRequest);







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












	@Override
	public void onBackPressed() {
		setResult(AccountAuthenticatorActivity.RESULT_CANCELED);
		super.onBackPressed();
	}

	private void finishLogin(Intent intent) {
		final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
		final String accountPassword = intent.getStringExtra(PARAM_USER_PASSWORD);
		final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
		String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);

		if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
			// Creating the account on the device and setting the auth token we got
			// (Not setting the auth token will cause another call to the server to authenticate the user)
			mAccountManager.addAccountExplicitly(account, accountPassword, null);
			mAccountManager.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken);
		} else {
			mAccountManager.setPassword(account, accountPassword);
		}

		setAccountAuthenticatorResult(intent.getExtras());
		setResult(AccountAuthenticatorActivity.RESULT_OK, intent);

		finish();
	}


}
