package com.example.infinit.acount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.infinit.acount.adapter.MyInvoAdapter;
import com.example.infinit.acount.jsonresponce.JSONResponceGetSpinner;
import com.example.infinit.acount.jsonresponce.JSONResponseInvo;
import com.example.infinit.acount.model.DataGetPaymentmodel;
import com.example.infinit.acount.model.DataGetSpinner;
import com.example.infinit.acount.model.InsertPaymentModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class InvoTrackerActivity extends AppCompatActivity {


    ArrayList<String> payment_amount = new ArrayList<String>();
    ArrayList<String> payment_date = new ArrayList<String>();
    ArrayList<String> type_id = new ArrayList<String>();
    ArrayList<String> payment_status = new ArrayList<String>();
    ProgressDialog pd;
    Spinner spinner;
    private List<DataGetPaymentmodel> row;
    long total = 0;
    TextView totalSum;
    List<String> categories = new ArrayList<String>();
    String orderID;
    MyInvoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invo_tracker);
        getSupportActionBar().hide();


//        SharedPreferences prefs = this.getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
//        String storedUsername = prefs.getString("username", "a");
//        String storedPassword = prefs.getString("password", "b"); //return nothing if no pass saved
//        String userID = prefs.getString("userID", "");


        Bundle extras = getIntent().getExtras();
        orderID = extras.getString("orderID");
        String amount = extras.getString("amount");
        TextView totalAmount = (TextView) findViewById(R.id.d);
        totalSum = (TextView) findViewById(R.id.c);
        totalAmount.setText(amount);
        row = new ArrayList<DataGetPaymentmodel>();

        getPaymentData(orderID);
        getSpinnerData();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyInvoAdapter(this, row);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        spinner = (Spinner) findViewById(R.id.spinner);
        categories.add("Choose payment status");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    private void getPaymentData(String orderID) {
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.setCancelable(true);
        pd.show();

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
                request.addHeader("Authorization", "Bearer "+sp.getString("token","0"));
              //  request.addHeader("Content-Type", "application/json");
            }
        };
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(RestInterface.url).setRequestInterceptor(requestInterceptor).build();
        final RestInterface restInterface = adapter.create(RestInterface.class);
        restInterface.getPaymentDATA(orderID, new Callback<JSONResponseInvo>() {
            @Override
            public void success(JSONResponseInvo jsonResponseInvo, Response response) {
                if (jsonResponseInvo.getData() != null) {
                    DataGetPaymentmodel[] dgm = jsonResponseInvo.getData();
                    if (dgm != null && dgm.length > 0) {
                        for (DataGetPaymentmodel qm : dgm) {
                            payment_amount.add(qm.getPayment_amount());
                            payment_date.add(qm.getPayment_date());
                            type_id.add(qm.getType_id());
                            payment_status.add(qm.getPayment_status());
                            //   orderID.add(qm.getId());
                            //  total=totalAmount+total;
                            int totalAmount = Integer.parseInt(qm.getPayment_amount());
                            total = totalAmount + total;
                            row.add(qm);
                            if (pd != null)
                                if (pd.isShowing())
                                    pd.dismiss();
                        }
                        mAdapter.notifyDataSetChanged();
                        String ttl = String.valueOf(total);
                        totalSum.setText(ttl);
                    } else {
                        Toast.makeText(getApplicationContext(), "no payment found", Toast.LENGTH_SHORT).show();

                        if (pd != null)
                            if (pd.isShowing())
                                pd.dismiss();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void paymentClick(View v) {
        String spin = spinner.getSelectedItem().toString();
        EditText totalAmount = (EditText) findViewById(R.id.editText);
        String ed = totalAmount.getText().toString();
        if (spinner.getSelectedItemPosition() > 0 && !ed.equals("")) {
            int posi = spinner.getSelectedItemPosition();
            UploadToListview(spin, ed, posi);
        } else if (spinner.getSelectedItemPosition() < 1) {
            Toast.makeText(getApplicationContext(), "please select payment status", Toast.LENGTH_SHORT).show();

        } else if (ed.equals("")) {
            Toast.makeText(getApplicationContext(), "please enter valid amound", Toast.LENGTH_SHORT).show();

        }
    }

    private void UploadToListview(String spin, String ed, int position) {
        String posi = String.valueOf(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String s = dateFormat.format(date);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
                request.addHeader("Authorization", "Bearer "+sp.getString("token","0"));
             //   request.addHeader("Content-Type", "application/json");
            }
        };
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(RestInterface.url).setRequestInterceptor(requestInterceptor).build();
        final RestInterface restInterface = adapter.create(RestInterface.class);
        restInterface.insertPayment(s, "1", posi, ed, orderID, new Callback<InsertPaymentModel>() {
            @Override
            public void success(InsertPaymentModel insertPaymentModel, Response response) {
                if (insertPaymentModel.getStatus().equals("1")) {
                    finish();
                    startActivity(getIntent());
                }
                if (insertPaymentModel.getStatus().equals("0")) {
                    Toast.makeText(getApplicationContext(), "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
                }
                if (insertPaymentModel.getStatus().equals("2")) {
                    Toast.makeText(getApplicationContext(), "failed. Incorrect credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSpinnerData() {
        final ArrayList<String> d2 = new ArrayList<>();

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                SharedPreferences sp = getSharedPreferences("loginSharedP", Context.MODE_PRIVATE);
                request.addHeader("Authorization", "Bearer "+sp.getString("token","0"));
              //  request.addHeader("Content-Type", "application/json");
            }
        };
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(RestInterface.url).setRequestInterceptor(requestInterceptor).build();
        final RestInterface restInterface = adapter.create(RestInterface.class);
        restInterface.getSpinner("1", new Callback<JSONResponceGetSpinner>() {
            @Override
            public void success(JSONResponceGetSpinner getSpinnerModel, Response response) {
                if (getSpinnerModel.getData() != null) {
                    DataGetSpinner[] dgm = getSpinnerModel.getData();
                    for (DataGetSpinner qm : dgm) {
                        categories.add(qm.getType_name());
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getApplicationContext(), "failed to get spinner data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
