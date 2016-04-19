package com.example.rnice01.pitected2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.example.rnice01.pitected2.http.JsonParser;

import static com.example.rnice01.pitected2.http.HttpManager.getData;

public class RegisterDevice extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    EditText ipAddress, username, password;
    SharedPreferences userPrefs;
    Context context;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    // private ProgressBar mRegistrationProgressBar;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_device);
        ipAddress = (EditText)findViewById(R.id.ipAddress);
        username = (EditText)findViewById(R.id.usernameInput);
        password = (EditText)findViewById(R.id.passwordInput);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }




    public void registerDevice(View view) {
        if(isOnline()){
            //Write IP address to shared preferences to use later in all GET requests
            userPrefs = getSharedPreferences("userPrefs", this.MODE_PRIVATE);
            SharedPreferences.Editor editor = userPrefs.edit();
            editor.putString("ipAddress", ipAddress.getText().toString());
            editor.putString("username", username.getText().toString());
            editor.commit();
            //Verify the IP and user credentials are legit
            requestData("http://"+ ipAddress.getText().toString()+"/PiTected-Web-App/php/getUserData.php?username="+username.getText().toString()+"&password="+password.getText().toString());
        }
        else{
            Toast.makeText(this,"Connectivity error, check your network connection",Toast.LENGTH_LONG).show();
        }

    }

    private void requestData(String uri) {
        RegisterTask register = new RegisterTask();

        register.execute(uri);

    }

    public void getToken(){
        // Registering BroadcastReceiver
        registerReceiver();
        Log.i(TAG, "Executing check play services");
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

        }
    }

    //Class to instantiate the Async Task class for running
    //Http Requests in the background
    private class RegisterTask extends AsyncTask<String, String, String> {


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
                    Boolean result = parse.getRegistrationResult(content);
                    confirmRegistration(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void confirmRegistration(Boolean result) {
        if(result){
            //Generate GCM token and send user to login activity
           getToken();
        }
        else{
        Toast.makeText(this,"Invalid credentials", Toast.LENGTH_LONG).show();
        }
    }
}
