package com.example.loginpage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static com.example.loginpage.Configuration.con;

public class TakerActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    EditText donorID, Tdate, Tplace;
    Button Tsend;
    String ID,date,place,out2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taker);
        setTitle("Donor's Activity");

        donorID = findViewById(R.id.donorID);
        Tdate = findViewById(R.id.Tdate);
        Tplace = findViewById(R.id.Tplace);
        Tsend = findViewById(R.id.Tsend);

        Bundle rs=getIntent().getExtras();
        out2=rs.getString("out1");
        System.out.println("ID is "+out2);

        Tdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();

                }

            }
        });


        Tsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TaddDonation().execute();
            }
        });

    }

    public void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog=new DatePickerDialog(
                this,this, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        int i3=i1+1;
        String date1 = i2 + "/" + i3 + "/" + i;
        Tdate.setText(date1);



    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class TaddDonation extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
            ID=donorID.getText().toString();
            date=Tdate.getText().toString();
            place=Tplace.getText().toString();

                String s1="http://"+Configuration.ip+":8081/myJSONclient/jsonPage.jsp?method=addDonation&donorID="+out2+"&takerID="+ID+"&date="+date+"&place=\'"+place+"\'";
                System.out.println("URL S1 is "+s1);
                Configuration.url =new URL(s1);

                con=null;
                con = (HttpURLConnection) Configuration.url.openConnection();
                System.out.println("Connection successful");
                Configuration.br=null;
                Configuration.br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                Configuration.line="";
                System.out.println("line is"+Configuration.line);
                Configuration.str=new StringBuffer();

                while ((Configuration.line=Configuration.br.readLine())!=null)
                {
                    Configuration.str.append(Configuration.line);
                }
                System.out.println(Configuration.str.toString());
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
            if(Integer.parseInt(Configuration.str.toString())==1)
            {
                Toast.makeText(getApplicationContext(),"Request sent!",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Request sending fail.Please check your provided information!",Toast.LENGTH_LONG).show();

            }


        }
    }
}