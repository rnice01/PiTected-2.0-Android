package com.example.rnice01.pitected2;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rnice01.pitected2.http.HttpManager;
import com.example.rnice01.pitected2.http.JsonParser;

import java.util.ArrayList;

public class ConfirmPopup extends AppCompatActivity {
    ProgressBar progressBar;
    Button cancelBtn;
    EditText pinInput;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_popup);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        pinInput = (EditText)findViewById(R.id.pin);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


        /**Using display metrics to get the current sensor
         * window height and width */
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        /**Set the layout to fit at 60% of the window height and  80% width */
        getWindow().setLayout((int) (width * .8), (int) (height * .6));
    }

    public void confirmPin(View view){
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE);
        String enteredPin = pinInput.getText().toString();
        String userID = userPrefs.getString("id", null);
        String status = "";

            final String ipAddress = userPrefs.getString("ipAddress", null);
        if(Main_Menu.armDisarmBtn.getText().equals("Arm")) {
            status= "1";
        }
        else{
            status="0";
        }
            /**Check connectivity*/
            if(isOnline()){
                //Arm the system
                Log.i("RequestingData", userID + " " + enteredPin + " " + " " + status);
                String uri = "http://"+ ipAddress+"/PiTected-Web-App/php/armSystem.php?userID="+ userID+"&pin="+enteredPin+"&armStatus="+status;
                requestData(uri);

            }

    }

    /** close the pop up activity */
    public void cancelArm(View view){
        finish();
    }

    //Checks network connectivity, returns true or false
    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //Thread pool executer allows multiple tasks in parallel
    private void requestData(String uri) {
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
        protected void onPostExecute(String content) {
            try {
                progressBar.setVisibility(View.GONE);
                JsonParser parse = new JsonParser();
                Boolean result = parse.getArmingResult(content);
                if(result){
                    if(Main_Menu.armDisarmBtn.getText().equals("Arm")){
                        Main_Menu.armDisarmBtn.setText("Disarm");
                        Toast.makeText(getApplicationContext(),"System armed",Toast.LENGTH_SHORT).show();
                       kill_activity();
                    }
                    else{
                        Main_Menu.armDisarmBtn.setText("Arm");
                        Toast.makeText(getApplicationContext(),"System disarmed",Toast.LENGTH_SHORT).show();
                        kill_activity();
                    }



                }else{
                    Toast.makeText(getApplicationContext(),"System could not be armed",Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        private void kill_activity()
        {
            finish();
        }

    }



}
