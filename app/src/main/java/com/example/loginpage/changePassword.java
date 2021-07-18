package com.example.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class changePassword extends AppCompatActivity {

    EditText Cpassword,Npassword,RNpassword;
    Button Save,Cancel;
    String out3,pass2,pass22,pass3;

    HttpURLConnection con=null;
    URL url=null;
    BufferedReader br=null;
    String line=null,output, urlString;
    StringBuffer str=new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");

        Cpassword=findViewById(R.id.Cpassword);
        Npassword=findViewById(R.id.Npassword);
        RNpassword=findViewById(R.id.RNpassword);

        Save=findViewById(R.id.Save);
        Cancel=findViewById(R.id.Cancel);

        Bundle rs=getIntent().getExtras();
        out3=rs.getString("out2");
        System.out.println("ID is "+out3);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(Npassword.getText().toString().equals(RNpassword.getText().toString()))
                {
                    //id
                    final String loginID=out3.toString();

                    urlString="http://"+ Configuration.ip +":8081/myJSONclient/jsonPage.jsp?method=changePassword&id="+loginID+"&pass1="+Cpassword.getText().toString()+"&pass2="+Npassword.getText().toString();
                    System.out.println("URL S1 is "+urlString);
                    new SavePassword().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Both passwords are not matched!",Toast.LENGTH_LONG).show();
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent in=new Intent(changePassword.this,settings.class);
                startActivity(in);*/
                changePassword.super.finish();
            }
        });
    }


    class SavePassword extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            try
            {

                    url =new URL(urlString);

                    con=null;
                    con = (HttpURLConnection) url.openConnection();
                    System.out.println("Connection successful");
                    br=null;
                    br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                    line="";
                    System.out.println("line is"+line);
                    str=new StringBuffer();

                    while ((line=br.readLine())!=null)
                    {
                        str.append(line);
                    }
                    System.out.println(str.toString());
            }

            catch(Exception ex)
            {
                ex.printStackTrace();
            }



            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {


            if(str.toString().equals("1"))
            {
                System.out.println("String "+str.toString());
                Toast.makeText(getApplicationContext(),"Password changed successfully!",Toast.LENGTH_LONG).show();
            }
            if(str.toString().equals("0"))
            {
                System.out.println("String "+str.toString());
                Toast.makeText(getApplicationContext(), "Please check your current password.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
