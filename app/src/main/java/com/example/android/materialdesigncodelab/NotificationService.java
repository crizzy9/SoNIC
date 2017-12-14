package com.example.android.materialdesigncodelab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class NotificationService extends Service {
    NotificationManager manager;
    Notification myNotication;

    private static final String LOGIN_URL = Links.commonUrl+ "/scripts/checkStatus.php";

    private String username;
    static SharedPreferences sharedpreferences;

    static JSONArray jArray = new JSONArray();
    public String posts[]= new String[10];

    public NotificationService() {

        Toast.makeText(getApplicationContext(),"Toast Created",Toast.LENGTH_LONG).show();
        System.out.println("TOAST CREATED");
        try{

            Context c = getApplicationContext();
            sharedpreferences = c.getSharedPreferences("sonicPrefs",Context.MODE_PRIVATE);
            username=sharedpreferences.getString("username", null);
            Toast.makeText(getApplicationContext(), "Username ==> "+username, Toast.LENGTH_LONG).show();
//            createNotification();
            sendNotification("SONIC","Shyam is a chu",6);
        }catch(Exception e){ e.printStackTrace();}
    }

    private void createNotification() {
        Notification1 nt = new Notification1();
        try {
            String xx = nt.execute(username).get();

        JSONArray jsonArray = new JSONArray(xx);
        //System.out.println("on execute:"+result);
        //ArrayList<String> array = new ArrayList<String>();

        jArray = jsonArray;
        System.out.println("JSONNNNN = " + xx + " : " + jArray.length());
        JSONObject jObject;

        for (int i = 0; i < jArray.length(); i++) {
            jObject = jArray.getJSONObject(i);
            posts[i] = jObject.getString("postid");
            System.out.println("DATA " + (i + 1) + " = " + posts[i]);
        }
    } catch (JSONException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    } catch (ExecutionException e) {
        e.printStackTrace();
    }


        for (int i = 0; posts[i] != null; i++) {
        System.out.println("POST with postid=" + posts[i] + " is sent to BMC");
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class Notification1 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            for (int i = 0; posts[i] != null; i++) {
                Toast.makeText(getApplicationContext(), "POST with postid=" + posts[i] + " is sent to BMC", Toast.LENGTH_LONG).show();
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                sendNotification("S.O.N.I.C.", "POST with postid=" + posts[i] + " is sent to BMC", Integer.parseInt(posts[i]));

            }
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> data = new HashMap<>();
            data.put("username",params[0]);
            RegisterUserClass ruc = new RegisterUserClass();
            String result = ruc.sendPostRequest(LOGIN_URL,data);
            return result;
        }
    }

    private void sendNotification(String notificationTitle, String notificationMessage, int PID){
        Intent intent = new Intent(NotificationService.this, TileContentFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(NotificationService.this);

        builder.setAutoCancel(true);
        builder.setTicker("New Message from S.O.N.I.C.");
        builder.setContentTitle(notificationTitle);
        builder.setContentText(notificationMessage);
        builder.setSmallIcon(R.drawable.ic_crce);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(true);
        builder.build();

        myNotication = builder.getNotification();
        manager.notify(PID, myNotication);
        }



}

