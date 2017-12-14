



package com.example.android.materialdesigncodelab;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartActivity extends Activity implements OnClickListener {

	Button btnSignIn;
	Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		Intent i = null;
		switch(v.getId()){
			case R.id.btnSingIn:


				i = new Intent(this,LoggerClass.class);
				break;
			case R.id.btnSignUp:
				i = new Intent(this,SignUpActivity.class);
				break;
		}
		startActivity(i);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		// Do Here what ever you want do on back press;
	}

    
}
