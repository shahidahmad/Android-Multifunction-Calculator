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

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 *  The code in this file is based on the project FragmentBasicsM25 that
 *  Todd Kelly has given us through his personal communication.
 * Created by Xing on 4/10/2015.
 */
public class TaskDetailFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private ActivityDbAdapter dbHelper;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {

        // If activity recreated (such as from screen rotate), restore
        // the previous act selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }

        // Inflate the layout for this fragment
        return rootView = inflater.inflate(R.layout.act_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the act text.
        Bundle args = getArguments();
        if (args != null) {
            // Set act based on argument passed in
            updateArticleView(args.getLong(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set act based on saved instance state defined during onCreateView
            updateArticleView(mCurrentPosition);
        }
    }

    public void updateArticleView(long position) {

        dbHelper = new ActivityDbAdapter(getActivity());
        dbHelper.open();
        Cursor cursor = dbHelper.fetchTasksById(position);
        if (cursor != null) {
            try {
                String title =
                        cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String note =
                        cursor.getString(cursor.getColumnIndexOrThrow("note"));
                String priority =
                        cursor.getString(cursor.getColumnIndexOrThrow("priority"));
                String duration =
                        cursor.getString(cursor.getColumnIndexOrThrow("duration"));

                ((TextView) rootView.findViewById(R.id.title)).setText(title);
                ((TextView) rootView.findViewById(R.id.note)).setText(note);
                ((TextView) rootView.findViewById(R.id.priority)).setText(priority);
                ((TextView) rootView.findViewById(R.id.duration)).setText(duration);
            } catch (IllegalArgumentException e) {
                //Log.d(TAG, "IllegalArgumentException");
            }
        }
        //mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current act selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }
}