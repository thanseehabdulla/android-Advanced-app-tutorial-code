package com.example.infinit.acount.model;

import com.google.gson.annotations.Expose;

/**
 * Created by pc3 on 11-Nov-16.
 */
public class InsertPaymentModel {
    @Expose
    private String status;
    @Expose
    private String login;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Expose
    private String name;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


}
