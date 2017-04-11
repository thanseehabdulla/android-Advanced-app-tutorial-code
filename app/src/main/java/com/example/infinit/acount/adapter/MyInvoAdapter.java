package com.example.infinit.acount.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.infinit.acount.R;
import com.example.infinit.acount.model.DataGetPaymentmodel;

import java.util.List;

/**
 * Created by pc3 on 10-Nov-16.
 */

public class MyInvoAdapter extends RecyclerView.Adapter<MyInvoAdapter.ViewHolder> {
    Context context;
    List<DataGetPaymentmodel> itemsData;

    public MyInvoAdapter(Context context, List<DataGetPaymentmodel> items) {
        this.context = context;
        this.itemsData = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyInvoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_invitracker, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        DataGetPaymentmodel rowItem = (DataGetPaymentmodel) getItem(position);
        viewHolder.date.setText(rowItem.getPayment_date());
        viewHolder.amound.setText(rowItem.getPayment_amount());
        viewHolder.pending.setText(rowItem.getPayment_status());
        viewHolder.cheque.setText(rowItem.getType_id());


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;
        public TextView amound;
        public TextView cheque;
        public TextView pending;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            amound = (TextView) itemLayoutView.findViewById(R.id.amound);
            cheque = (TextView) itemLayoutView.findViewById(R.id.cheque);
            pending = (TextView) itemLayoutView.findViewById(R.id.pending);

        }
    }

    public DataGetPaymentmodel getItem(int position) {
        return itemsData.get(position);
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}