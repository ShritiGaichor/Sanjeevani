package com.example.loginpage;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class userInfo extends AppCompatActivity
{
    TextView Uname,Ucontact,Ucity,Ubloodgroup;
    ImageButton call,msg;
    String Y,M,D;


    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setTitle("User Information");
        Uname=findViewById(R.id.Uname);
        Ucontact=findViewById(R.id.Ucontact);
        Ucity=findViewById(R.id.Ucity);
        Ubloodgroup=findViewById(R.id.Ubloodgroup);

        call=findViewById(R.id.call);
        //msg=findViewById(R.id.msg);
        new showUserInfo().execute();

        Bundle rs=getIntent().getExtras();
        uid=rs.getString("id");
        System.out.println("ID IS "+uid);



        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent callin=new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",M,null));
                startActivity(callin);
            }
        });




/*
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String msgContent="I want a blood donor.If you are available then reply me.";
                Intent in=new Intent(getApplicationContext(),userInfo.class);
                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(),0,in,0);

                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(M,null,msgContent,pi,null);
                Toast.makeText(getApplicationContext(), "Message sent successfully!", Toast.LENGTH_SHORT).show();
            }
        });
        */



    }

    class showUserInfo extends AsyncTask<String,Integer,String>
    {
        StringBuffer str=new StringBuffer();
        ArrayList<String> keylist=new ArrayList<String>();
        String[] keyArray;

        @Override
        protected String doInBackground(String... strings)
        {
            try {
                URL url = new URL("http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=getAccountInfo&id=" + uid);

                //URL url = new URL("http://192.168.42.159:8081/myJSONclient/jsonPage.jsp?method=getAccountInfo&id="+uid);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.println("Connection");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null)
                {
                    str.append(line);
                }
                System.out.println(str.toString());
                JSONArray jsonArray = new JSONArray(str.toString());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    Iterator it = obj.keys();
                    System.out.println("Keys are " + it);
                    while (it.hasNext()) {
                        String key = it.next().toString();
                        keylist.add(obj.getString(key));
                    }
                }
                System.out.println("keylist2= " + keylist);

                String Cname=keylist.get(0).replace("'","");
                Uname.append(" "+Cname);

                String ConNum=keylist.get(3);
                Ucontact.append(" "+ConNum);

                String cCity=keylist.get(5).replace("'","");
                Ucity.append(" "+cCity);

                String cBloodGroup=keylist.get(6).replace("'","");
                Ubloodgroup.append(" "+cBloodGroup);

                final String number=Ucontact.getText().toString();
                System.out.println("Number "+number);

                StringTokenizer st=new StringTokenizer(number," ");
                if(st.hasMoreTokens())
                {
                    Y=st.nextToken();
                    System.out.println("Y "+Y);
                }
                if(st.hasMoreTokens())
                {
                    M=st.nextToken();
                    System.out.println("M "+M);

                }


            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

    }

}
