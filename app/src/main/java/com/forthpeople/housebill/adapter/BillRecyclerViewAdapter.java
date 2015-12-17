package com.forthpeople.housebill.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.forthpeople.housebill.MainFragment;
import com.forthpeople.housebill.R;
import com.forthpeople.housebill.model.HouseBill;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by dhiraj on 17-12-2015.
 */
public class BillRecyclerViewAdapter  extends RecyclerView
        .Adapter<BillRecyclerViewAdapter
        .HouseBillObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<HouseBill> houseBillArrayList;
    private static MyClickListener myClickListener;
    private  Activity activity;

    public static class HouseBillObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;

        public HouseBillObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label);
            dateTime = (TextView) itemView.findViewById(R.id.datetime);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public BillRecyclerViewAdapter(ArrayList<HouseBill> myDataset, Activity activity) {
        houseBillArrayList = myDataset;
        this.activity = activity;
    }

    @Override
    public HouseBillObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        HouseBillObjectHolder dataObjectHolder = new HouseBillObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(HouseBillObjectHolder holder, int position) {
        holder.label.setText(activity.getResources().getString(R.string.Rs) + " " +houseBillArrayList.get(position).getAmount() + "");
        String myDateFormat = "MMM/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(myDateFormat);
        holder.dateTime.setText(dateFormat.format(houseBillArrayList.get(position).getBillDated()));
    }

    public void addItem(HouseBill dataObj, int index) {
        houseBillArrayList.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        houseBillArrayList.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return houseBillArrayList.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
