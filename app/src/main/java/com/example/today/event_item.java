package com.example.today;

import java.io.Serializable;

public class event_item implements Serializable {
    private String eventnameincalendar;
    private String eventnoteincalendar;
    private String eventtimeincalendar;
    private String am_pmincalendar;
    private String firebaseKey;

    public event_item(){

    }

    public event_item(String eventnameincalendar, String eventnoteincalendar, String eventtimeincalendar, String am_pmincalendar, String firebaseKey) {
        this.eventnameincalendar = eventnameincalendar;
        this.eventnoteincalendar = eventnoteincalendar;
        this.eventtimeincalendar = eventtimeincalendar;
        this.am_pmincalendar = am_pmincalendar;
        this.firebaseKey = firebaseKey;
    }

    public void setEventnameincalendar(String eventnameincalendar) {
        this.eventnameincalendar = eventnameincalendar;
    }

    public void setEventnoteincalendar(String eventnoteincalendar) {
        this.eventnoteincalendar = eventnoteincalendar;
    }

    public void setEventtimeincalendar(String eventtimeincalendar) {
        this.eventtimeincalendar = eventtimeincalendar;
    }

    public void setAm_pmincalendar(String am_pmincalendar) {
        this.am_pmincalendar = am_pmincalendar;
    }

    public void setFirebaseKey(String firebaseKey) {

        this.firebaseKey = firebaseKey;
    }


    public String getEventnameincalendar() {

        return eventnameincalendar;
    }

    public String getEventnoteincalendar() {

        return eventnoteincalendar;
    }

    public String getEventtimeincalendar() {

        return eventtimeincalendar;
    }

    public String getAm_pmincalendar() {

        return am_pmincalendar;
    }

    public String getFirebaseKey() {

        return firebaseKey;
    }
}
