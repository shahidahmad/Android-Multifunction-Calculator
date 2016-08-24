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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ContactDetailFragment extends Fragment {
    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    private ContactDbAdapter dbHelper;
    private String contactID;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.contact_info, container, false);
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        //rootView = inflater.inflate(R.layout.contact_info, container, false);

        Button deleteButton = (Button)rootView.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                deleteContact(rootView);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateContactView(args.getLong(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateContactView(mCurrentPosition);
        }
    }

    public void updateContactView(long position) {
        //TextView article = (TextView) getActivity().findViewById(R.id.article);
        //article.setText(Ipsum.Articles[position]);
        dbHelper = new ContactDbAdapter(getActivity());
        dbHelper.open();
        Cursor cursor = dbHelper.fetchContactById(position);
        if (cursor != null) {
            try {

                contactID =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_ROWID));
                String contactFirstName =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_FIRSTNAME));
                String contactLastName =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_LASTNAME));
                String contactPhone =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_PHONE));
                String contactEmail =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_EMAIL));
                String contactNote =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContactDbAdapter.KEY_NOTE));

                ((TextView) rootView.findViewById(R.id.firstNameData)).setText(contactFirstName);
                ((TextView) rootView.findViewById(R.id.lastNameData)).setText(contactLastName);
                ((TextView) rootView.findViewById(R.id.phoneData)).setText(contactPhone);
                ((TextView) rootView.findViewById(R.id.emailData)).setText(contactEmail);
                ((TextView) rootView.findViewById(R.id.noteData)).setText(contactNote);
            } catch (IllegalArgumentException e) {
                //Log.d(TAG, "IllegalArgumentException");
            }
        }
        //mCurrentPosition = position;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    public void deleteContact(View view){
        dbHelper = new ContactDbAdapter(getActivity());
        dbHelper.open();
        dbHelper.deleteContactById(contactID);


    }
}