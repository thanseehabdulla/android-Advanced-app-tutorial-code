package com.example.infinit.acount.model;

import com.google.gson.annotations.Expose;

/**
 * Created by pc3 on 11/22/2016.
 */
public class DataGetSpinner {
    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Expose
    private String type_name;
    @Expose
    private String id;

}
