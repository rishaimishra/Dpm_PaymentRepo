package com.dpm.payment.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpm.payment.activities.WebViewActivity;
import com.dpm.payment.models.receipt.DatasItem;
import com.dpm.payment.utils.StringUtils;
import com.payment.R;

import java.util.List;


public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {

    private List<DatasItem> listOfNav;
    Activity activity;


    public ReceiptAdapter(List<DatasItem> listOfNav, Activity activity) {
        this.listOfNav = listOfNav;
        this.activity = activity;


    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final DatasItem object = listOfNav.get(position);

        holder.tvKey.setText(object.getId());

        holder.tvView.setOnClickListener(v->{
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra("url",object.getUrl());
            activity.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {


        return listOfNav == null ? 0 : listOfNav.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvKey, tvView;


        // view create //
        MyViewHolder(View view) {
            super(view);
            tvKey = view.findViewById(R.id.tvKey);
            tvView = view.findViewById(R.id.tvView);


        }
    }
}