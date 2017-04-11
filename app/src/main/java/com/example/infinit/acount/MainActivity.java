package com.example.infinit.acount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.infinit.acount.adapter.MyHomeAdapter;
import com.example.infinit.acount.jsonresponce.JSONResponseHome;
import com.example.infinit.acount.model.DataGetmodel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MainActivity";

    private static final int REQ_SIGNUP = 1;
    private List<DataGetmodel> rowItems2;
    ProgressDialog pd;
    ArrayList<String> orderNumber = new ArrayList<String>();
    ArrayList<String> orderDescription = new ArrayList<String>();
    ArrayList<String> orderQty = new ArrayList<String>();
    ArrayList<String> orderAmount = new ArrayList<String>();
    ArrayList<String> orderID = new ArrayList<String>();
    MyHomeAdapter mAdapter;

    String r;
    String rt;
    private AccountManager mAccountManager;
    private AuthPreferences mAuthPreferences;
    private String authToken;

    private TextView text1;
    private TextView text2;
    private TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        text1 = (TextView) findViewById(android.R.id.text1);
//        text2 = (TextView) findViewById(android.R.id.text2);
//        text3 = (TextView) findViewById(R.id.text3);

        authToken = null;
        mAuthPreferences = new AuthPreferences(this);
        mAccountManager = AccountManager.get(this);

        // Ask for an auth token
        mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
       //Bundle extras = getIntent().getExtras();
       String userID = sp.getString("userID","0");
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.setCancelable(true);
        pd.show();
        rowItems2 = new ArrayList<DataGetmodel>();
        getOrderData(userID);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyHomeAdapter(this, rowItems2);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void getOrderData(final String agent_id) {


        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
                request.addHeader("Authorization", "Bearer "+sp.getString("token","0"));
                //request.addHeader("Content-Type", "application/json");
            }
        };

        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(RestInterface.url).setRequestInterceptor(requestInterceptor).build();
        final RestInterface restInterface = adapter.create(RestInterface.class);
        restInterface.getData(agent_id, new Callback<JSONResponseHome>() {
            @Override
            public void success(JSONResponseHome jsResponce, Response response) {
                if (jsResponce.getDataGetmodel() != null) {
                    DataGetmodel[] dgm = jsResponce.getDataGetmodel();
                    if (dgm != null && dgm.length > 0) {
                        for (DataGetmodel qm : dgm) {
                            orderNumber.add(qm.getOrder_number());
                            orderDescription.add(qm.getOrder_desc());
                            orderQty.add(qm.getOrder_qty());
                            orderAmount.add(qm.getOrder_amount());
                            orderID.add(qm.getId());
                            rowItems2.add(qm);



                            if (pd != null)
                                if (pd.isShowing())
                                    pd.dismiss();
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "no data available", Snackbar.LENGTH_LONG)
                                .setAction("back", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .setActionTextColor(Color.RED)
                                .show();
                        if (pd != null)
                            if (pd.isShowing())
                                pd.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong.no data available", Toast.LENGTH_SHORT).show();
                    if (pd != null)
                        if (pd.isShowing())
                            pd.dismiss();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "Failed,check your network", Toast.LENGTH_SHORT).show();
                if (pd != null)
                    if (pd.isShowing())
                        pd.dismiss();
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private class GetAuthTokenCallback implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Bundle bundle;

            try {
                bundle = result.getResult();

                final Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (null != intent) {
                    startActivityForResult(intent, REQ_SIGNUP);
                } else {
                    authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    final String accountName = bundle.getString(AccountManager.KEY_ACCOUNT_NAME);

                    // Save session username & auth token
                    mAuthPreferences.setAuthToken(authToken);
                    mAuthPreferences.setUsername(accountName);

                    text1.setText("Retrieved auth token: " + authToken);
                    text2.setText("Saved account name: " + mAuthPreferences.getAccountName());
                    text3.setText("Saved auth token: " + mAuthPreferences.getAuthToken());

                    // If the logged account didn't exist, we need to create it on the device
                    Account account = AccountUtils.getAccount(MainActivity.this, accountName);
                    if (null == account) {
                        account = new Account(accountName, AccountUtils.ACCOUNT_TYPE);
                        mAccountManager.addAccountExplicitly(account, bundle.getString(LoginActivity.PARAM_USER_PASSWORD), null);
                        mAccountManager.setAuthToken(account, AccountUtils.AUTH_TOKEN_TYPE, authToken);
                    }
                }
            } catch (OperationCanceledException e) {
                // If signup was cancelled, force activity termination
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close_session:
                // Clear session and ask for new auth token
                mAccountManager.invalidateAuthToken(AccountUtils.ACCOUNT_TYPE, authToken);
                mAuthPreferences.setAuthToken(null);
                mAuthPreferences.setUsername(null);
                mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            mAccountManager.invalidateAuthToken(AccountUtils.ACCOUNT_TYPE, authToken);
            mAuthPreferences.setAuthToken(null);
            mAuthPreferences.setUsername(null);
            mAccountManager.getAuthTokenByFeatures(AccountUtils.ACCOUNT_TYPE, AccountUtils.AUTH_TOKEN_TYPE, null, this, null, null, new GetAuthTokenCallback(), null);
        }
        /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
