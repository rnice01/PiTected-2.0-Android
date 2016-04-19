package com.example.rnice01.pitected2.logs;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rnice01.pitected2.R;
import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;
import com.example.rnice01.pitected2.objects.SystemLog;

import java.util.ArrayList;
import java.util.List;

public class SystemLogActivity extends AppCompatActivity {
    ListView eventList;
    List<SystemLog> eventsToList;
    SystemLogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_log);
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        final String ipAddress = userPrefs.getString("ipAddress", null);

        if(isOnline()){
            requestData("http://"+ ipAddress+"/PiTected-Web-App/php/getLogs.php?log_type=system");
        }
        else{
            Toast.makeText(this, "Network isn't available", Toast.LENGTH_SHORT).show();
        }

        eventList = (ListView) findViewById(R.id.listView);
    }

    //Adds items to listView
    private void updateDisplay() {
        if(eventsToList != null){
            ArrayList<SystemLog> deviceListView = new ArrayList<>();
            for(SystemLog logs: eventsToList){
                deviceListView.add(logs);

            }

            adapter = new SystemLogAdapter(this, deviceListView);
            eventList.setAdapter(adapter);
        }
        else{
            Toast.makeText(this, "Unable to connect to devices", Toast.LENGTH_SHORT).show();
        }
    }

    //Checks network connectivity
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else{
            return false;
        }
    }


    //Thread pool executer allows multiple tasks in parallel
    private void requestData( String uri) {
        MyTask task = new MyTask();
        task.execute(uri);
    }

    //Class to instantiate the Async Task class for running
    //Http Requests in the background
    private class MyTask extends AsyncTask<String, String, String>{
        //This method has access to the main thread
        //and runs before doInBackground
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            return content;
        }

        //This method receives a result, depending
        //on the RunTasks<> data parameter type
        @Override
        protected void onPostExecute(String s) {
            try {
                JsonParser parser = new JsonParser();
                eventsToList = parser.parseSystemLogs(s);
                updateDisplay();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
