package com.forthpeople.housebill;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forthpeople.housebill.adapter.BillRecyclerViewAdapter;
import com.forthpeople.housebill.customviews.DividerItemDecoration;
import com.forthpeople.housebill.model.HouseBill;

import java.util.Collections;
import java.util.Comparator;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dhiraj on 16-12-2015.
 */
public class MainFragment extends Fragment {

    String TAG = "MainFragment";
    public static String dateFormat = "yyyy/MM/dd";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.main_fragment_container, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.bill_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new BillRecyclerViewAdapter(getDataSet(), getActivity());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        ((BillRecyclerViewAdapter) mAdapter).setOnItemClickListener(
                new BillRecyclerViewAdapter.MyClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Log.i(TAG, " Clicked on Item " + position);
                        }
                    });
    }

    private ArrayList<HouseBill> getDataSet() {
        ArrayList<HouseBill> results = new ArrayList<HouseBill>(HouseBill.findWithQuery(HouseBill.class, "Select * from HOUSE_BILL", null));
        Collections.sort(results, new Comparator<HouseBill>() {
            @Override
            public int compare(HouseBill lhs, HouseBill rhs) {
                return (lhs.getBillDated().getTime() < rhs.getBillDated().getTime() ? -1 : 1);
            }
        });
        return results;
    }
}
