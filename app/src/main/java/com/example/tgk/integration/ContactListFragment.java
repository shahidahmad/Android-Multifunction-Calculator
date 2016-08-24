/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class ContactListFragment extends ListFragment {
    OnHeadlineSelectedListener mCallback;
    private ContactDbAdapter dbHelper;
    private SimpleCursorAdapter dataAdapter;
    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnHeadlineSelectedListener {
        /** Called by ContactListFragment when a list item is selected */
        public void onArticleSelected(long position);
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
    private void displayListView() {
        new AsyncTask<Object, Object, Cursor>() {
            @Override
            public Cursor doInBackground(Object... ignore) {
                dbHelper = new ContactDbAdapter(getActivity());
                dbHelper.open();
                //Clean all data
                //dbHelper.deleteAllContacts();
                //Add some data
            //    dbHelper.insertSomeContacts();
                Cursor cursor = dbHelper.fetchAllContacts();


                return cursor;  // about to be passed by AsyncTask to onPostExecute
            }

            @Override
            public void onPostExecute(Cursor cursor) {
                //ListView listView = (ListView) findViewById(R.id.listView1);
                // Assign adapter to ListView
                dataAdapter.changeCursor(cursor);  //MAIN THREAD after doInBackground returns the cursor

            }
        }.execute();
        // In class, I had these MAIN THREAD items in doInBackground(), but they need to be on the main thread
        // The desired columns to be bound  MAIN THREAD
        String[] columns = new String[]{
                ContactDbAdapter.KEY_FIRSTNAME,
                ContactDbAdapter.KEY_LASTNAME,
                ContactDbAdapter.KEY_PHONE,
                ContactDbAdapter.KEY_EMAIL,
                ContactDbAdapter.KEY_NOTE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[]{
                R.id.firstNameData,
                R.id.lastNameData,
                R.id.phoneData,
                R.id.emailData,
                R.id.noteData
        };

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(             // MAIN THREAD
                getActivity(), R.layout.label_info,
                null,
                columns,
                to,
                0);

        setListAdapter(dataAdapter);  //MAIN THREAD
    }
        @Override
    public void onStart() {
        super.onStart();
        displayListView();
        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.article_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onArticleSelected(id);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }
}