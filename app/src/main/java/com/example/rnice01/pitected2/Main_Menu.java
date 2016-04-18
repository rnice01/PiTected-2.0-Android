package com.example.rnice01.pitected2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;
import com.example.rnice01.pitected2.logs.DeviceActivity;
import com.example.rnice01.pitected2.logs.EventLogsActivity;
import com.example.rnice01.pitected2.logs.SystemLogActivity;
import com.example.rnice01.pitected2.services.CheckSystem;

import java.util.ArrayList;

import static com.example.rnice01.pitected2.http.HttpManager.getData;

public class Main_Menu extends AppCompatActivity implements View.OnClickListener {
    ImageButton deviceLogs, systemStatus, logout, systemLogs;
    public static Button armDisarmBtn;
    private static final String TAG = "Main Menu";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        deviceLogs =  (ImageButton)findViewById(R.id.deviceLogs);
        systemStatus = (ImageButton)findViewById(R.id.systemStatus);
        systemLogs = (ImageButton)findViewById(R.id.systemLogs);
        logout = (ImageButton)findViewById(R.id.logout);
        armDisarmBtn = (Button)findViewById(R.id.armDisarmBtn);

        Intent checkSystem = new Intent(this, CheckSystem.class);
        startService(checkSystem);


        deviceLogs.setOnClickListener(this);
        systemStatus.setOnClickListener(this);
        systemLogs.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent checkSystem = new Intent(this, CheckSystem.class);
        stopService(checkSystem);
    }





    public void armService(View view){
        if(isOnline()){
            SharedPreferences sharedPref = getSharedPreferences("userPrefs",MODE_PRIVATE);
            String ipAddress = sharedPref.getString("ipAddress", null);
            requestData(ipAddress+"/php/checkSensors.php");
        }
    }


    private void requestData(String uri) {
        CheckSensorStatus check = new CheckSensorStatus();
        check.execute(uri);

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deviceLogs:
                Intent deviceLogIntent = new Intent(this, EventLogsActivity.class);
                startActivity(deviceLogIntent);
                break;

            case R.id.systemLogs:
                Intent systemLogIntent = new Intent(this, SystemLogActivity.class);
                startActivity(systemLogIntent);
                break;
            case R.id.systemStatus:
                Intent statusIntent = new Intent(this, DeviceActivity.class);
                startActivity(statusIntent);
                break;
            case R.id.logout:
                killActivity();
                break;



        }

    }

    private void killActivity() {
            finish();
        Intent loginActivity = new Intent(this, MainActivity.class);
        startActivity(loginActivity);
    }

    //Class to instantiate the Async Task class for running
    //Http Requests in the background
    private class CheckSensorStatus extends AsyncTask<String, String, String> {
        //This method has access to the main thread
        //and runs before doInBackground
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, params[0]);
            String content = getData(params[0]);
            Log.i(TAG, content);
            return content;
        }

        //This method receives a result, depending
        //on the RunTasks<> data parameter type
        @Override
        protected void onPostExecute(String content) {
            try {
                JsonParser parse = new JsonParser();
                ArrayList<String> result = parse.getSensorResult(content);
                if(result.isEmpty()){
                    startActivity(new Intent(Main_Menu.this,ConfirmPopup.class));

                }else{
                    for(int i = 0; i < result.size(); i++) {
                        Toast.makeText(getApplicationContext(), "Check " + result.get(i), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
}
