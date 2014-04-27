package com.ninedash.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lenovo on 14年4月26日.
 */
public class RestaurantListFragment extends ListFragment {

    public RestaurantListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View listView = super.onCreateView(inflater, container, savedInstanceState);
        new HTTPHandler() {
            @Override
            public HttpUriRequest getHttpRequest() {
                URI uri = null;
                try {
                    uri = new URI(MainActivity.HOST + "restaurants/");
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                return new HttpGet(uri);
            }

            @Override
            public void onResponse(JSONObject res) {
                try {
                    JSONArray array = res.getJSONArray("data");
                    ArrayList<String> listData = new ArrayList<String>();
                    for (int i = 0; i < array.length(); i++) {
                        listData.add(i, array.getJSONObject(i).getString("_id"));
                    }
                    RestaurantListAdapter adapter = new RestaurantListAdapter(listData);
                    setListAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = getListView();
        lv.setDivider(null);

    }
}
