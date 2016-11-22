package com.sourmilq.sourmilq.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Philip on 2016-11-22.
 */

public class ExpireListAdapter extends RecyclerView.Adapter<ExpireListAdapter.ViewHolder> {

    ArrayList<Item> mDataset;

    public ExpireListAdapter(ArrayList<Item> dataset) {
        this.mDataset = dataset;
    }

    @Override
    public ExpireListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expire_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mDataset.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expString = sdf.format(item.getExpiration().getTime());
        holder.mTextView.setText(expString + " : "+ item.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.expireItem);
        }
    }
}
