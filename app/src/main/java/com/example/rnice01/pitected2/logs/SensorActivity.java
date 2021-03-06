package com.example.rnice01.pitected2.logs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.rnice01.pitected2.R;
import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;
import com.example.rnice01.pitected2.objects.Sensors;
import com.example.rnice01.pitected2.services.UpdateSensorService;

public class SensorActivity extends AppCompatActivity {
    ListView sensorList;
    List<Sensors> sensorsToList;
    SensorAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
       final String ipAddress = userPrefs.getString("ipAddress", null);
       // Intent checkSensors = new Intent(this, UpdateSensorService.class);
        //startService(checkSensors);




        if(isOnline()){

            requestData("http://"+ipAddress + "/PiTected-Web-App/php/getLogs.php?log_type=current");
        }
        else{
            Toast.makeText(this,"Network isn't available", Toast.LENGTH_SHORT).show();
        }

        sensorList = (ListView) findViewById(R.id.sensorList);

    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Intent checkSensors = new Intent(this, UpdateSensorService.class);
//        stopService(checkSensors);
//    }
//
//    @Override
//    protected  void onPause(){
//        super.onPause();
//        Intent checkSensors = new Intent(this, UpdateSensorService.class);
//        stopService(checkSensors);
//    }
//
//    @Override
//    protected void onResume(){
//        super.onResume();
//        Intent checkSensors = new Intent(this, UpdateSensorService.class);
//        startService(checkSensors);
//    }

    //Adds items to listView
    public void updateDisplay() {
        if(sensorsToList != null){
            ArrayList<Sensors> sensorListView = new ArrayList<>();
            for(Sensors sensors: sensorsToList){
                sensorListView.add(sensors);

            }

            sensorAdapter = new SensorAdapter(this, sensorListView);
            sensorList.setAdapter(sensorAdapter);
        }
        else{
            Toast.makeText(this, "Unable to connect to sensors", Toast.LENGTH_SHORT).show();
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
    private class MyTask extends AsyncTask<String, String, String> {
        //This method has access to the main thread
        //and runs before doInBackground
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String content = HttpManager.getData(params[0]);
            Log.i("Getting current sensors",content);
            return content;
        }

        //This method receives a result, depending
        //on the RunTasks<> data parameter type
        @Override
        protected void onPostExecute(String s) {
            try {
                JsonParser parser = new JsonParser();
                sensorsToList = parser.parseSensorFeed(s);
                updateDisplay();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}
