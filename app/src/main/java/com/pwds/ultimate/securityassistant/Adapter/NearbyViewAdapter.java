package com.pwds.ultimate.securityassistant.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pwds.ultimate.securityassistant.Model.RegisterDto;
import com.pwds.ultimate.securityassistant.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Ultimate on 4/28/2018.
 */

public class NearbyViewAdapter extends RecyclerView.Adapter<NearbyViewAdapter.ViewHolder>{
    private ArrayList<RegisterDto> mData;
    private LayoutInflater mInflater;
    private NearbyViewAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    public NearbyViewAdapter(Context context, ArrayList<RegisterDto> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public NearbyViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.custom_nearby_people, parent, false);
        return new NearbyViewAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(NearbyViewAdapter.ViewHolder holder, int position) {
        RegisterDto name = mData.get(position);
        holder.username.setText(name.getUsername());
        Double num = Double.parseDouble(name.getDistance());
        holder.distance.setText(new DecimalFormat("##.#").format(num) + " m");
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView username, distance;

        ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            distance = itemView.findViewById(R.id.distance);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    RegisterDto getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(NearbyViewAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
