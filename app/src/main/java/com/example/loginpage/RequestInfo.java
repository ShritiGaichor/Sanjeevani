package com.example.loginpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class RequestInfo extends AppCompatActivity {
    String uid,tid;
    TextView Rid, Rnumber, Rdate, Rplace,Rname;
    Button accept,cancel;

    String id, number, date, place;
    HttpURLConnection con=null;
    URL url=null;
    BufferedReader br=null;
    String line=null,output;
    StringBuffer str=new StringBuffer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_info);
        setTitle("Request Information");

        Bundle rs = getIntent().getExtras();
        uid = rs.getString("id");
        tid=rs.getString("out2");

        System.out.println("ID IS " + uid);

        Rid = findViewById(R.id.Rid);
        Rdate = findViewById(R.id.Rdate);
        Rnumber = findViewById(R.id.Rnumber);
        Rplace = findViewById(R.id.Rplace);
        Rname=findViewById(R.id.Rname);
        accept = findViewById(R.id.accept);
        cancel=findViewById(R.id.cancel);

        new showUserInfo().execute();
        new showRequests().execute();

        Rid.append(uid);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new acceptRequest().execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new cancelRequest().execute();
            }
        });
    }

    class showUserInfo extends AsyncTask<String, Integer, String> {
        StringBuffer str = new StringBuffer();
        ArrayList<String> keylist = new ArrayList<String>();
        String[] keyArray;
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=getAccountInfo&id=" + uid);
                //URL url = new URL("http://192.168.42.159:8081/myJSONclient/jsonPage.jsp?method=getAccountInfo&id="+uid);
                System.out.println(url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.println("Connection");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line = "";
                while ((line = br.readLine()) != null) {
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

                String Cname = keylist.get(0).replace("'", "");
                System.out.println("name"+Cname);
                Rname.append(" " + Cname);

                String ConNum = keylist.get(3);
                Rnumber.append(" " + ConNum);


            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

    }

        class showRequests extends AsyncTask<String,String,String> {

            StringBuffer str = new StringBuffer();
            SimpleAdapter adapter;
            ArrayList<String> keyList = new ArrayList<String>();
            String[] keyArray;

            ArrayList<String> keylist=new ArrayList<String>();



            @Override
            protected String doInBackground(String... strings) {

                try {
                    URL url = new URL("http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=showRequest&takerID=" + tid);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        str.append(line);
                    }

                    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                    JSONArray jsonArray = new JSONArray(str.toString());

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();

                        Iterator it = obj.keys();
                        System.out.println("Keys are " + it);
                        while (it.hasNext())
                        {
                            String key = it.next().toString();
                            keylist.add(obj.getString(key));
                        }
                    }

                    date = keylist.get(1);
                    System.out.println("DATE"+date);
                    Rdate.append(" " + date);

                    String place = keylist.get(2).replace("'","");
                    System.out.println("place "+place);
                    Rplace.append(" " + place);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                return null;
            }

        }

        class acceptRequest extends AsyncTask<String,Integer,String>
        {
            @Override
            protected String doInBackground(String... strings)
            {
                try {
                    String s1 = "http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=accept&Did="+uid+"&Tid="+tid+"&Ddate="+date;


                    System.out.println("URL S1 is " + s1);
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

                if(Integer.parseInt(str.toString())==1)
                {
                    Toast.makeText(getApplicationContext(),"Request Accepted!",Toast.LENGTH_LONG).show();

                }

            }
        }

        class cancelRequest extends AsyncTask<String,Integer,String>
        {
            @Override
            protected String doInBackground(String... strings)
            {
                try {
                    String s1 = "http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=cancel&Did="+uid+"&Tid="+tid;


                    System.out.println("URL S1 is " + s1);
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

                if(Integer.parseInt(str.toString())==1)
                {
                    Toast.makeText(getApplicationContext(),"Request cancel!",Toast.LENGTH_LONG).show();

                }

            }

        }


}
