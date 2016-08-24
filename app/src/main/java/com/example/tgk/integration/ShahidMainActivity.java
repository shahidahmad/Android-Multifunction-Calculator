package com.example.tgk.integration;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



/**
 * Note:    The code in this file is based on the code of project FragmentBasicsM25 that
 *          Todd Kelly has given us through his personal communication.
 * File:    ShahidMainActivity.java
 * Author:  Shahid
 * Date:    April 2015
 */

public class ShahidMainActivity extends ActionBarActivity {

    private CarbonFootprintDBAdapter dbHelper;
    public ShahidMainActivity(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shahid_main_activity);

        AddTripFragment addTripFragment = new AddTripFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.add(R.id.container, addTripFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

     //   noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (id){
            case R.id.action_one:
                go(ZhuhuiMainActivity.class);
                break;
            case R.id.action_two:
                go(ShahidMainActivity.class);
                break;
            case R.id.action_three:
                go(StephenActivity.class);
                break;
            case R.id.action_four:
                go(XingMainActivity.class);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void go(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    public void onEntrySelected(String position){

        dbHelper = new CarbonFootprintDBAdapter(getApplicationContext());
        CarbonFootprintDetail newDetails = new CarbonFootprintDetail();
        newDetails.setId(position);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newDetails).commit();

    }
}
