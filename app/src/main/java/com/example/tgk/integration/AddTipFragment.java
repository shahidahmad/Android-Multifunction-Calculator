package com.example.tgk.integration;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Zhihui on 2015-04-11.
 */
public class AddTipFragment extends Fragment {
    View view;
    Button calcTip;
    Button databaseInsertionButton;
    Button viewBills;
    Button clearDatabaseButton;
    TextView paymentTextView ;
    String paymentMode ;
    TextView priceTextView ;
    TextView tipTextView;
    TextView totalTextView;
    double price ;
    double tip;

    private RestaurantDBAdapter dbHelper;

    public AddTipFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.add_tip, container, false);
        calcTip = (Button)view.findViewById(R.id.calculate);
        calcTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcRestaurantTip(v);            }
        });

        databaseInsertionButton = (Button)view.findViewById(R.id.database_insertion);
        databaseInsertionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIntoDataBase();
            }
        });

        viewBills = (Button)view.findViewById(R.id.view_bills);
        viewBills.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                TipListFragment tipListFragment = new TipListFragment();
                ft.replace(R.id.zhuhui_container, tipListFragment).commit();
            }
        });

        clearDatabaseButton=(Button)view.findViewById(R.id.delete_info);
        clearDatabaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDatabase();
            }
        });

        return view ;
    }

    public void calcRestaurantTip(View v){
//        Spinner spinner = (Spinner)(view.findViewById(R.id.trans_list));
        paymentTextView = (TextView)view.findViewById(R.id.payment_method_text);
        paymentMode = paymentTextView.getText().toString();
        priceTextView = (TextView)view.findViewById(R.id.price_amount_text);
        price = Integer.parseInt((priceTextView.getText().toString()));
        tipTextView = (TextView)view.findViewById(R.id.tip_amount_text);
        tip=Integer.parseInt((tipTextView.getText().toString()));
        totalTextView=(TextView)view.findViewById(R.id.co2_value);
        if(paymentMode.equals("credit")){
            totalTextView.setText("$"+(1.13*price)+tip);
        } else if(paymentMode.equals("debit")){
            totalTextView.setText("$"+(1.13*price)+tip);
        } else if(paymentMode.equals("cash")){
            totalTextView.setText("$"+(1.13*price)+tip);
        } else {
            totalTextView.setText("$"+(1.13*price)+tip);
        }
    }

    public void insertIntoDataBase(){
        try {
            dbHelper = new RestaurantDBAdapter(getActivity());
            dbHelper.open();
            dbHelper.createRestaurantInfo(paymentMode, Double.toString(price),
                    ((TextView) (view.findViewById(R.id.tip_amount))).getText().toString(),
                    ((TextView) (view.findViewById(R.id.tip_notes))).getText().toString(),
                    tipTextView.toString());
        } catch(Exception e){

        } finally {
            dbHelper.close();
        }
    }

    public void clearDatabase(){
        try {
            dbHelper = new RestaurantDBAdapter(getActivity());
            dbHelper.open();
            dbHelper.deleteInfo();
        } catch(Exception e){

        } finally {
            dbHelper.close();
        }
    }
}
