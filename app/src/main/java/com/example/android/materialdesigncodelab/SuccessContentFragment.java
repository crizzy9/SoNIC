//Activity Needed-Shyam


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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


/**
 * Provides UI for the view with Tile.
 */
public class SuccessContentFragment extends Fragment {
    public String[] images = new String[100];
    public String[] desc = new String[100];
    public String[] reporter = new String[100];
    public String[] upvotes = new String[100];
    //public String[] downvotes = new String[100];
    public String[] postid = new String[100];
    public static JSONObject jObject;
    static JSONArray jArray = new JSONArray();
    static LazyImageLoadAdapter adapter;
    static String uname;
    static Context cont;
    static ListView list;
    static Activity a;
    public static View xView;
    static ArrayList<ListItem> listData = new ArrayList<>();
    static SharedPreferences sharedpreferences;
    public static String uploadURL=Links.commonUrl+"/scripts/";
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;


/*    public static TileContentFragment newInstance(){
        return new TileContentFragment();
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);


        cont = getActivity().getApplicationContext();

        sharedpreferences = this.getActivity().getSharedPreferences("sonicPrefs",Context.MODE_PRIVATE);
        uname=sharedpreferences.getString("username",null);
        System.out.println(sharedpreferences.getString("username",null));
        //list = (ListView) container.findViewById(R.id.layout1);
        //a=this.getActivity();


/*

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.INTERNET)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
*/



        try {
            listData = getListData();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.getActivity(), listData);
        /*adapter = new LazyImageLoadAdapter(getActivity(),listData,"alex");
        System.out.println(adapter);
         list.setAdapter(adapter)*/;

        ContentAdapter adapter = new ContentAdapter(this.getActivity(),listData);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
        //return list;
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

*/

    private ArrayList<ListItem> getListData() throws Exception {
        ArrayList<ListItem> listMockData = new ArrayList<>();
        //String[] images = getResources().getStringArray(R.array.images_array);
        //String[] headlines = getResources().getStringArray(R.array.headline_array);
        //try {
        AsyncHttpPost asyncHttpPost1 = new AsyncHttpPost();
        try {
            String ketan = asyncHttpPost1.execute(Links.commonUrl + "/scripts/successPosts.php").get();
            System.out.println(ketan + "  ketan printed this");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        System.out.println("Reached here");


        for (int i = 0; i < jArray.length(); i++) {
            jObject = jArray.getJSONObject(i);

            images[i] = jObject.getString("filelocation");
            desc[i] = jObject.getString("description");
            reporter[i] = jObject.getString("userid");
            upvotes[i] = jObject.getString("upvote_count");
            //downvotes[i] = jObject.getString("downvote_count");
            postid[i] = jObject.getString("postid");
            System.out.println("Data " + i + ":  " + images[i] + "    " + desc[i] + "   " + reporter[i] + "   " + upvotes[i] + "   " + postid[i] + "  " + i);
        }


        //Toast.makeText(getApplicationContext(),"krtan",Toast.LENGTH_LONG).show();
        for (int i = 0; i < jArray.length(); i++) {
            System.out.println("Inside for loop");
            ListItem newsData = new ListItem();
            newsData.setUrl(images[i]);
            newsData.setHeadline(desc[i]);
            newsData.setReporterName(reporter[i]);
            newsData.setUpvotes(upvotes[i]);
            //newsData.setDownvotes(downvotes[i]);
            newsData.setPostId(postid[i]);
            newsData.setDate("May 26, 2013, 13:35");
            listMockData.add(newsData);
        }
        System.out.println("outside for loop");
        return listMockData;
    }

    public static class AsyncHttpPost extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                //  conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();
// sending username to php
                //List<NameValuePair> parameter = new ArrayList<>();
                //parameter.add(new BasicNameValuePair("username",sharedpreferences.getString("username",null)));



                //HashMap<String,String> parameter = new HashMap<>();
                //parameter.put("username",sharedpreferences.getString("username",null));


                String user="username="+sharedpreferences.getString("username",null);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
//                System.out.println(sharedpreferences.getString("username",null));
//                writer.write("" + sharedpreferences.getString("username", null));
                writer.write(user);
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

                JSONArray jsonArray = new JSONArray(result.toString());
                //System.out.println("on execute:"+result);
                //ArrayList<String> array = new ArrayList<String>();

                jArray = jsonArray;
                conn.disconnect();
                return result.toString();
            } catch (Exception e) {
                return e.toString();
            }
        }

        /*@Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            adapter = new LazyImageLoadAdapter(a,listData,"alex");
            System.out.println(adapter);
            list.setAdapter(adapter);

        }*/


        /*private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
        {
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            return result.toString();
        }
*/
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView headlineView;
        public View mView;
        public TextView reporterNameView;
        //public TextView reportedDateView;
        public TextView upvotes;
        //public Button dvt;
        public ImageButton upvt;
        public String postid;
        public ImageView imageView;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.success_card, parent, false));

            System.out.println("Inside ViewHolder");
            mView = itemView;
            xView = itemView;
            headlineView = (TextView) itemView.findViewById(R.id.card_text);
           // upvt = (ImageButton) itemView.findViewById(R.id.favorite_button);
            imageView = (ImageView) itemView.findViewById(R.id.card_image);
            upvotes = (TextView) itemView.findViewById(R.id.action_button);
            reporterNameView = (TextView) itemView.findViewById(R.id.card_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("postid",postid);
                    intent.putExtra("activity","success");
                    /*intent.putExtra("image",imageView);*/
                    context.startActivity(intent);
                }
            });
            // the upper code is to go to detail activity on image press

            // Adding Snackbar to Action Button inside card
            //its downvote button for now
            /*Button button = (Button) itemView.findViewById(R.id.action_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "Action is pressed",
                            Snackbar.LENGTH_LONG).show();
                }
            });
*/
            /*ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);*/
//            upvt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //int f = giveUpvote(postid,uname);
//                    giveUpvote(postid,uname);
//
//                    /*upvt.setEnabled(false);
//                    upvt.setImageResource(R.drawable.ic_up_blue_24dp);*/
//                    /*Snackbar.make(v, "You Upvoted This Post",
//                            Snackbar.LENGTH_LONG).show();*/
//
//                    /*if(f ==1){
//                        System.out.println("This is upvote flag:  "+ f);
//                        int m =(Integer.parseInt(upvotes.getText().toString())+1);
//                        System.out.println("upvote value:    "+m);
//                        upvotes.setText(m);
//                    }*/
//                }
//            });

//           ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
//            shareImageButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    giveDownvote(postid,uname);
//                    /*Snackbar.make(v, "You Downvoted This Post",
//                            Snackbar.LENGTH_LONG).show();*/
//                }
//            });
        }
    }


//    private static void giveUpvote(String postid, String username) {
//        //final int[] flag = new int[1];
//        class RegisterUser extends AsyncTask<String, Void, String> {
//            //ProgressDialog loading;
//            //int flag;
//            RegisterUserClass ruc = new RegisterUserClass();
//            @Override
//            protected String doInBackground(String... params) {
//                System.out.println("in background");
//                HashMap<String, String> data = new HashMap<String,String>();
//                data.put("postid",params[0]);
//                data.put("username",params[1]);
//                System.out.println("Upvote to:   " +params[0]+"    "+params[1]);
//
//                String result = ruc.sendPostRequest(uploadURL+"upvote.php",data);
//
//                return  result;
//            }
//
//            protected void onPostExecute(String s){
//                super.onPostExecute(s);
//                System.out.println("on post execute 1:   "+s);
//                if(s.equalsIgnoreCase("You have already upvoted this post")){
//                    System.out.println("post already upvoted");
//                    //flag[0] = 0;
//                    //Snackbar.make(snack,"You have already upvoted this post",Snackbar.LENGTH_LONG).show();
//                    //Toast.makeText(cont, "You have already upvoted this post", Toast.LENGTH_LONG).show();
//                    Snackbar.make(xView,"You have already upvoted this post",Snackbar.LENGTH_LONG).show();
//                }
//                else{
//                    System.out.println("post upvoted");
//                    //flag[0] =1;
//                    /*Snackbar.make(snack, "You Upvoted This Post",
//                            Snackbar.LENGTH_LONG).show();*/
//                    //Toast.makeText(cont,"You Upvoted This Post",Toast.LENGTH_LONG).show();
//
//                    Snackbar.make(xView,"You have upvoted this post",Snackbar.LENGTH_LONG).show();
//
//                }
//            }
//
//        }
//
//        RegisterUser ru = new RegisterUser();
//        ru.execute(postid,username);
//        /*System.out.println("async upload flag:   "+ flag[0]);
//        return flag[0];*/
//    }



    private static void giveDownvote(String postid, String username) {
        class RegisterUserx extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected String doInBackground(String... params) {
                System.out.println("in background");
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("postid",params[0]);
                data.put("username",params[1]);

                System.out.println("Downvote to:   " + params[0] + "    " + params[1]);

                String result = ruc.sendPostRequest(uploadURL+"downvote.php",data);

                return  result;
            }

            protected void onPostExecute(String k){
                super.onPostExecute(k);
                System.out.println("on post execute 2:   "+ k);
                if(k.equalsIgnoreCase("You have already downvoted this post")){
                    System.out.println("post already downvoted");
                    //Snackbar.make(snack,"You have already upvoted this post",Snackbar.LENGTH_LONG).show();
                    //Toast.makeText(cont, "You have already upvoted this post", Toast.LENGTH_LONG).show();
                    Snackbar.make(xView,"You have already downvoted this post",Snackbar.LENGTH_LONG).show();
                }
                else {
                    System.out.println("post downvoted");
                    /*Snackbar.make(snack, "You Upvoted This Post",
                            Snackbar.LENGTH_LONG).show();*/
                    //Toast.makeText(cont,"You Upvoted This Post",Toast.LENGTH_LONG).show();

                    Snackbar.make(xView,"You have downvoted this post",Snackbar.LENGTH_LONG).show();

                }
            }


        }



        RegisterUserx ru = new RegisterUserx();
        ru.execute(postid,username);

    }



    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private int LENGTH;

        private Activity activity;
        public String uname;
        private ArrayList<ListItem> data;
        public String post;
        private static LayoutInflater inflater = null;
        public ImageLoader imageLoader;
        public String uploadURL=Links.commonUrl+"/scripts/upvote.php";



        public ContentAdapter(Activity c, ArrayList<ListItem> x){
            activity = c;
            data = x;
            LENGTH=x.size();
            System.out.println("Inside Constructor");
            imageLoader = new ImageLoader(activity.getApplicationContext());
            System.out.println("here");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
            System.out.println("lalalallala");
            ViewHolder v = (ViewHolder) holder;
            ListItem newsItem = data.get(position);
            v.postid = newsItem.getPostid();
            v.headlineView.setText(newsItem.getHeadline());
            v.upvotes.setText(newsItem.getUpvotes());
            v.reporterNameView.setText(newsItem.getReporterName());
            ImageView image = v.imageView;


            System.out.println("Inside Adapter:");
            System.out.println("Image: " + position + "   " + v.postid + "   " + newsItem.getHeadline());
            imageLoader.displayImage(newsItem.getUrl(), image);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
