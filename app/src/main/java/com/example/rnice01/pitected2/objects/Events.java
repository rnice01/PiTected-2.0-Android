package com.example.rnice01.pitected2.objects;

/**
 * Created by rnice01 on 3/16/2016.
 */
public class Events {
    public String eventStatus;
    private String eventDevice;
    private String eventDate;



    public Events(){

    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventString) {
        this.eventStatus = eventString;
    }

    public String getEventDevice() {
        return eventDevice;
    }

    public void setEventDevice(String eventDevice) {
        this.eventDevice = eventDevice;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
