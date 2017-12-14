//Activity Needed-Alex




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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class DetailActivity extends AppCompatActivity {


    public TextView headlineView;
    //public View mView;
    public TextView reporterNameView;
    //public TextView reportedDateView;
    public TextView upvotes;
    public TextView loc;
    //public Button dvt;
    //public ImageButton upvt;
    //public String postid;
    public ImageLoader imageLoader;
    public ImageView imageView;
    String m;
    public static String URLx=Links.commonUrl+"/scripts/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String postid = bundle.getString("postid");
        String activity = bundle.getString("activity");
        System.out.println(postid);
        //newString = (String) savedInstanceState.getSerializable("headline");

        reporterNameView = (TextView) findViewById(R.id.detail_uploader);
        headlineView = (TextView) findViewById(R.id.detail_desc);
        imageView = (ImageView) findViewById(R.id.detail_image);
        upvotes = (TextView) findViewById(R.id.detail_upvotes);
        imageLoader = new ImageLoader(getApplicationContext());
        loc = (TextView) findViewById(R.id.detail_location);


        JSONArray jarr=null;

        if(activity.equals("feed")){
            jarr = TileContentFragment.jArray;
            System.out.println(" tile  detail activity: "+ jarr);
        }
        else if(activity.equals("success")){
            jarr = SuccessContentFragment.jArray;
            System.out.println("Success detail activity" + jarr);
        }
        else if(activity.equals("profile")){
            jarr=ProfileContentFragment.jsonArray;
            System.out.println("Profile detail activity"+jarr);
        }
        else if(activity.equals("search")){
            jarr=SearchResultFragment.jsonArray;
            System.out.println("search detail activity"+jarr);
        }


        JSONObject jobj;
        int pos;

        for(int i = 0;i<jarr.length();i++){
            System.out.println("inside looppppp1");
            try {
                System.out.println("inside try");
                jobj = jarr.getJSONObject(i);
                if(jobj.getString("postid").equals(postid)){
                    System.out.println("inside if");
                    pos =i;
                    System.out.println("this is the position:   "+pos);
                    System.out.println("detail activity 2: "+ jobj);
                    headlineView.setText(jobj.getString("description"));
                    reporterNameView.setText(jobj.getString("userid"));
                    upvotes.setText(jobj.getString("upvote_count"));
                    String image = jobj.getString("filelocation");
                    imageLoader.displayImage(image,imageView);

                    AsyncHttpPost async = new AsyncHttpPost();
                    try {
                        m = async.execute(URLx + "detailPost.php", postid).get();
                        System.out.println("inside detail activity ::::::   "+m);
                    }

                    catch (Exception e){
                        e.printStackTrace();
                    }
                    JSONArray jArray = new JSONArray(m);
                    JSONObject jObject;
                    String lane =  null;
                    String area = null;
                    String city = null;

                    for (int j = 0; j < jArray.length(); j++) {
                        jObject = jArray.getJSONObject(j);
                        lane = jObject.getString("lane");
                        area = jObject.getString("area");
                        city = jObject.getString("city");
                    }

                    loc.setText(lane+", "+area+", "+city);


                    break;
                };
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        // Set title of Detail page
        //collapsingToolbar.setTitle(getString(R.string.item_title));
        collapsingToolbar.setTitle(postid);


    }


    public static class AsyncHttpPost extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                String postid = "postid="+params[1];
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //  conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();


                //String user = "username=" + sharedpreferences.getString("username", null);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postid);
                writer.flush();
                writer.close();
                os.close();
                // sending user name to php finish

                InputStream input = conn.getInputStream();

                System.out.println(conn.getResponseCode());
                System.out.println("***************************");
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                System.out.println(result.toString());

                //JSONArray jsonArray = new JSONArray(result.toString());
                //System.out.println("on execute:"+result);
                //ArrayList<String> array = new ArrayList<String>();

                //jArray = jsonArray;
                conn.disconnect();
                return result.toString();
            } catch (Exception e) {
                return e.toString();
            }
        }
    }
}
