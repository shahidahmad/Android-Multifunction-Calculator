package com.example.tgk.integration;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Zhihui on 2015-04-11.
 */
public class TipListFragment extends ListFragment {
    private RestaurantDBAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    private View view;

    public TipListFragment(){
        //       displayListView();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // We need to use a different list item layout for devices older than Honeycomb
        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        //Generate ListView from SQLite Database
        displayListView();
        // Create an array adapter for the list view, using the Ipsum headlines array
        //setListAdapter(new ArrayAdapter<String>(getActivity(), layout, Ipsum.Headlines));
    }
//    public void onCreate(Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        displayListView();
//    }

    private void displayListView(){
        new AsyncTask<Object, Object, Cursor>(){
            @Override
            public Cursor doInBackground(Object... ignor){
                dbHelper = new RestaurantDBAdapter(getActivity());
                try {
                    dbHelper.open();
                } catch(Exception e){
                    e.printStackTrace();
                }
                Cursor cursor = dbHelper.fetchAllData();
                return cursor;
            }
            @Override
            public void onPostExecute(Cursor cursor){
                dataAdapter.changeCursor(cursor);
            }
        }.execute();

        String[] columns = new String[]{
                RestaurantDBAdapter.KEY_PAYMENT,
                RestaurantDBAdapter.KEY_PRICE,
                RestaurantDBAdapter.KEY_TIP,
                RestaurantDBAdapter.KEY_NOTES,
                RestaurantDBAdapter.KEY_TOTAL
        };
        int[] to = new int[]{
                R.id.payment,
                R.id.price,
                R.id.tips,
                R.id.notes,
                R.id.total
        };
        dataAdapter = new SimpleCursorAdapter(
                getActivity(), R.layout.bill_details,
                null,
                columns,
                to,
                0);
        setListAdapter(dataAdapter);
    }
}
