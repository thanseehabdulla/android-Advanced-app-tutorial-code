package com.example.infinit.acount;



import android.content.Context;
import android.content.SharedPreferences;

import com.example.infinit.acount.jsonresponce.JSONResponceGetSpinner;
import com.example.infinit.acount.jsonresponce.JSONResponseHome;
import com.example.infinit.acount.jsonresponce.JSONResponseInvo;
import com.example.infinit.acount.model.InsertPaymentModel;
import com.example.infinit.acount.model.LoginModel;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by pc3 on 11-Nov-16.
 */
public interface RestInterface {
// String url = "http://192.168.1.102:8080/dealer/public/";
String url="http://beforelive.in/dev/dealer/public/";

// String url="http://beforelive.in/dev/rice_dealer/v1/";

    @FormUrlEncoded
    @POST("/login")
    void login(@Field("username") String username,
               @Field("password") String password, Callback<LoginModel> cb);

    @FormUrlEncoded
    @POST("/getdataByAgentDealerID")
    void getData(@Field("agent_id") String agent_id,
                 Callback<JSONResponseHome> cb);
    //    @GET("/users")
    //    Call<Model> listRepos(@Path("user") String user);

    @FormUrlEncoded
    @POST("/getPaymentByUserID")
    void getPaymentDATA(@Field("order_id") String orderId,
                        Callback<JSONResponseInvo> cb);

    @FormUrlEncoded
    @POST("/getSpinnerItem")
    void getSpinner(@Field("id") String orderId,
                    Callback<JSONResponceGetSpinner> cb);

    @FormUrlEncoded
    @POST("/insertPayment")
    void insertPayment(@Field("payment_date") String payment_date,
                       @Field("payment_status") String payment_amount,
                       @Field("type_id") String type_id,
                       @Field("payment_amount") String password,
                       @Field("order_id") String order_id,
                       Callback<InsertPaymentModel> cb);
}
