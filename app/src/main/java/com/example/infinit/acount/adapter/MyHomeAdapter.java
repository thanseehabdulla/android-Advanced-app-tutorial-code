package com.example.infinit.acount.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.infinit.acount.InvoTrackerActivity;
import com.example.infinit.acount.R;
import com.example.infinit.acount.model.DataGetmodel;

import java.util.List;

public class MyHomeAdapter extends RecyclerView.Adapter<MyHomeAdapter.ViewHolder> {
    Context context;
    List<DataGetmodel> itemsData;
    String OID, Amount;

    public MyHomeAdapter(Context context, List<DataGetmodel> items) {
        this.context = context;
        this.itemsData = items;
    }

    @Override
    public MyHomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout_home, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        DataGetmodel rowItem = (DataGetmodel) getItem(position);
        viewHolder.txtViewTitle.setText("Order no: " + rowItem.getOrder_number());
        viewHolder.qty.setText("Qty: " + rowItem.getOrder_qty());
        viewHolder.rupees.setText("Desc: "+rowItem.getOrder_desc());
        viewHolder.amount.setText("Amount: "+rowItem.getOrder_amount());
        viewHolder.orderId.setText(rowItem.getId());
        //  viewHolder.imgViewIcon.setImageResource(rowItem.getImageUrl());

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OID = viewHolder.orderId.getText().toString();
                Amount = viewHolder.amount.getText().toString();
                Intent i = new Intent(context, InvoTrackerActivity.class);
                i.putExtra("orderID", OID);
                i.putExtra("amount", Amount);
                context.startActivity(i);
            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtViewTitle;
        public TextView qty;
        public TextView rupees;
        public TextView amount;
        public TextView orderId;
        public ImageView plus;
        //    public CircularImageView imgViewIcon;
        //   CircleImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            qty = (TextView) itemLayoutView.findViewById(R.id.item_qty);
            rupees = (TextView) itemLayoutView.findViewById(R.id.item_rupees);
            amount = (TextView) itemLayoutView.findViewById(R.id.item_total);
            orderId = (TextView) itemLayoutView.findViewById(R.id.orderId);
            plus = (ImageView) itemLayoutView.findViewById(R.id.plus);

            //    imgViewIcon = (CircleImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }

    public DataGetmodel getItem(int position) {
        return itemsData.get(position);
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}