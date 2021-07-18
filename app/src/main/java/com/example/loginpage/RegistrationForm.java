package com.example.loginpage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;


public class RegistrationForm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
    EditText Name,BirthDate,ContactNo,Email,Password1,Password2;
    RadioGroup rgGender;
    RadioButton rbMale,rbFemale;
    Spinner City,BloodGroup;
    CheckBox Donor;
    Button Submit;
    Integer donor;
    String gender="";

    HttpURLConnection con=null;
    URL url=null;
    BufferedReader br=null;
    String line=null,output;
    StringBuffer str=new StringBuffer();

    String cities[]={"Select your city","Devgad","Dodamarg","Kankavli","Kudal","Malvan","Sawantwadi","Vaibhavwadi","Vengurla","Other"};
    String bloodGroups[]={"Select your Blood Group","A+","A-","B+","B-","AB+","AB-","O+","O-","Bombay Blood Group"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setTitle("Registration Form");
        setContentView(R.layout.activity_registration_form);
        super.setTitleColor(12);



        Name= findViewById(R.id.Name);
        BirthDate= findViewById(R.id.BirthDate);
        rgGender=findViewById(R.id.rgGender);
        rbMale=findViewById(R.id.rbMale);
        rbFemale=findViewById(R.id.rbFemale);
        ContactNo=findViewById(R.id.ContactNo);
        Email=findViewById(R.id.Email);
        City=findViewById(R.id.City);
        BloodGroup=findViewById(R.id.BloodGroup);
        Password1=findViewById(R.id.Password1);
        Password2=findViewById(R.id.Password2);
        Donor=findViewById(R.id.Donor);
        Submit=findViewById(R.id.Submit);


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


        final ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,cities)
        {
            @Override
            public boolean isEnabled(int position)
            {
                if(position==0)
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };
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




        final ArrayAdapter adapter1=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,bloodGroups);
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


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addUser().execute();

            }
        });

    }

    public void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


    }

     @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        int i3=i1+1;
        String date1 = i2 + "/" + i3 + "/" + i;
        BirthDate.setText(date1);

    }



    class addUser extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
                        try
                        {
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
                            //password
                            String password1=Password1.getText().toString();
                            System.out.println("password1 "+password1);
                            String password2=Password2.getText().toString();
                            System.out.println("password2 "+password2);

                            String password="";
                            if(password1.equals(password2))
                            {
                                password+=password1;
                                System.out.println("password "+password);

                            }


                            //gender
                            if(rbMale.isChecked())
                            {
                                gender+=rbMale.getText();
                            }
                            else
                            {
                                gender+=rbFemale.getText();
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

                         String s1="http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=addUser&uname=\'"+name+"\'&udob="+birthDate+"&ugender="+gender+"&ucontact="+contactNo+"&uemail="+email+"&ucity="+city+"&ublood_group="+bg+"&upassword=\'"+password+"\'&udonor="+donor;


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

            if(Integer.parseInt(str.toString())>0)
            {
                Toast.makeText(getApplicationContext(),"Registration successful!",Toast.LENGTH_LONG).show();
                System.out.println("String "+str.toString());
                output=str.toString();
                Intent in = new Intent(RegistrationForm.this,NavigationDrawer.class);
                in.putExtra("out",output);
                startActivity(in);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please complete your details!",Toast.LENGTH_LONG).show();
                Intent in2 = new Intent(RegistrationForm.this,RegistrationForm.class);
                startActivity(in2);
            }
        }
    }
}
