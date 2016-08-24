package com.example.tgk.integration;

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

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v7.app.ActionBarActivity;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
/**
 *  The code in this file is based on the project FragmentBasicsM25 that
 *  Todd Kelly has given us through his personal communication.
 * Created by Xing on 4/10/2015.
 */
public class XingMainActivity
        extends ActionBarActivity
        implements HeadlineFragment.OnHeadlineSelectedListener,
        AddTaskFragment.OnFragmentInteractionListener {

    private HeadlineFragment headlineFragment;
    private ActivityDbAdapter dbHelper;
    public XingMainActivity(){

    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_menu);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment

        AddTaskFragment newFragment = new AddTaskFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void loadHeadlineFragment() {

        HeadlineFragment firstFragment = new HeadlineFragment();

        // pass the Intent's extras to the fragment as arguments
        firstFragment.setArguments(getIntent().getExtras());

        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
        headlineFragment = firstFragment;
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


    public void showInstruction(){
        Intent intent = new Intent(this,InstructionWindow.class);
        startActivity(intent);

    }



    @Override
    public void onItemSelected(long position) {
        // The user selected the headline of an act from the CountryListFragment

        // Capture the act fragment from the activity layout
        TaskDetailFragment articleFrag = (TaskDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.article_fragment);

        if (articleFrag != null) {
            // If act frag is available, we're in two-pane layout...

            // Call a method in the CountryDetailFragment to update its content
            //articleFrag.updateArticleView(position);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected act
            TaskDetailFragment newFragment = new TaskDetailFragment();
            Bundle args = new Bundle();
            args.putLong(TaskDetailFragment.ARG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction() {
        if(headlineFragment == null)
            loadHeadlineFragment();

        headlineFragment.displayListView();
        getSupportFragmentManager().popBackStackImmediate();
    }
}