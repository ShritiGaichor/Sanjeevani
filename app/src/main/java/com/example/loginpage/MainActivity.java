package com.example.loginpage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText lcontact, Password;
    Button Login, Register;
    String contact, lpass;
    private static final int PERMISSION_REQUEST_CODE=1;


    HttpURLConnection con = null;
    URL url = null;
    BufferedReader br = null;
    String line = null, output;
    StringBuffer str = new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login Page");


        lcontact = (EditText) findViewById(R.id.lcontact);
        Password = (EditText) findViewById(R.id.Password);
        Login = (Button) findViewById(R.id.Login);
        Register = (Button) findViewById(R.id.Register);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_DENIED)
                    {
                        Log.d("permission","permission denied");
                        String[] permissions={Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE};
                        requestPermissions(permissions,PERMISSION_REQUEST_CODE);
                    }
                }
                new login().execute();

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in2 = new Intent("com.example.loginpage.RegistrationForm");
                startActivity(in2);
            }
        });
    }


    class login extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            try {
                contact=lcontact.getText().toString();
                lpass=Password.getText().toString();
                String s1 = "http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=login&contact=" + contact + "&password=" + lpass;
                System.out.println("s1 "+s1);
                url = new URL(s1);
                con = null;
                con = (HttpURLConnection) url.openConnection();
                System.out.println("Connection successful");
                br = null;
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                line = "";
                System.out.println("line is" + line);
                str = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    str.append(line);
                }
                System.out.println(str.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            if (str.toString()!="0") {
                Toast.makeText(getApplicationContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                System.out.println("String " + str.toString());

                Intent in=new Intent(MainActivity.this,NavigationDrawer.class);
                in.putExtra("out",str.toString());
                startActivity(in);
            }
            else
                {
                Toast.makeText(getApplicationContext(), "Login failed!Please check your details...", Toast.LENGTH_LONG).show();
            }
        }
    }

}