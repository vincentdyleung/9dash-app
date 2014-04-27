package com.ninedash.app;

import android.app.Activity;
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
    OnBlockSelectedListener mCallback;
    ArrayList<String> mListData;

    public RestaurantListFragment() {
        mListData = new ArrayList<String>();
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
                    for (int i = 0; i < array.length(); i++) {
                        mListData.add(i, array.getJSONObject(i).getString("id"));
                    }
                    RestaurantListAdapter adapter = new RestaurantListAdapter(mListData);
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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
        mCallback.onBlockSelected(mListData.get(position));

    }

    public interface OnBlockSelectedListener {
        public void onBlockSelected(String id);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBlockSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnBlockListener");
        }
    }
}
