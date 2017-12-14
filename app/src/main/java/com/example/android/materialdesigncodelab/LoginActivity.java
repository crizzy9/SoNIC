package com.example.android.materialdesigncodelab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginActivity extends Activity {

	public static final String USER_NAME = "USER_NAME";

	public static final String PASSWORD = "PASSWORD";

	private static final String LOGIN_URL = Links.commonUrl+ "/scripts/checkLogin.php";

	private EditText editTextUserName;
	private EditText editTextPassword;
	public Button buttonLogin;
	UserSessionManager session;
	SharedPreferences sharedpreferences;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA I am in LoginActivity");
		/*String sss = getMD5Hasher("12345");
		System.out.println("test :    "+sss);*/
		System.out.println(""+MD5Hasher.getMD5Hasher("12345"));
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA I am in LoginActivity2");

		// User Session Manager
        session = new UserSessionManager(getApplicationContext());
		sharedpreferences = getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);

		// get Email, Password input text
		editTextUserName = (EditText) findViewById(R.id.username);
		editTextPassword = (EditText) findViewById(R.id.password);
		Intent i= getIntent();

		if(session.checkLogin() )
			finish();


		Toast.makeText(getApplicationContext(),
        		"User Login Status: " + session.isUserLoggedIn(), 
        		Toast.LENGTH_LONG).show();
        
        
        // User Login button
        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);

        System.out.println("before button");
        // Login button click event
        buttonLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// Get username, password from EditText
//				String username = txtUsername.getText().toString();
//				String password = txtPassword.getText().toString();

				System.out.println("1");
				Toast.makeText(getApplicationContext(),"i am here 1",Toast.LENGTH_LONG).show();

				String username = editTextUserName.getText().toString().trim();
				String password = editTextPassword.getText().toString().trim();
				System.out.println("BEFOREEEEEE : " + password);
				//String sss = getMD5Hasher("12345");
				//System.out.println("test :    "+sss);
		//		password = getMD5Hasher(password);
				//String x = MD5Hasher.getMD5Hasher(password);
				System.out.println("AFTEEEEERRRRRR : "+password);
				userLogin(username,password);

				// Validate if username, password is filled				
				//if(username.trim().length() > 0 && password.trim().length() > 0){
					
					// For testing puspose username, password is checked with static data
					// username = admin
					// password = admin
					
//					if(username.equals("admin") && password.equals("admin")){
//
//						// Creating user login session
//						// Statically storing name="Android Example"
//						// and email="androidexample84@gmail.com"
//						session.createUserLoginSession("Android Example", "androidexample84@gmail.com");
//
//						// Starting MainActivity
//						Intent i = new Intent(getApplicationContext(), MainActivity.class);
//						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//						// Add new Flag to start new Activity
//						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(i);
//
//						finish();
//
//					}else{
//
//						// username / password doesn't match
//						Toast.makeText(getApplicationContext(), "Username/Password is incorrect", Toast.LENGTH_LONG).show();
//
//					}
				//else {
					
					// user didn't entered username or password
				//	Toast.makeText(getApplicationContext(), "Please enter username and password", Toast.LENGTH_LONG).show();
					
				//}
				
			}

			/*private String getMD5Hasher(String input) {
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
				class UserLoginClass extends AsyncTask<String,Void,String> {
					ProgressDialog loading;
					@Override
					protected void onPreExecute() {
						System.out.println("4");
						super.onPreExecute();
						loading = ProgressDialog.show(LoginActivity.this,"Please Wait",null,true,true);
					}

					@Override
					protected void onPostExecute(String s) {
						super.onPostExecute(s);
						loading.dismiss();
						System.out.println(" 2 "+s);

						if(s.equalsIgnoreCase("User Found")){

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putString("username", username);
							editor.apply();
							session.createUserLoginSession(username);


		/*					//For notifications
							Toast.makeText(getApplicationContext(), "SSSSSSSSSSSSSSSSSSSSSSSSSSSSS Before Intent", Toast.LENGTH_LONG).show();
							System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSS Before Intent");
							Intent intent2 = new Intent(getApplicationContext(), NotificationService.class);
							startService(intent2);
							System.out.println("SSSSSSSSSSSSSSSSSSSSSSSService has been called");
*/


							// Starting MainActivity
						Intent i = new Intent(getApplicationContext(), MainActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							System.out.println("MMMMMMMMMMMMMMMMMM MAin activity has been initiated");

							// Add new Flag to start new Activity
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(i);
//
						finish();











						//	Toast.makeText(getApplicationContext(),"Preference is: "+editor.toString(),Toast.LENGTH_LONG).show();
							//System.out.println("Preference is:" + editor.toString());
						//	System.out.println(sharedpreferences.getString("username", null));
						//	Intent intent = new Intent(ActivityLogin.this,MainActivity.class);
						//	intent.putExtra(USER_NAME,username);
						//	startActivity(intent);
						}else{
							Toast.makeText(LoginActivity.this,s,Toast.LENGTH_LONG).show();
						}
					}

					@Override
					protected String doInBackground(String... params) {
						System.out.println("3");
						HashMap<String,String> data = new HashMap<>();
						data.put("username",params[0]);
						System.out.println("before hash");
						//String s = MD5Hasher.getMD5Hasher(params[1]);

						//System.out.println("in do in background : Hash is: "+s);
						data.put("password",params[1]);

						RegisterUserClass ruc = new RegisterUserClass();

						String result = ruc.sendPostRequest(LOGIN_URL,data);

						return result;
					}


				}

				UserLoginClass ulc = new UserLoginClass();
				ulc.execute(username,password);
			}
		});
    }
	@Override
	public void onBackPressed() {
		// Do Here what ever you want do on back press;
	}
}