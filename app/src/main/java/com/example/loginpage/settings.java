package com.example.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class settings extends AppCompatActivity {


    Button ChangePassword,Logout;
    String out2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        ChangePassword=findViewById(R.id.ChangePassword);
        Logout=findViewById(R.id.Logout);

        Bundle rs=getIntent().getExtras();
        out2=rs.getString("out1");
        System.out.println("ID is "+out2);


        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent("com.example.loginpage.changePassword");
                in.putExtra("out2",out2);
                startActivity(in);
            }
        });

    }
}
