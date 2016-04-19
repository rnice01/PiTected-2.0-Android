package com.example.rnice01.pitected2.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rnice01.pitected2.R;
import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;
import com.example.rnice01.pitected2.logs.DeviceActivity;
import com.example.rnice01.pitected2.objects.Devices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rnice01 on 4/17/2016.
 */
public class UpdateSensorService extends Service {
    List<Devices> sensorList;
    ListView sensorListView;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        //return super.onStartCommand(intent, flags, startId);
        start();


        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        stop();
    }

    private boolean started = false;
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {


            if(started) {
                start();
                Toast.makeText(getApplicationContext(),"Starting the service", Toast.LENGTH_SHORT).show();
                SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                final String ipAddress = userPrefs.getString("ipAddress", null);
                CheckSystemTask task = new CheckSystemTask();
                task.execute("http://"+ ipAddress+"/PiTected-Web-App/php/getLogs.php?log_type=current");
            }
        }
    };

    public void stop() {
        Toast.makeText(getApplicationContext(),"Stopping the service", Toast.LENGTH_SHORT).show();
        started = false;
        handler.removeCallbacks(runnable);
    }

    public void start() {
        started = true;
        handler.postDelayed(runnable, 10000);
    }

    //Class to instantiate the Async Task class for running
    //Http Requests in the background
    private class CheckSystemTask extends AsyncTask<String, String, String> {
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
                sensorList = parser.parseDeviceFeed(s);

                DeviceActivity activity = new DeviceActivity();
                activity.updateDisplay(sensorList);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

}
