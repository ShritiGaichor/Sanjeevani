package com.example.loginpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.Map;

public class SearchDonor extends AppCompatActivity
{
    EditText Search;
    Spinner Filter;
    ListView list;

    String types[]={"Filter","Blood group","City","Gender"};

    String cities[]={"Devgad","Dodamarg","kankavli","Kudal","Malvan","Sawantwadi","Vaibhavwadi","Vengurla","Other"};
    String bloodGroups[]={"A+","A-","B+","B-","AB+","AB-","O+","O-","Bombay Blood Group"};
    String Genders[]={"Male","Female"};


    int j=0,pos;
    int bgL=bloodGroups.length;
    int ctL=cities.length;
    int gdL=Genders.length;
    String BG,CT,GDR,bg;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);
        setTitle("Search Donor");


        Search=findViewById(R.id.Search);
        Filter=findViewById(R.id.Filter);
        list=findViewById(R.id.list);

        new donors().execute();

        final AlertDialog.Builder builder=new AlertDialog.Builder(this);


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("Choose your required Blood Group");

                builder.setItems(bloodGroups, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        for(j=0;j<bgL;j++)
                        {
                            if(which==j)
                            {
                                bg=bloodGroups[j];
                                BG=bg.replace("+","%2B");
                                System.out.println("bg is "+BG);
                                new search().execute();
                            }
                        }
                    }
                });
                builder.show();
            }
        });


        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,types);

        Filter.setAdapter(adapter);
        Filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int index=parent.getSelectedItemPosition();
                //Toast.makeText(getApplicationContext(), "You selected "+types[index], Toast.LENGTH_SHORT).show();

                if(index==0)
                {
                    BG="";
                    CT="";
                    GDR="";
                }
                if(index==1)
                {
                    System.out.println("Index "+index);
                    builder.setTitle("Choose Blood Group");
                    builder.setItems(bloodGroups, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            for(j=0;j<bgL;j++)
                            {
                                if(which==j)
                                {
                                    BG=bloodGroups[j].replace("+","%2B");;
                                    new fetchData().execute();
                                }
                            }
                        }
                    });
                    builder.show();
                    System.out.println("Blood group "+BG);
                    System.out.println("Index "+index);


                }


                if(index==2)
                {
                    builder.setTitle("Choose City");
                    builder.setItems(cities, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            for(j=0;j<ctL;j++)
                            {
                                if(which==j)
                                {
                                    CT=cities[j];
                                    new fetchData().execute();
                                }
                            }
                        }
                    });
                    builder.show();
                    System.out.println("City "+CT);
                    System.out.println("Index "+index);
                }

                if(index==3)
                {
                    builder.setTitle("Choose Gender");
                    builder.setItems(Genders, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            for(j=0;j<gdL;j++)
                            {
                                if(which==j)
                                {
                                    GDR=Genders[j];
                                    new fetchData().execute();
                                }
                            }
                        }
                    });
                    builder.show();
                    System.out.println("Gender is "+GDR);
                    System.out.println("Index "+index);
                }
               // new fetchData().execute();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {


            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
                String id1=myMap.get("id");
                System.out.println("idddddd is "+id1);
                Intent in=new Intent(SearchDonor.this,userInfo.class);
                in.putExtra("id",id1);
                startActivity(in);
            }
        });

    }


    class donors extends AsyncTask<String,Integer,String>
    {
        StringBuffer str=new StringBuffer();
        SimpleAdapter adapter;
        ArrayList<String> keyList=new ArrayList<String>();
        String[] keyArray;

        protected String doInBackground(String... params)
        {
            try
            {



                URL url = new URL("http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=showAll");

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
                        keyList.add(key);
                        if(key.equalsIgnoreCase("name"))
                        {
                            map.put(key,obj.getString(key).replace("'",""));
                        }
                        else
                        {
                            map.put(key,obj.getString(key));
                        }                    }

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
                adapter = new SimpleAdapter(SearchDonor.this, mylist, R.layout.row, keyArray, new int[] {R.id.id,R.id.name,R.id.BD});
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
            list.setAdapter(adapter);
        }

    }


    class fetchData extends AsyncTask<String,Integer,String>
    {StringBuffer str=new StringBuffer();
        SimpleAdapter adapter;
        ArrayList<String> keyList=new ArrayList<String>();
        String[] keyArray;

        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL("http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=fetchData&bloodGroup="+BG+"&gender="+GDR+"&city="+CT);
                System.out.println("URL ="+url);
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
                        keyList.add(key);
                        if(key.equalsIgnoreCase("name"))
                        {
                            map.put(key,obj.getString(key).replace("'",""));
                        }
                        else
                        {
                            map.put(key,obj.getString(key));
                        }                    }

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
                adapter = new SimpleAdapter(SearchDonor.this, mylist, R.layout.row, keyArray, new int[] {R.id.id,R.id.name,R.id.BD});
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
            list.setAdapter(adapter);
        }

    }


    class search extends AsyncTask<String,Integer,String>
    {StringBuffer str=new StringBuffer();
        SimpleAdapter adapter;
        ArrayList<String> keyList=new ArrayList<String>();
        String[] keyArray;

        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL("http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=Search&bg="+BG);
                System.out.println("URL ="+url);
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
                        keyList.add(key);
                        if(key.equalsIgnoreCase("name"))
                        {
                            map.put(key,obj.getString(key).replace("'",""));
                        }
                        else
                        {
                            map.put(key,obj.getString(key));
                        }
                    }
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
                adapter = new SimpleAdapter(SearchDonor.this, mylist, R.layout.row, keyArray, new int[] {R.id.id,R.id.name,R.id.BD});
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
            list.setAdapter(adapter);
        }

    }

}
