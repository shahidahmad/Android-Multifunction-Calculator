package com.example.tgk.integration;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.sql.SQLException;

/**
 * Note:    The code in this file is based on the code of Project FragmentBasicsM25 that
 *          Todd Kelly has given us through his personal communication.
 * File:    CarbonFootprintDetail.java
 * Author:  Shahid
 * Date:    April 2015
 */
public class CarbonFootprintDetail extends Fragment {

    private Button deleteButton;
    private Button returnButton;
    private View rootView;
    private CarbonFootprintDBAdapter dbHelper;
    private String id ;
    private String ID;
    private LayoutInflater inflater;
    private ViewGroup container;
    private Bundle savedInstanceState;
    final static String ARG_POSITION = "position";
    private int mCurrentPosition = -1;


    public CarbonFootprintDetail(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        this.inflater = inflater;
        this.container = container;
        this.savedInstanceState = savedInstanceState;
        dbHelper = new CarbonFootprintDBAdapter(getActivity());
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
        }
        rootView = inflater.inflate(R.layout.co2_emission_detail, container, false);
        deleteButton = (Button)rootView.findViewById(R.id.delete_entry);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo();
            }
        });
        returnButton = (Button)rootView.findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToMainWindow();
            }
        });
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();

            fetchTripData(ID);

    }

    public void fetchTripData(String position){
        Cursor cursor;


        try {

            dbHelper.open();


            cursor = dbHelper.fetchDataById(position);
            if(cursor != null) {
                id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String ride =
                        cursor.getString(cursor.getColumnIndexOrThrow("ride"));
                String distance =
                        cursor.getString(cursor.getColumnIndexOrThrow("distance"));
                String date =
                        cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String notes =
                        cursor.getString(cursor.getColumnIndexOrThrow("notes"));
                String emission =
                        cursor.getString(cursor.getColumnIndexOrThrow("emission"));

                ((TextView) rootView.findViewById(R.id.tripIdValue)).setText(id);
                ((TextView) rootView.findViewById(R.id.vehicle1)).setText(ride);
                ((TextView) rootView.findViewById(R.id.distance1)).setText(distance);
                ((TextView) rootView.findViewById(R.id.date1)).setText(date);
                ((TextView) rootView.findViewById(R.id.notes1)).setText(notes);
                ((TextView) rootView.findViewById(R.id.emission1)).setText(emission);

            }
        } catch(IllegalArgumentException  e){

        } catch(SQLException e) {


        } finally{
            try {
                dbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void deleteInfo(){
        dbHelper = new CarbonFootprintDBAdapter(getActivity());
        try {
            dbHelper.open();
            dbHelper.deleteInfo(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                dbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        getFragmentManager().beginTransaction().
                replace(R.id.container, new AddTripFragment()).commit();

    }

    public void returnToMainWindow(){
        getFragmentManager().beginTransaction().
                replace(R.id.container, new AddTripFragment()).commit();

    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }

    public void setId(String id){
        this.ID = id;
    }
}
