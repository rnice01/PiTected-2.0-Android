package com.example.rnice01.pitected2.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.rnice01.pitected2.ContextClass;
import com.example.rnice01.pitected2.MainActivity;
import com.example.rnice01.pitected2.objects.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.rnice01.pitected2.objects.Devices;
import com.example.rnice01.pitected2.objects.Events;
import com.example.rnice01.pitected2.objects.SystemLog;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class JsonParser {
    private Context context;
    public static User user;
    MainActivity main = new MainActivity();


    public User getUser(String content) {
        try {
            JSONArray ja = new JSONArray(content);
            user = new User();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject obj = ja.getJSONObject(i);


                user.setUserID(obj.getString("id"));
                user.setUsername(obj.getString("username"));
                user.setPassword(obj.getString("password"));

            }

            return user;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Takes content front background task, parses as JSON data for devices
    public List<Devices> parseDeviceFeed(String content) {
        try {
            JSONArray ja1 = new JSONArray(content);
            //Creating a list of UserDeviceStatus objects
            List<Devices> devices = new ArrayList<>();

            for (int i = 0; i < ja1.length(); i++) {
                JSONObject obj = ja1.getJSONObject(i);
                Devices devicesListed = new Devices();
                //Creating objects from the JSON data posted in the provided URI
                devicesListed.setDeviceName(obj.getString("name"));
                devicesListed.setDeviceStatus(obj.getString("status"));



                devices.add(devicesListed);
            }
            return devices;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  List<Events> parseEventFeed(String content) {
        try {
            JSONArray ja2 = new JSONArray(content);

            //Creating a list of Event objects
            List<Events> events = new ArrayList<>();

            for (int i = 0; i < ja2.length(); i++) {
                JSONObject obj = ja2.getJSONObject(i);
                Events eventList = new Events();
                //Creating objects from the JSON data posted in the provided URI
                eventList.setEventDevice(obj.getString("name"));
                eventList.setEventDate(obj.getString("timestamp"));
                eventList.setEventStatus(obj.getString("status"));
                //Check the the status and the device type and set the
                //event status accordingly


                events.add(eventList);
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  String parseSystemFeed(String s) throws JSONException {
        JSONArray ja3 = new JSONArray(s);

        String status = "";

        for (int i = 0; i < ja3.length(); i++) {
            JSONObject obj = ja3.getJSONObject(i);

            status = obj.getString("status");
        }
        return status;
    }

    public  List<SystemLog> parseSystemLogs(String content) {
        try {
            JSONArray ja4 = new JSONArray(content);

            //Creating a list of Event objects
            List<SystemLog> logList = new ArrayList<>();

            for (int i = 0; i < ja4.length(); i++) {
                JSONObject obj = ja4.getJSONObject(i);
                SystemLog log = new SystemLog();
                //Creating objects from the JSON data posted in the provided URI
                log.setUsername(obj.getString("username"));
                log.setTimeStamp(obj.getString("timestamp"));
                log.setStatus(obj.getString("status"));


                logList.add(log);
            }
            return logList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String parseSystemKey(String content) {
        try {
            JSONArray ja4 = new JSONArray(content);

            String key = "";
            for (int i = 0; i < ja4.length(); i++) {
                JSONObject obj = ja4.getJSONObject(i);
                key = obj.getString("passphrase");

            }
            return key;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean comparePin(String contentFromRequest, String enteredPin) {
        try {
            JSONArray ja4 = new JSONArray(contentFromRequest);

            String key = "";
            for (int i = 0; i < ja4.length(); i++) {
                JSONObject obj = ja4.getJSONObject(i);
                key = obj.getString("pin");

            }
            if(enteredPin.equals(key)){
                return true;
            }
           else{
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getLoginResult(String content) {
        user = new User();
        try {

            ArrayList<String> preferences = new ArrayList<String>();
                JSONObject obj = new JSONObject(content);

                preferences.add(0,obj.getString("result"));
                preferences.add(1, obj.getString("username"));
                preferences.add(2, obj.getString("permissions"));
                preferences.add(3, obj.getString("pin"));
                preferences.add(4, obj.getString("id"));


            return preferences;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public int getSystemStatus(String content) {
        try {
            int result;
            JSONObject obj = new JSONObject(content);

            result = obj.getInt("status");

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Boolean getArmingResult(String content) {
        try {
            Boolean result = false;
            JSONObject obj = new JSONObject(content);

            result = obj.getBoolean("result");

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getSensorResult(String content) {
        try {
            ArrayList<String> result = new ArrayList<>();
            if(content != "") {
                JSONArray jArray = new JSONArray(content);


                if (jArray != null) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject obj = (JSONObject) jArray.get(i);
                        result.add(obj.getString("name"));
                    }
                }
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean getRegistrationResult(String content) {
        try {

            JSONObject obj = new JSONObject(content);

            Boolean result = obj.getBoolean("result");

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
