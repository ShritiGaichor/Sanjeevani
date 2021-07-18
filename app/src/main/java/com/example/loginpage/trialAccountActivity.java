package com.example.loginpage;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

public class trialAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView LoginID;
    EditText Name,BirthDate,ContactNo,Email;
    Spinner City,BloodGroup;
    CheckBox Donor;
    Button Save;
    RadioGroup rbGroup;
    RadioButton male,female;
    ProgressBar PB;



    HttpURLConnection con=null;
    URL url=null;
    BufferedReader br=null;
    String line=null,output;
    StringBuffer str=new StringBuffer();

    Integer donor;
    String gender="";

    String out2;

    String cities[]={"Select your city","Devgad","Dodamarg","Kankavli","Kudal","Malvan","Sawantwadi","Vaibhavwadi","Vengurla","Other"};
    String bloodGroups[]={"Select your Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-","Bombay Blood Group"};

    ArrayAdapter adapter,adapter1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_account);
        setTitle("Profile");


        LoginID=findViewById(R.id.LoginID);
        Name= findViewById(R.id.Name);
        BirthDate= findViewById(R.id.BirthDate);
        rbGroup=findViewById(R.id.rbGroup);
        male=findViewById(R.id.male);
        female=findViewById(R.id.female);
        ContactNo=findViewById(R.id.ContactNO);
        Email=findViewById(R.id.Email);
        City=findViewById(R.id.City);
        BloodGroup=findViewById(R.id.BloodGroup);
        Donor=findViewById(R.id.Donor);
        PB=findViewById(R.id.PB);
        Save=findViewById(R.id.Save);

        PB.setMin(0);
        PB.setMax(90);



        Bundle rs=getIntent().getExtras();
        out2=rs.getString("out1");
        System.out.println("ID is "+out2);

        LoginID.setText("            Login ID:"+out2);


        BirthDate.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if(hasFocus)
                {
                    showDatePickerDialog();
                }
            }
        });

        adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,cities);
        City.setAdapter(adapter);
        City.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int index=parent.getSelectedItemPosition();
                //Toast.makeText(getApplicationContext(), "You selected "+cities[index]+" city.", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,bloodGroups);

        BloodGroup.setAdapter(adapter1);
        BloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int index=parent.getSelectedItemPosition();
               // Toast.makeText(getApplicationContext(),"You selected "+bloodGroups[index]+" blood group,",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        new ExecuteTask().execute();

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new saveData().execute();
            }
        });

    }

    public void showDatePickerDialog()
    {
        DatePickerDialog date=new DatePickerDialog(this,this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        date.show();
    }

    @Override
    public void onDateSet(DatePicker view, int i, int i1, int i2)
    {
        int i3=i1+1;
        String date1 = i2 + "/" + i3 + "/" + i;
        BirthDate.setText(date1);
    }


    class ExecuteTask extends AsyncTask<String,Integer,String>
    {
        StringBuffer str=new StringBuffer();
        ArrayList<String> keylist=new ArrayList<String>();
        String[] keyArray;


        @Override
        protected String doInBackground(String... strings) {
            try {
               URL url = new URL("http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=getAccountInfo&id="+out2);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                System.out.println("Connection");
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line="";
                while ((line=br.readLine())!=null)
                {
                    str.append(line);
                }
                System.out.println(str.toString());
                ArrayList<HashMap<String,String>> mylist=new ArrayList<HashMap<String, String>>();
                JSONArray jsonArray=new JSONArray(str.toString());

                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject obj=jsonArray.getJSONObject(i);
                    HashMap<String,String> map=new HashMap<String,String>();

                    Iterator it=obj.keys();
                    System.out.println("Keys are "+it);
                    keylist.clear();
                    while (it.hasNext())
                    {
                        String key=it.next().toString();
                        keylist.add(obj.getString(key));
                    }
                }
                System.out.println("keylist2= "+keylist);




                String Cname=keylist.get(0).replace("'","");
                Name.setText(Cname);
                //System.out.println("0= "+keylist.get(0));

                //System.out.println("1= "+keylist.get(1));

                String BD=keylist.get(1);
                String Y="",M="",D="";

                StringTokenizer st=new StringTokenizer(BD,"-");
                if(st.hasMoreTokens())
                {
                    Y=st.nextToken();
                }
                if(st.hasMoreTokens())
                {
                    M=st.nextToken();
                }
                if(st.hasMoreTokens())
                {
                    D=st.nextToken();
                }

                BirthDate.setText(D+"/"+M+"/"+Y);


                String gender=keylist.get(2);
                System.out.println("Gender: "+gender);
                if(gender.equalsIgnoreCase("Male"))
                {
                    male.setChecked(true);
                }
                else
                {
                    female.setChecked(true);
                }


               // System.out.println("2= "+keylist.get(2));
                String ConNum=keylist.get(3);
                ContactNo.setText(ConNum);
                //System.out.println("3= "+keylist.get(3));
                String Cemail=keylist.get(4);
                Email.setText(Cemail);
                System.out.println("4= "+keylist.get(4));
                System.out.println("5= "+keylist.get(5));
                System.out.println("Blood Group is "+keylist.get(6));

                String cCity=keylist.get(5);
                System.out.println("cCity is "+cCity);

                int p=adapter.getPosition(cCity);
                System.out.println("p is "+p+"pth item is "+adapter.getItem(p));
                System.out.println("City= "+keylist.get(5));
                City.setSelection(p);

                System.out.println("BG is "+keylist.get(6).replace("'",""));
                String cBloodGroup=keylist.get(6).replace("'","");
                System.out.println("bloodgroup is "+cBloodGroup);

                int q=adapter1.getPosition(cBloodGroup);
                System.out.println("q is "+q+"qth item is "+adapter1.getItem(q));

                System.out.println("q is "+q);
                BloodGroup.setSelection(q);
                System.out.println("Blood Group is "+keylist.get(6));

                // System.out.println("6= "+keylist.get(6));
                int k=Integer.parseInt(keylist.get(7));
               // System.out.println("7= "+keylist.get(7));

                if(k==1)
                {
                    Donor.setChecked(true);
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
        }

    }


    class saveData extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                //id
                final String loginID=out2.toString();
                //name
                final String name=Name.getText().toString();
                //dob
                final String birthDate=BirthDate.getText().toString();
                //contact
                final String contactNo=ContactNo.getText().toString();
                //email
                final String email=Email.getText().toString();
                //city
                final String city=City.getSelectedItem().toString();
                //blood_group
                final String bloodGroup=BloodGroup.getSelectedItem().toString();

                //gender
                if(male.isChecked())
                {
                    gender+=male.getText();
                }
                else
                {
                    gender+=female.getText();
                }

                //donor
                if(Donor.isChecked())
                {
                    donor=1;
                }
                else
                {
                    donor=0;
                }

                String bg=bloodGroup.replace("+","%2B");

                String s1="http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=updateUser&uid="+out2+"&uname=\'"+name+"\'&udob="+birthDate+"&ugender="+gender+"&ucontact="+contactNo+"&uemail="+email+"&ucity=\'"+city+"\'&ublood_group=\'"+bg+"\'&udonor="+donor;

                System.out.println("URL S1 is "+s1);
                url =new URL(s1);

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


            if(Integer.parseInt(str.toString())==1)
            {
                System.out.println("String "+str.toString());
                Toast.makeText(getApplicationContext(),"Your profile is updated.",Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();


            }
            if(str.equals(0))
            {
                Toast.makeText(getApplicationContext(), "Profile updation failed....Plz try again", Toast.LENGTH_SHORT).show();
            }


        }
    }


}
