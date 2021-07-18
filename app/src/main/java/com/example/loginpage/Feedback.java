package com.example.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Feedback extends AppCompatActivity {
    String out2;
    EditText info;
    String info1;
    Button b1;
    HttpURLConnection con = null;
    URL url = null;
    BufferedReader br = null;
    String line = null, output;
    StringBuffer str = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setTitle("Feedback");
        Bundle rs = getIntent().getExtras();
        out2 = rs.getString("out1");
        System.out.println("ID is " + out2);

        info=findViewById(R.id.info);

        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                new addFeedback().execute();
            }
        });



    }


    class addFeedback extends AsyncTask<String, Integer, String>
    {
        @Override
        protected String doInBackground(String... strings) {

            info1=info.getText().toString();
            System.out.println("Information: "+info1);

            try {
                String s1 = "http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=addFeedback&id="+out2+"&info=\'"+info1+"\'";
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

            if (Integer.parseInt(str.toString())==1)
            {
                Toast.makeText(getApplicationContext(), "Feedback sent successfully!", Toast.LENGTH_LONG).show();
                System.out.println("String " + str.toString());
            }
            else
                {
                Toast.makeText(getApplicationContext(), "Feedback sending failed!", Toast.LENGTH_LONG).show();
                }
        }
    }
}