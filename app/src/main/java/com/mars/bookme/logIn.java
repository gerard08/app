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

public class logIn extends AppCompatActivity {
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String pass;
    private boolean back;
    private String email;
    private String password;
    private Boolean New;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        check();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loginlayout);
        LinearLayout your_Layout = findViewById(R.id.main_container);
        AnimationDrawable animationDrawable = (AnimationDrawable) your_Layout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

    }
    public void loginclick(View view){
        login();
    }
    public void login() {
        post(this);
        get();
    }
    public void register(View view){
        Intent i = new Intent(getBaseContext(), register.class);
        startActivity(i);}

    private void post(Context context){
        if(back==true){
            final EditText mail = findViewById(R.id.email);
            final EditText passwd =findViewById(R.id.passwd);
            email = mail.getText().toString();
            password = passwd.getText().toString();
            Toast.makeText(context, "variables", Toast.LENGTH_SHORT).show();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest sr = new StringRequest(Request.Method.POST,"http://marsdevelopers.ddns.net/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(logIn.this, "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("page","login");
                params.put("mail",email);
                params.put("pass",password);
                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        queue.add(sr);
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
                        pass = response.substring(0);

                        if(pass.equals("True")){
                            Intent i = new Intent(getBaseContext(), Main.class);
                            if(New==true)save();
                            startActivity(i);
                            finish();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toaster("Check your username and password or your connection");
            }

        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void toaster(String jelly){
        Toast.makeText(this, jelly, Toast.LENGTH_SHORT).show();
    }
    private void check(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User", 0);
        SharedPreferences.Editor editor = pref.edit();
        email= pref.getString("mail", "null");
        password= pref.getString("pass", "null");
        New = pref.getBoolean("new",true);
        if(email.equals("null")){
            back=true;
        }
        else{
            back=false;
            login();
        }

    }
    private void save(){
        final EditText mail = findViewById(R.id.email);
        final EditText passwd =findViewById(R.id.passwd);
        email = mail.getText().toString();
        password = passwd.getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("User", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("mail",email);
        editor.putBoolean("new",false);
        editor.putString("pass",password);
        editor.commit();
    }
}