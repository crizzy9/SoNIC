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

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Provides UI for the view with List.
 */
public class SearchFragment extends Fragment {

    Spinner search_spinner;
    EditText search_text;
    Button search_btn;
    public static final ArrayList<ListItem> gridMockData = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = (View) inflater.inflate(R.layout.search,container,false);

        search_btn = (Button) mView.findViewById(R.id.search_btn);
        search_text = (EditText) mView.findViewById(R.id.search_text);
        search_spinner = (Spinner) mView.findViewById(R.id.search_spinner);
        //search_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        search_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                /*String searchString = search_text.getText().toString();
                search_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                String searchCategory = search_spinner.toString();
*/
                /*Intent i = new Intent(getContext(),SearchResultFragment.class);
                ListItem newsData = new ListItem();
                newsData.setType(searchCategory);
                newsData.setValue(searchString);
                gridMockData.add(newsData);
                startActivity(i);*/


                String searchString = search_text.getText().toString();
                search_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                String searchCategory = search_spinner.getSelectedItem().toString();
                System.out.println("I am here 1");

                ListItem newsData = new ListItem();
                newsData.setType(searchCategory);
                newsData.setValue(searchString);
                gridMockData.add(newsData);
                System.out.println("here 2");
                System.out.println("This is search:: " + gridMockData + " ::: " + searchCategory + "    " + searchString);

                //intent in fragment
                Fragment frag = new SearchResultFragment();
                android.support.v4.app.FragmentManager fragManager = getFragmentManager();
                fragManager.beginTransaction().replace(R.id.search_frag, frag).commit();

            }
        });


        return mView;


        /*RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));


            /*search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    String searchString = search_text.getText().toString();
                    search_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                    String searchCategory = search_spinner.toString();
                    System.out.println("I am here 1");

                    ListItem newsData = new ListItem();
                    newsData.setType(searchCategory);
                    newsData.setValue(searchString);
                    gridMockData.add(newsData);
                    System.out.println("here 2");
                    System.out.println("This is search:: " + gridMockData + " ::: " + searchCategory + "    " + searchString);

                    Intent i = new Intent(getContext(), SearchResultFragment.class);
                    System.out.println("here 3");
                    startActivity(i);
                }
            });
*/

        }
    }
    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 18;

        public ContentAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}




// Original search method ||


/*

package com.example.android.materialdesigncodelab;





*/
/*
REFERENCES
:

http://stackoverflow.com/questions/7230893/android-search-with-fragments



USE THIS FOR ACTION BAR SEARCH

http://codetheory.in/adding-search-to-android/

*//*




//lot of changes made

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

*/
/**
 * Provides UI for the view with Tiles.
 *//*

public class SearchFragment extends Fragment {
    Spinner search_spinner;
    EditText search_text;
    Button search_btn;
    SharedPreferences sharedpreferences;
    static String uname;
    public String[] images = new String[100];
    public String[] desc = new String[100];
    protected View vView;
    //public String[] reporter = new String[100];
    public String[] upvotes = new String[100];
    //public String[] downvotes = new String[100];
    public String[] postid = new String[100];
    static ArrayList<ListItem> searchData = new ArrayList<>();

    public static String searchURL = Links.commonUrl + "/scripts/searchPost.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        sharedpreferences = this.getActivity().getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);
        uname= sharedpreferences.getString("username", null);
        */
/*this.vView=recyclerView;*//*




        //Stuff thats needed
        ContentAdapter adapter = new ContentAdapter(this.getActivity(),searchData);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return recyclerView;
    }

    private ArrayList<ListItem> getSearchData(String searchText, String searchCat) throws Exception {

        ArrayList<ListItem> searchMockData = new ArrayList<>();

        */
/*String type = searchCat;
        String value = searchText;*//*


        AsyncHttpPost async = new AsyncHttpPost();
        String jsonOutput=null;
        try{
            jsonOutput = async.execute(searchCat,searchText).get();
            System.out.println("after async task");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        JSONArray jsonArray = new JSONArray(jsonOutput);
        JSONObject jObject;

        for (int i = 0; i < jsonArray.length(); i++) {
            jObject = jsonArray.getJSONObject(i);
            images[i]=jObject.getString("filelocation");
            desc[i]=jObject.getString("description");
            upvotes[i]=jObject.getString("upvote_count");
            postid[i]=jObject.getString("postid");
            System.out.println("Data " + i + ":  " + images[i] + "    " + desc[i] +  "   " + upvotes[i] + "   " + postid[i] + "  " + i);

        }

        for (int i = 0; i < jsonArray.length(); i++) {
            System.out.println("Inside for loop");
            ListItem newsData = new ListItem();
            newsData.setUrl(images[i]);
            newsData.setHeadline(desc[i]);
            //newsData.setReporterName(reporter[i]);
            newsData.setUpvotes(upvotes[i]);
            //newsData.setDownvotes(downvotes[i]);
            newsData.setPostId(postid[i]);
            newsData.setDate("May 26, 2013, 13:35");
            searchMockData.add(newsData);
        }
        System.out.println("outside for loop");
        return searchMockData;
    }

    public static class AsyncHttpPost extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("inside async task");
            HashMap<String,String> data = new HashMap<>();
            data.put("type",params[0]);
            data.put("value",params[1]);
            RegisterUserClass ruc = new RegisterUserClass();
            String result = ruc.sendPostRequest(searchURL,data);

            return result;
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView headlineView;
        public View mView;
        //public TextView reporterNameView;
        //public TextView reportedDateView;
        public TextView upvotes;
        public TextView headline;
        //public Button dvt;
        //public ImageButton upvt;
        public String postid;
        public ImageView imageView;


        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.search, parent, false));

            mView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.search_grid_image);
            headline = (TextView)itemView.findViewById(R.id.search_tile_title);
            //upvotes = (TextView) itemView.findViewById(R.id.action_button);

            search_btn = (Button) itemView.findViewById(R.id.search_btn);
            search_spinner=(Spinner)itemView.findViewById(R.id.search_spinner);
            search_text=(EditText)itemView.findViewById(R.id.search_text);


            search_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View arg0) {

                    String searchString = search_text.getText().toString();
                    search_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
                    String searchCategory = search_spinner.toString();

                    try{
                        searchData = getSearchData(searchString,searchCategory);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }
    */
/**
     * Adapter to display recycler view.
     *//*

    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private int LENGTH;

        private Activity activity;
        private ArrayList<ListItem> data;
        public ImageLoader imageLoader;


        public ContentAdapter(Activity c, ArrayList<ListItem> x) {
            activity = c;
            data = x;
            LENGTH=x.size();
            imageLoader = new ImageLoader(activity.getApplicationContext());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
            ViewHolder v = (ViewHolder) holder;
            ListItem newsItem = data.get(position);
            v.postid = newsItem.getPostid();
            v.headline.setText(newsItem.getHeadline());
            //v.upvotes.setText(newsItem.getUpvotes());
            //v.reporterNameView.setText(newsItem.getReporterName());
            ImageView image = v.imageView;

            System.out.println("Inside Adapter:");
            System.out.println("Image: " + position + "   " + v.postid + "   " + newsItem.getHeadline()+"   "+newsItem.getUrl());
            imageLoader.displayImage(newsItem.getUrl(), image);

        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}

*/
