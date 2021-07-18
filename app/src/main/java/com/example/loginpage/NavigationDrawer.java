package com.example.loginpage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    Button Details,SearchDonor;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setTitle("Sanjeevani");
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
       fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Bundle rs=getIntent().getExtras();
                String out1=rs.getString("out");
                Intent in=new Intent(NavigationDrawer.this,Requests.class);
                in.putExtra("out1",out1);
                startActivity(in);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Details=findViewById(R.id.Details);
        SearchDonor=findViewById(R.id.searchDonor);

        SearchDonor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search=new Intent("com.example.loginpage.SearchDonor");
                startActivity(search);

            }
        });


        Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detail=new Intent(NavigationDrawer.this,Users.class);
                startActivity(detail);
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
            {
                super.onBackPressed();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent in=new Intent(NavigationDrawer.this,settings.class);
            Bundle rs=getIntent().getExtras();
            String out1=rs.getString("out");
            in.putExtra("out1",out1);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Account)
        {
            Intent in=new Intent(NavigationDrawer.this,trialAccountActivity.class);
            Bundle rs=getIntent().getExtras();
            String out1=rs.getString("out");
            in.putExtra("out1",out1);
            startActivity(in);

        }
        else if (id == R.id.NewDonation) {
            Bundle rs=getIntent().getExtras();
            String out1=rs.getString("out");
            System.out.println("ID is "+out1);


            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            Intent in=new Intent(NavigationDrawer.this,TakerActivity.class);
            in.putExtra("out1",out1);
            startActivity(in);


        }

        else if (id == R.id.Activities) {

            Intent in=new Intent(NavigationDrawer.this,Donations.class);
            Bundle rs=getIntent().getExtras();
            String out1=rs.getString("out");
            System.out.println("ID is "+out1);
            in.putExtra("out1",out1);
            startActivity(in);


        } else if (id == R.id.Help) {
            Intent in=new Intent(NavigationDrawer.this,Help.class);
            startActivity(in);


        }  else if (id == R.id.Invite) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Blood Donation");
                String shareMessage= "\nLet me recommend you this application for Blood Donation\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose one"));
            } catch(Exception e) {
                //e.toString();
            }



        }
        else if (id == R.id.Feedback)
        {

            Intent in=new Intent(NavigationDrawer.this,Feedback.class);
            Bundle rs=getIntent().getExtras();
            String out1=rs.getString("out");
            System.out.println("ID is "+out1);
            in.putExtra("out1",out1);
            startActivity(in);
        }
        else if (id == R.id.contactUs)
        {
            Intent in=new Intent(NavigationDrawer.this,ContactUs.class);
            startActivity(in);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
