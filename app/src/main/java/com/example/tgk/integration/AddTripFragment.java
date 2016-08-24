package com.example.tgk.integration;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Note:    The code in this file is based on the code of Project FragmentBasicsM25 that
 *          Todd Kelly has given us through his personal communication.
 * File:    AddTripFragment.java
 * Author:  Shahid
 * Date:    April 2015
 */
public class AddTripFragment extends Fragment {
    private View view;
    private Button calcCO2;                     //Button that would calculate CO2 Emission
    private Button databaseInsertionButton;     //Button that would insert trip details into the database
    private Button viewTrips;                   //Button that would display all the trips
    private Spinner transTextView ;
    private String transportMode ;
    private TextView distanceTextView ;
    private TextView idTextView;
    private String emissionValue;

    double distance ;
    private TextView co2TextView ;
    private CarbonFootprintDBAdapter dbHelper;

    public AddTripFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.add_trip, container, false);
        calcCO2 = (Button)view.findViewById(R.id.calculate);
        calcCO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcCarbonFootprint(v);            }
        });
        dbHelper = new CarbonFootprintDBAdapter(getActivity());
        databaseInsertionButton = (Button)view.findViewById(R.id.database_insertion);
        databaseInsertionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIntoDataBase();
            }
        });

        viewTrips = (Button)view.findViewById(R.id.view_trips);
        viewTrips.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                try {
                    dbHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if(!dbHelper.rowCount()){
                    String toastMessage = "OOPS there is nothing in the database to be displayed!!! :(";
                    Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();
                } else {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                TripListFragment tripListFragment = new TripListFragment();
                ft.replace(R.id.container, tripListFragment).commit();
                }

                try {
                    dbHelper.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        return view ;
    }

    /**
     * This method calculates carbon footprint
     * @param v View
     */
    public void calcCarbonFootprint(View v) {
        boolean isNumeric = true;

        idTextView = (TextView) view.findViewById(R.id.trip_id_value);
        transTextView = (Spinner) view.findViewById(R.id.trans_value);
        transportMode = transTextView.getSelectedItem().toString();
        distanceTextView = (TextView) view.findViewById(R.id.trip_distance_text);
        co2TextView = (TextView) view.findViewById(R.id.co2_value);
        emissionValue = co2TextView.getText().toString();
        TextView date = (TextView)view.findViewById(R.id.trip_date_value);
        TextView notes = (TextView)view.findViewById(R.id.trip_notes);

        try {
            distance = Double.parseDouble((distanceTextView.getText().toString()));
        } catch (NumberFormatException nfe) {
            isNumeric = false;
        }
        if((idTextView.getText().length() == 0) || (Double.toString(distance).length() == 0) ||
        (date.getText().length() == 0) || (notes.getText().length() == 0)){
            Toast.makeText(getActivity(), "All fields are required", Toast.LENGTH_SHORT).show();
        } else if (!isNumeric) {
           Toast.makeText(getActivity(), "Enter proper distance value", Toast.LENGTH_SHORT).show();
        } else {
            if (transportMode.equals("Car")) {
                co2TextView.setText((1.76 * distance) + " lb");
            } else if (transportMode.equals("SUV")) {
                co2TextView.setText((2.3 * distance) + " lb");
            } else if (transportMode.equals("Van")) {
                co2TextView.setText((2 * distance) + " lb");
            } else if (transportMode.equals("Public transportation")) {
                co2TextView.setText((1.6 * distance) + " lb");
            } else if (transportMode.equals("Aeroplane")) {
                co2TextView.setText((4 * distance) + " lb");
            }
        }
    }

    /**
     * This method inserts details of a trip into the database
     */
    public void insertIntoDataBase(){

            boolean isNumber = true;
            Cursor cursor ;
        try {

            dbHelper.open();
            String distance = Double.toString(this.distance);
            String tripId = (((TextView) view.findViewById(R.id.trip_id_value))).getText().toString();
            String tripDate = ((TextView) (view.findViewById(R.id.trip_date_value))).getText().toString();
            String tripNotes =  ((TextView) (view.findViewById(R.id.trip_notes))).getText().toString();
            String co2Value = ((TextView) (view.findViewById(R.id.co2_value))).getText().toString();

             try {
                 double num = Double.parseDouble(tripId);
             } catch(NumberFormatException nfe){
                 isNumber = false;
             }

                 if (tripId.length() == 0 || tripDate.length() == 0
                         || tripNotes.length() == 0 ||
                         co2Value.length() == 0) {
                     Toast.makeText(getActivity(), "Please enter all the required values", Toast.LENGTH_SHORT).show();
                 } else if(!isNumber){
                     Toast.makeText(getActivity(), "Id must be numeric!", Toast.LENGTH_LONG).show();
                 } else {
                     dbHelper.createCarbonFootprintInfo(tripId,
                             transportMode, distance,
                             tripDate,
                             tripNotes,
                             co2Value
                     );

                }

        } catch(Exception e){

        } finally {
            try {
                dbHelper.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
