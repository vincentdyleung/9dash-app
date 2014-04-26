package com.ninedash.app;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        String[] values = new String[] { "McDonlad's", "Cafe de Coral", "Golden Bowl" };
        HashMap<String,String> myMap = new HashMap<String, String>();
        ArrayList<HashMap<String,String>> listData = new ArrayList<HashMap<String, String>>();
        for(int i = 0; i < values.length; i++) {
            myMap.put("Name",values[0]);
            listData.add(myMap);
        }


        RestaurantListAdapter adapter = new RestaurantListAdapter(listData);

        setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView lv = getListView();
        lv.setDivider(null);

    }
}
