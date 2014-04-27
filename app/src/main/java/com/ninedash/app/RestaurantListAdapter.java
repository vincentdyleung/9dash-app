package com.ninedash.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lenovo on 14年4月26日.
 */
public class RestaurantListAdapter extends BaseAdapter {
    ArrayList<String> mData;

    RestaurantListAdapter(ArrayList<String> rowdata) {
        mData = rowdata;
    }
    HashMap<Integer, JSONObject> waitingStats = new HashMap<Integer, JSONObject>();

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
/*
        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        } else {
            result = convertView;
        }
        */
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        final TextView restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
        final TextView waitingTime = (TextView) view.findViewById(R.id.waiting_time);
        final TextView waitingPosition = (TextView) view.findViewById(R.id.waiting_position);
        final int finalPosition = position;
        if (!waitingStats.containsKey(position)) {
            final String id = getItem(position);
            new HTTPHandler() {
                @Override
                public HttpUriRequest getHttpRequest() {
                    URI uri = null;
                    try {
                        uri = new URI(MainActivity.HOST + "restaurants/id/" + id);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return new HttpGet(uri);
                }

                @Override
                public void onResponse(JSONObject res) {
                    try {
                        JSONObject time = res.getJSONObject("time");
                        waitingStats.put(finalPosition, res);
                        restaurantName.setText(res.getString("name"));
                        waitingTime.setText(Long.toString(Math.round(time.getDouble("allWT"))));
                        waitingPosition.setText(Long.toString(Math.round(time.getDouble("allWP"))));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }.execute();
        } else {
            JSONObject stat = waitingStats.get(position);
            try {
                restaurantName.setText(stat.getString("name"));
                JSONObject time = stat.getJSONObject("time");
                waitingTime.setText(Long.toString(Math.round(time.getDouble("allWT"))));
                waitingPosition.setText(Long.toString(Math.round(time.getDouble("allWP"))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }


}
