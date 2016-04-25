/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rnice01.pitected2.http.JsonParser;

import java.util.ArrayList;

import static com.example.rnice01.pitected2.http.HttpManager.getData;

public class MainActivity extends AppCompatActivity {
    Button loginBtn, registerBtn;
    EditText usernameInput, passwordInput;
    public static CheckBox autoLogin;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**=====================================
         * Get all the resources from the layout
         ======================================*/
        loginBtn = (Button) findViewById(R.id.regBtn);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);
        registerBtn = (Button) findViewById(R.id.registerDevice);

        /**When activity is created check to see if user login data is stored in phone already
         * if it is, log user in to main menu activity automatically*/
        if(autoLogin.isChecked()) {
            SharedPreferences userPrefs = getPreferences(this.MODE_PRIVATE);
            String username = userPrefs.getString("username", null);
            String password = userPrefs.getString("password", null);
            String ipAddress = userPrefs.getString("ip", null);
            if (username != null && password != null) {
                requestData("http://"+ ipAddress+"/PiTected-Web-App/php/getUserData.php?username=" + username + "&password=" + password);
            }
        }
    }


    /**
     * ================================================================================
     * Check the credentials match what is in the database and if auto login is checked
     * ==================================================================================
     */
    public void confirmCredentials(View view) {
        SharedPreferences userPrefs = getSharedPreferences("userPrefs",MODE_PRIVATE);
        String user = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        if(autoLogin.isChecked()) {

            SharedPreferences.Editor edit = userPrefs.edit();
            edit.putString("password", password);

        }
        String ipAddress = userPrefs.getString("ipAddress", null);
        if(ipAddress != null){
            if (isOnline()) {
                Log.i(TAG, "changed detected");
                requestData("http://" + ipAddress + "/PiTected-Web-App/php/getUserData.php?username=" + user + "&password=" + password);

            }
            else{
                Toast.makeText(this, "Cannot connect to any network.",Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(this, "No IP Address found, have you registered this sensor?",Toast.LENGTH_LONG).show();
        }



    }

    private void requestData(String uri) {
        LoginTask login = new LoginTask();
        Log.i(TAG, "Executing Async Task");
        login.execute(uri);

    }

    /**
     * Called after Asynctask runs and gets user data from DB
     * @param result
     */
    private void confirmPassword(ArrayList<String> result) {

        if (Boolean.parseBoolean(result.get(0))){
            SharedPreferences userPrefs = getSharedPreferences("userPrefs",MODE_PRIVATE);
            SharedPreferences.Editor edit = userPrefs.edit();
            edit.putString("username", result.get(1));
            edit.putString("permissions", result.get(2));
            edit.putString("pin", result.get(3));
            edit.putString("id", result.get(4));
            edit.commit();

            Intent intentActivity = new Intent(this, Main_Menu.class);
            startActivity(intentActivity);

        } else {
            Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void register(View view) {
        Intent register = new Intent(this, RegisterDevice.class);
        startActivity(register);
    }


    //Class to instantiate the Async Task class for running
    //Http Requests in the background
    private class LoginTask extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            String content = getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            if(content == null){
                Toast.makeText(getApplicationContext(), "Could not connect to your PI, check your system and confirm IP address.",Toast.LENGTH_LONG).show();
            }else {
                try {
                    JsonParser parse = new JsonParser();
                    ArrayList result = parse.getLoginResult(content);
                    confirmPassword(result);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Could not connect to your PI, check your system and confirm IP address.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

}
