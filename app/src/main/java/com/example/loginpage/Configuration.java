package com.example.loginpage;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Configuration extends AppCompatActivity
{
    //static String ip="10.0.2.2";
    //static String ip="192.168.43.227";
    static String ip="192.168.42.159";

    static HttpURLConnection con=null;
    static URL url=null;
    static BufferedReader br=null;
    static String line=null;
    static StringBuffer str=new StringBuffer();
}
