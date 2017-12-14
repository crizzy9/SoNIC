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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides UI for the view with Tiles.
 */
    public class SearchResultFragment extends Fragment {




    SharedPreferences sharedpreferences;
    static String uname;
    String type;
    String value;

    public String[] images = new String[100];
    public String[] desc = new String[100];
    //public String[] reporter = new String[100];
    public String[] upvotes = new String[100];
    //public String[] downvotes = new String[100];
    public String[] postid = new String[100];
    public static JSONArray jsonArray;
    static ArrayList<ListItem> userData = new ArrayList<>();

    public static String userURL = Links.commonUrl + "/scripts/searchPost.php";


    /*public SearchResultFragment(String searchString,String searchCategory){
        type = searchString;
        value = searchCategory;
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);

        sharedpreferences = this.getActivity().getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);
        uname= sharedpreferences.getString("username", null);


        ListItem newsItem  = SearchFragment.gridMockData.get(0);
        /*String typex = newsItem.getType();
        String valuex = newsItem.getValue();
        System.out.println(typex+"  "+valuex);
*/
        type = newsItem.getType();
        value = newsItem.getValue();
        System.out.println(type+"     "+value);


        try{
            userData = getUserData(type,value);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        ContentAdapter adapter = new ContentAdapter(this.getActivity(),userData);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        // Set padding for Tiles
        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return recyclerView;
    }

    private ArrayList<ListItem> getUserData(String type,String value) throws Exception {

        ArrayList<ListItem> userMockData = new ArrayList<>();

        AsyncHttpPost async = new AsyncHttpPost();
        String jsonOutput=null;
        try{
            jsonOutput = async.execute(type,value).get();
            System.out.println("after async task");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(jsonOutput);
        jsonArray = new JSONArray(jsonOutput);
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
            userMockData.add(newsData);
        }
        System.out.println("outside for loop");
        return userMockData;
    }

    public static class AsyncHttpPost extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            System.out.println("inside async task");
            HashMap<String,String> data = new HashMap<>();
            data.put("type",params[0]);
            data.put("value",params[1]);
            RegisterUserClass ruc = new RegisterUserClass();
            String result = ruc.sendPostRequest(userURL,data);

            return result;
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

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
            super(inflater.inflate(R.layout.activity_profile, parent, false));

            mView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.grid_image);
            headline = (TextView)itemView.findViewById(R.id.tile_title);
            //upvotes = (TextView) itemView.findViewById(R.id.action_button);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("postid",postid);
                    intent.putExtra("activity","search");
                    /*intent.putExtra("image",imageView);*/
                    context.startActivity(intent);
                }
            });

        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
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

