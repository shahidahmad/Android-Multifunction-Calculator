package com.example.tgk.integration;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;

/**
 * Note:    The code in this file is based on the code of Project FragmentBasicsM25 that
 *          Todd Kelly has given us through his personal communication.
 * File:    TripListFragment.java
 * Author:  Shahid
 * Date:    April 2015
 */
public class TripListFragment extends ListFragment {
    private CarbonFootprintDBAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private View view;
     ShahidMainActivity mainActivity;

    public TripListFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;


        //Generate ListView from SQLite Database
        displayListView();

    }

    private void displayListView(){
        new AsyncTask<Object, Object, Cursor>(){
            @Override
            public Cursor doInBackground(Object... ignor){
                Cursor cursor = null;
                dbHelper = new CarbonFootprintDBAdapter(getActivity());
                try {
                    dbHelper.open();



                 cursor = dbHelper.fetchAllData();
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        dbHelper.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                return cursor;
            }

            @Override
            public void onPostExecute(Cursor cursor){
                dataAdapter.changeCursor(cursor);
            }
        }.execute();

        String[] columns = new String[]{
                CarbonFootprintDBAdapter.KEY_ROWID,
                CarbonFootprintDBAdapter.KEY_RIDE,
                CarbonFootprintDBAdapter.KEY_DISTANCE,
                CarbonFootprintDBAdapter.KEY_DATE,
                CarbonFootprintDBAdapter.KEY_NOTES,
                CarbonFootprintDBAdapter.KEY_EMISSION
        };

        int[] to = new int[]{
           R.id.tripId_value,
           R.id.vehicle,
           R.id.distance,
           R.id.date,
           R.id.notes,
           R.id.emission
        };

        dataAdapter = new SimpleCursorAdapter(
            getActivity(), R.layout.co2_emission_details,
            null,
            columns,
            to,
            0);
        setListAdapter(dataAdapter);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mainActivity = (ShahidMainActivity)activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){

        mainActivity.onEntrySelected(Long.toString(id));


    }
}
