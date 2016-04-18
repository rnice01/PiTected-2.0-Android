package com.example.rnice01.pitected2.http;
import android.widget.Toast;

import com.example.rnice01.pitected2.ContextClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by rnice01 on 3/16/2016.
 */
public class HttpManager {

    public static String getData(String uri) {
        ContextClass context = new ContextClass();
        BufferedReader rd = null;

        try {
            //Make a connection with the passed in URI to the method
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //Create a new stringBuilder variable
            StringBuilder sb = new StringBuilder();
            //Read in the text from the given URI
            rd = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            //While there are items to read in
            //from the buffered reader add each line to the
            //stringbuilder
            while ((line = rd.readLine()) != null) {
                sb.append(line + "\n");
            }

            //Return the built string
            return sb.toString();
        }

        catch (Exception e) {
            //Network connectivity is checked before HttpURLConnection is used so problem may be with Pi or IP address
            Toast.makeText(context.getApplicationContext(), "Could not connect to your PI, check your system and confirm IP address.",Toast.LENGTH_LONG).show();

        } finally {
            //close the input stream connection
            if (rd != null) {
                try {
                    rd.close();
                } catch (Exception e) {

                }
            }
        }


        return uri;
    }

    public static String postRequest(String uri){
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            if(response == 200){
                return "true";
            }
            else{
                return "false";
            }

        }catch(IOException ex){
            ex.printStackTrace();
            return "false";
        }
    }
}
