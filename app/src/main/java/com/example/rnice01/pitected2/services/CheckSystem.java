package com.example.rnice01.pitected2.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.rnice01.pitected2.Main_Menu;
import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class CheckSystem extends Service {
    int systemStatus;
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
                Toast.makeText(getApplicationContext(), "Checking system status", Toast.LENGTH_SHORT).show();
                SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
                final String ipAddress = userPrefs.getString("ipAddress", null);
                CheckSystemTask task = new CheckSystemTask();
                task.execute(ipAddress+"/php/getSystemStatus.php");
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
                systemStatus = parser.getSystemStatus(s);

                 updateArmStatus(systemStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        private void updateArmStatus(int status) {
            Log.i("From checkSystemService", "Changing status " +status);
            if(status == 0){
                Main_Menu.armDisarmBtn.setText("Arm");
            }
            else{
                Main_Menu.armDisarmBtn.setText("Disarm");
            }

        }
    }



}
