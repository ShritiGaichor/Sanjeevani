package com.example.loginpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Help extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Facts and Guidelines");
        text=findViewById(R.id.textView6);
        text.setText("Why give blood\n" +   "Giving blood saves lives. The blood you give is a lifeline in an emergency and for people who need long-term treatments.\n"+
                "Why do we need you to give blood?\n" +
                "We need new blood donors from all backgrounds to ensure there is the right blood available for patients who need it.\n\n" +
                "\n" +
                "We need:\n" +
                "Nearly 400 new donors a day to meet demand\n" +
                "Around 135,000 new donors a year to replace those who can no longer donate\n" +
                "40,000 more black donors to meet growing demand for better-matched blood\n" +
                "30,000 new donors with priority blood types such as O negative every year\n" +
                "More young people to start giving blood so we can make sure we have enough blood in the future\n\n"+
                "Who can give blood\n" +
                "Most people can give blood. You can give blood if you:\n" +
                "\n" +
                "are fit and healthy\n" +
                "weigh between 7 stone 12 lbs and 25 stone, or 50kg and 160kg\n" +
                "are aged between 17 and 66 (or 70 if you have given blood before)\n" +
                "are over 70 and have given blood in the last two years\n\n"+"Male and female donors\n" +
                "Men are more likely to donate more often than women because:\n" +
                "\n" +
                "men’s additional body weight means they have suitable iron levels\n" +
                "they are less likely than women to carry certain immune cells meaning their plasma is more widely usable for transfusions\n" +
                "their platelet count is typically higher meaning they are more likely to be accepted as platelet donors\n\n"+
                "");
    }
}
