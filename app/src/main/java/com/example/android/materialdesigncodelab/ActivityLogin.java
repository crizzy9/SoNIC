//Activity Needed-Alex


package com.example.android.materialdesigncodelab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener{

    public static final String USER_NAME = "USER_NAME";

    public static final String PASSWORD = "PASSWORD";

    private static final String LOGIN_URL = Links.commonUrl+ "/scripts/checkLogin.php";

    private EditText editTextUserName;
    private EditText editTextPassword;
    private Button buttonLogin;
    UserSessionManager session;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA I am in ActivityLogin");
        // User Session Manager
             session = new UserSessionManager(getApplicationContext());

             sharedpreferences = getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);

            editTextUserName = (EditText) findViewById(R.id.username);
            editTextPassword = (EditText) findViewById(R.id.password);

            buttonLogin = (Button) findViewById(R.id.buttonUserLogin);

            buttonLogin.setOnClickListener(this);
        }



    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private void login(){
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        System.out.println("BEFOREEEEEE : "+password);

        //MD5 is done in RegisterUserClass on password check.


        /*password = getMD5Hasher(password);*/
        /*String sss = getMD5Hasher("12345");
        System.out.println("test :    "+sss);*/
        //String x = MD5Hasher.getMD5Hasher(password);

        //System.out.println("AFTEEEEERRRRRR : "+x);
        userLogin(username, password);
    }
/*    private String getMD5Hasher(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }*/

    private void userLogin(final String username, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityLogin.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equalsIgnoreCase("User Found")){

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("username", username);
                    editor.apply();
                    session.createUserLoginSession(username);


        /*            //For notifications
                    Toast.makeText(getApplicationContext(), "SSSSSSSSSSSSSSSSSSSSSSSSSSSSS Before Intent", Toast.LENGTH_LONG).show();
                    System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS Before Intent");
                    Intent intent2 = new Intent(getApplicationContext(), NotificationService.class);
                    startService(intent2);
                    System.out.println("SSSSSSSSSSSSSSSSSSSSSSSService has been called");

*/

                    Toast.makeText(getApplicationContext(),"Preference is: "+editor.toString(),Toast.LENGTH_LONG).show();
                    //System.out.println("Preference is:" + editor.toString());
                    System.out.println(sharedpreferences.getString("username", null));
                    Intent intent = new Intent(ActivityLogin.this,MainActivity.class);
                    intent.putExtra(USER_NAME,username);
                    startActivity(intent);
                }else{
                    Toast.makeText(ActivityLogin.this,s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("username",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }


        }

        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username,password);
    }


    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            login();
        }
    }
}
