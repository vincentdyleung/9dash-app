package com.ninedash.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 14年4月26日.
 */
public class RestaurantListAdapter extends BaseAdapter {
    ArrayList<HashMap<String,String>> mData;

    RestaurantListAdapter(ArrayList<HashMap<String,String>> rowdata) {
        mData = rowdata;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;
/*
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        } else {
            result = convertView;
        }
        */
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
    }


}
