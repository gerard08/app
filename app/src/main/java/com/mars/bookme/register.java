package com.mars.bookme;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class register extends AppCompatActivity {
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.register);
        LinearLayout your_Layout = findViewById(R.id.main_container);
        AnimationDrawable animationDrawable = (AnimationDrawable) your_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

    public void login(View view) {
        post(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), logIn.class);
        startActivity(i);
        finish();
    }


    private void post(Context context) {
        final EditText Name = findViewById(R.id.name);
        final EditText Surname = findViewById(R.id.surname);
        final EditText Mail = findViewById(R.id.email);
        final EditText Passw = findViewById(R.id.passwd);
        final EditText Passw2 = findViewById(R.id.passwd2);

        if (Passw.getText().toString().equals(Passw2.getText().toString())) {

            RequestQueue queue = Volley.newRequestQueue(context);
            StringRequest sr = new StringRequest(Request.Method.POST, "http://marsdevelopers.ddns.net/", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Name", Name.getText().toString());
                    params.put("surname", Surname.getText().toString());
                    params.put("mail", Mail.getText().toString());
                    params.put("pass", Passw.getText().toString());
                    params.put("page", "register");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(sr);
            get();
        } else {
            Toast.makeText(context, "ERROR, check your password", Toast.LENGTH_SHORT).show();
        }

    }
    private void get (){
        final TextView mTextView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://marsdevelopers.ddns.net/";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        answer = response.substring(0);
                        if(answer.equals("Registered")){
                            save();
                            //toaster(answer);
                            toaster("Registered!!");

                            Intent i = new Intent(getBaseContext(), logIn.class);
                            startActivity(i);
                            finish();

                        }
                        else if(answer.equals("RegisterError")){
                            toaster("It seems like this mail is used...");

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toaster("Oops, check your connection...");
            }

        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
    private void toaster(String jelly){
        Toast.makeText(this, jelly, Toast.LENGTH_SHORT).show();
    }
    private void save(){
        final EditText Mail = findViewById(R.id.email);
        final EditText Passw = findViewById(R.id.passwd);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mail",Mail.getText().toString());
        editor.putString("pass",Passw.getText().toString());
        editor.commit();
    }
}