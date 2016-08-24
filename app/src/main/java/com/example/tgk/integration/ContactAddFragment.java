package com.example.tgk.integration;

import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ContactAddFragment extends Fragment {

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

        Button saveButton = (Button)rootView.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact(rootView);
            }
        });

        Button backButton = (Button)rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View view){
                backUp(rootView);
            }
        });

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

    }

    public void saveContact(View view){
        dbHelper = new ContactDbAdapter(getActivity());
        dbHelper.open();

                String firstName = ((TextView) rootView.findViewById(R.id.firstNameData)).getText().toString();
                String lastName = ((TextView) rootView.findViewById(R.id.lastNameData)).getText().toString();
                String phone = ((TextView) rootView.findViewById(R.id.phoneData)).getText().toString();
                String email = ((TextView) rootView.findViewById(R.id.emailData)).getText().toString();
                String note = ((TextView) rootView.findViewById(R.id.noteData)).getText().toString();

        dbHelper.createContact(firstName, lastName, phone, email, note);

    }

    public void backUp(View view){
        getFragmentManager().popBackStack();
    }

    public void deleteContact(View view){
        dbHelper = new ContactDbAdapter(getActivity());
        dbHelper.open();
        dbHelper.deleteContactById(contactID);


    }
}
