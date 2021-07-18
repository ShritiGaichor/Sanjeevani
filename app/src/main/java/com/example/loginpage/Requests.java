package com.example.loginpage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Map;

public class Requests extends AppCompatActivity {

    ListView request;
    HttpURLConnection con = null;
    URL url = null;
    BufferedReader br = null;
    String line = null, output;
    StringBuffer str = new StringBuffer();
    String out2,id1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        setTitle("Requests");

        Bundle rs = getIntent().getExtras();
        out2 = rs.getString("out1");
        System.out.println("ID is " + out2);
        request = findViewById(R.id.request);
        new showRequests().execute();


        request.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long iid)
            {

                String selectedItem=parent.getItemAtPosition(position).toString();
                String tempString=selectedItem.replace("{","");
                String finalString=tempString.replace("}","");
                String finalString1=finalString.replace(" ","");

                System.out.println("Final  STring "+finalString1);

                Map<String, String> myMap = new HashMap<String, String>();
                String s =finalString1;
                String[] pairs = s.split(",");
                System.out.println("pairs are "+pairs);
                for (int i=0;i<pairs.length;i++)
                {
                    String pair = pairs[i];
                    String[] keyValue = pair.split("=");
                    System.out.println("keyValue "+keyValue);
                    myMap.put(keyValue[0], String.valueOf(keyValue[1]));
                }
                System.out.println("map is "+myMap);
                id1=myMap.get("donor_id");
                System.out.println("idddddd is "+id1);
                Intent in=new Intent(Requests.this,RequestInfo.class);
                in.putExtra("id",id1);
                in.putExtra("out2",out2);
                System.out.println("Out 2 is "+out2+"taker "+id1);
                startActivity(in);
            }
        });


    }

    class showRequests extends AsyncTask<String,String,String>
    {

        StringBuffer str=new StringBuffer();
        SimpleAdapter adapter;
        ArrayList<String> keyList=new ArrayList<String>();
        String[] keyArray;


        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("http://" + Configuration.ip + ":8081/myJSONclient/jsonPage.jsp?method=showRequest&takerID=" + out2);

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
                        if(key.equalsIgnoreCase("place"))
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

                adapter = new SimpleAdapter(Requests.this, mylist, R.layout.row1, keyArray, new int[] {R.id.id,R.id.date,R.id.place});
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            request.setAdapter(adapter);
        }


    }

}
