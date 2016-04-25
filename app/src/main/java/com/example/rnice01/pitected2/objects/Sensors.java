package com.example.rnice01.pitected2.objects;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class Sensors {
    String sensorName;
    String sensorType;
    String sensorstatus;

    public Sensors(){

    }
    public Sensors(String sensorName, String sensorType, String sensorstatus) {
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.sensorstatus = sensorstatus;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getSensorStatus() {
        return sensorstatus;
    }

    public void setSensorStatus(String sensorstatus) {
        this.sensorstatus = sensorstatus;
    }

}
