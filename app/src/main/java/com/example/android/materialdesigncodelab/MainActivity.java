/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.example.android.materialdesigncodelab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Provides UI for the main screen.
 */
public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    SharedPreferences sharedpreferences;
    // UserSessionManager session;

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding Toolbar to Main screen
        // Adding Toolbar to Main screen



        // Stay at the current activity.


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs


        Intent intent = getIntent();
        sharedpreferences = getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);
        System.out.println(sharedpreferences.getString("username", null));


        String username = sharedpreferences.getString("username", null);
        View view;


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        TextView prof_name = (TextView) mDrawerLayout.findViewById(R.id.prof_name);
        prof_name.setText(username);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    // This method will trigger on item Click of navigation menu
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Set item in checked state
                        menuItem.setChecked(true);

                        int order = menuItem.getOrder();

                        //System.out.println(menuItem.getItemId());
                        //Toast.makeText(getApplicationContext(),"Item-"+menuItem.getItemId(),Toast.LENGTH_LONG).show();
                        // TODO: handle navigation
                        Fragment fragment = null;
                        Adapter adapter = new Adapter(getSupportFragmentManager());

                        switch (order) {

                            case 1:
                                Intent intent = new Intent(MainActivity.this, BitmapActivity.class);
                                startActivity(intent);
                                break;


                            case 2:
                                Intent intent1 = new Intent(MainActivity.this, UsContentFragment.class);
                                startActivity(intent1);
                                break;


                            case 3:
                                //   SaveSharedPreference.clearUserName(getApplicationContext());
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.clear();
                                editor.commit();

                                // After logout redirect user to Login Activity
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);

                                // Closing all the Activities
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                // Add new Flag to start new Activity
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                // Staring Login Activity
                                startActivity(i);
                                //   Toast.makeText(getApplicationContext(),"logout hogaya",Toast.LENGTH_LONG).show();


                                /*final String LOG_OUT = "event_logout";
                                Intent intentx = new Intent(LOG_OUT);
                                //send the broadcast to all activities who are listening
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentx);*/

                                break;

                        }


                        // Closing drawer on item click
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // Adding Floating Action Button to bottom right of main view
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BitmapActivity.class);
                startActivity(intent);//Snackbar.make(v, "Hello Snackbar!",
                //      Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        adapter.addFragment(new TileContentFragment(), "Feed");
        //Feed will be displayed using Cards

        adapter.addFragment(new SearchFragment(), "Search");
        viewPager.setAdapter(adapter);

        adapter.addFragment(new SuccessContentFragment(), "Success Stories");
        viewPager.setAdapter(adapter);


        adapter.addFragment(new ProfileContentFragment(), "Profile");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

}
