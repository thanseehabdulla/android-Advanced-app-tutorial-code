package com.example.infinit.acount.model;

import com.google.gson.annotations.Expose;

/**
 * Created by pc3 on 11/18/2016.
 */
public class DataGetmodel {
    @Expose
    private String order_number;
    @Expose
    private String order_desc;
    @Expose
    private String order_qty;
    @Expose
    private String id;
    @Expose
    private String order_amount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(String order_qty) {
        this.order_qty = order_qty;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }






    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_desc() {
        return order_desc;
    }

    public void setOrder_desc(String order_desc) {
        this.order_desc = order_desc;
    }
}
