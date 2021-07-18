package com.example.loginpage;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

public class Donations extends AppCompatActivity
{
    String out2;
    ListView donations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
        setTitle("Your Activities");

        donations=findViewById(R.id.donations);
        Bundle rs=getIntent().getExtras();
        out2=rs.getString("out1");
        System.out.println("ID is "+out2);

        new showDonations().execute();
    }

    class showDonations extends AsyncTask<String,Integer,String>
    {

        StringBuffer str=new StringBuffer();
        SimpleAdapter adapter;
        ArrayList<String> keyList=new ArrayList<String>();
        String[] keyArray;


        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=showDonations&Did="+out2);
                HttpURLConnection con=(HttpURLConnection)url.openConnection();
                BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line = "";
                while ((line = rd.readLine()) != null)
                {
                    str.append(line);
                }

                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                JSONArray jsonArray=new JSONArray(str.toString());

                for(int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject obj=jsonArray.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    Iterator it=obj.keys();
                    while(it.hasNext())
                    {
                        String key=it.next().toString();
                        System.out.println("Key: "+key);
                        keyList.add(key);
                        if(key.equalsIgnoreCase("place") )
                        {
                            map.put(key,obj.getString(key).replace("'",""));
                        }
                        else
                        {
                            map.put(key,obj.getString(key));
                        }
                    }
                    System.out.println("Map is "+map);
                    mylist.add(map);

                    keyArray=new String[keyList.size()];

                    for(int j=0; j<keyArray.length; j++)
                    {
                        if(!Arrays.asList(keyArray).contains(keyList.get(j)))
                        {
                            keyArray[j]=keyList.get(j);
                        }
                    }
                }

                adapter = new SimpleAdapter(Donations.this, mylist, R.layout.row2, keyArray, new int[] {R.id.date,R.id.place});
 }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            donations.setAdapter(adapter);
        }


    }
}
