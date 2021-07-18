package com.example.loginpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle("Contact Us");


        link=findViewById(R.id.link);

        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        String text="<a>gaichorshriti@gmail.com</a>";
        link.setText(Html.fromHtml(text));

    }
}
