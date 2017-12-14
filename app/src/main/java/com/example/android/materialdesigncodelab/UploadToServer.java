package com.example.android.materialdesigncodelab;

import android.Manifest;
import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
//this may only work when the server is pressed after the upload button


public class UploadToServer extends Activity {
    private final int SELECT_PHOTO = 1;
    private ImageView imageView;
    private String s;
    String uploadFileName = null, username;
    SharedPreferences sharedpreferences;
    String upLoadServerUri = null;

    TextView messageText;
    String upServerUri = null;
    String longitudex, latitudex;

    Button getlocation;
    TextView text;
    TextView table;
    private static final String TAG = "Debug";
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;

    String allLoc = "ADDRESS:\n";
    protected String lane = null, cityName = null, area = null, pinCode = null, district = null, state = null, country = null;


    EditText desc, title;
    Button uploadButton;
    String names;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    Spinner loc, cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_server);

        Button button;
        username = null;
        uploadButton = (Button) findViewById(R.id.uploadButton);
        imageView = (ImageView) findViewById(R.id.img);
        messageText = (TextView) findViewById(R.id.messageText);
        desc = (EditText) findViewById(R.id.desc);
        title = (EditText) findViewById(R.id.title);
        loc = (Spinner) findViewById(R.id.spinner_loc);
        cat = (Spinner) findViewById(R.id.spinner_cat);
        Intent intent = getIntent();


        sharedpreferences = getSharedPreferences("sonicPrefs", Context.MODE_PRIVATE);
        System.out.println(sharedpreferences.getString("username", null));
        username = intent.getStringExtra(ActivityLogin.USER_NAME);
        names = sharedpreferences.getString("username", null);
        System.out.println("XXXXX               " + username + "          XXXXXXXXXXXXXXXXXXX             " + names);
        //  addListenerOnButton();
        addListenerOnSpinnerItemSelection();
        //messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");
        upLoadServerUri = Links.commonUrl + "/scripts/UploadToServer.php";
        upServerUri = Links.commonUrl + "/scripts/addToServer.php";

        //  button = (Button) findViewById(R.id.button);

        uploadFileName = getIntent().getStringExtra("finalImage");
        Uri urix = Uri.parse(uploadFileName);
        imageView.setImageURI(urix);
        Toast.makeText(getApplicationContext(), "FINAL IMAGE : " + uploadFileName, Toast.LENGTH_LONG).show();

        //button.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uploadimg(v);
//            }
//        });

        getlocation = (Button) findViewById(R.id.getLoc);
        text = (TextView) findViewById(R.id.textView);
        table = (TextView) findViewById(R.id.textView2);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
/*        locationListener = new MyLocationListener();
                *//*if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }*//*
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);*/

        getlocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("inside loc button");

    //  Using GPS to get coordinates
                /*flag = displayGpsStatus();
                if (flag) {

                    Log.v(TAG, "onClick");

                    text.setText("Please!! move your device to"+
                            " see the changes in coordinates."+"\nWait..");

                    locationListener = new MyLocationListener();
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1,locationListener);

                } else {
                    text.setText("Gps Status!!"+ " Your GPS is: OFF");
                }
*/


                locationListener = new MyLocationListener();
/*                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }*/
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

            }
        });

        uploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(UploadToServer.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                messageText.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFileName);

                    }
                }).start();
            }
        });
    }

    public void uploadimg(View v){
        Intent intent = new Intent(Intent.ACTION_PICK);
        //intent.setType("content://media/internal/images/media");
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PHOTO);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream imageStream=null;
        switch (requestCode){
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try{
                        final Uri imageUri = data.getData();
                        imageStream = getContentResolver().openInputStream(imageUri);
                        System.out.println("image path is:" + imageStream + "\n" + imageUri);
                        Toast.makeText(getApplicationContext(), "image path is:" + imageStream + "\n" + imageUri, Toast.LENGTH_LONG).show();
                        // final Bitmap selectedImage = BitmapFactory.decodeStream((Uri)uploadFileName);

                        //  imageView.setImageURI();
                        uploadFileName = s;
                        //uploadFileName=getRealPathFromURI(imageUri);
                        //System.out.  println(uploadFileName);
                        //uploadFileName=imageUri.toString();
                        //uploadFileName=uploadFileName.split(":/")[1];
                    }
                    catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                    finally{
                        if(imageStream != null){
                            try{
                                imageStream.close();
                            }
                            catch(IOException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {


            Toast.makeText(getBaseContext(), "Location changed : Lat: " + loc.getLatitude() + " Lng: " + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            longitudex=longitude;
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            latitudex=latitude;
            Log.v(TAG, latitude);

/*        *//*----------to get City-Name from coordinates ------------- *//*
//            String lane = null, cityName = null, area = null, pinCode= null, district= null, state= null, country= null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.toString());
                }
                //     System.out.println("Lane : "+addresses.get(0).getAddressLine(0));
                //     System.out.println("Area : " +addresses.get(0).getAddressLine(1));
                //     System.out.println("Bigger Area : " +addresses.get(0).getAddressLine(2));
                //     System.out.println("Country : "+ addresses.get(0).getAddressLine(3));
                //     System.out.println("Country Code : " + addresses.get(0).getCountryCode());
                lane = "Lane: "+addresses.get(0).getFeatureName();
                cityName = "City: "+addresses.get(0).getLocality();
                area = "Station Area : " +addresses.get(0).getSubLocality();
                pinCode = "Pin Code : "+addresses.get(0).getPostalCode();
                district = "District : "+addresses.get(0).getSubAdminArea();
                state = "State : "+addresses.get(0).getAdminArea();
                country = "Country : " +addresses.get(0).getCountryName();



                //     System.out.println("Lane3 : "+addresses.get(0).getThoroughfare());

            } catch (IOException e) {
                e.printStackTrace();
            }*/


            String s = longitude + "\n" + latitude ;
            //allLoc ="ADDRESS:\n"+ lane+"\n"+cityName+"\n"+area+"\n"+pinCode+"\n"+district+"\n"+state+"\n"+country+"\n";
            text.setText(s);
            //table.setText(allLoc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }




    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            + uploadFileName);
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);


                //      conn.setRequestProperty("desc",desc.toString());
                //      conn.setRequestProperty("title",title.toString());


                System.out.println("XXXXXXXXXXXXXXXXXXX: UploadToServer: " + username + " " + desc.getText().toString() + " " + title.getText().toString() + " " + loc.getSelectedItem().toString() + " " + cat.getSelectedItem().toString());
                HashMap < String, String > data = new HashMap<String, String>();
                data.put("username", names );
                data.put("desc", desc.getText().toString());
                data.put("title", title.getText().toString());
                data.put("loc", loc.getSelectedItem().toString());
                data.put("cat", cat.getSelectedItem().toString());
                /*data.put("lane",lane.split(":")[1]);
                data.put("city",cityName.split(":")[1]);
                data.put("area",area.split(":")[1]);
                data.put("pincode",pinCode.split(":")[1]);
                data.put("district",district.split(":")[1]);
                data.put("state",state.split(":")[1]);
                data.put("country",country.split(":")[1]);*/
                data.put("longitude",longitudex.split(":")[1]);
                data.put("latitude",latitudex.split(":")[1]);
                System.out.println(data);
                //System.out.println(spinner1.getSelectedItem().toString());


                RegisterUserClass ruc = new RegisterUserClass();
                ruc.sendPostRequest(upServerUri, data);

                /*OutputStream os = conn.getOutputStream();

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write("username=" + names);
                writer.flush();
                writer.close();
                os.close();
*/
                dos = new DataOutputStream(conn.getOutputStream());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(dos,"UTF-8"));


                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\" uploaded_file\";filename="+ fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" http://www.androidexample.com/media/uploads/"
                                    +uploadFileName;

                            messageText.setText(msg);
                            Toast.makeText(UploadToServer.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                // the streams //
                fileInputStream.close();
                writer.flush();
                writer.close();
                dos.flush();
                dos.close();

                /*OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                //System.out.println(postDataParams);
                writer.write("username=" + names);
                //System.out.println(getPostDataString(postDataParams));

                writer.flush();
                writer.close();

                os.close();*/

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(UploadToServer.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(UploadToServer.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload server Exception", "Exception : " + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
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
        }

        return super.onOptionsItemSelected(item);
    }



    public void addListenerOnSpinnerItemSelection() {
        loc = (Spinner) findViewById(R.id.spinner_loc);
        loc.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        cat = (Spinner) findViewById(R.id.spinner_cat);
        cat.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    @Override
    public void onBackPressed() {
        Intent ix = new Intent(this, MainActivity.class);
        startActivity(ix);
    }
}
