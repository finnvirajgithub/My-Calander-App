package com.example.today;

public class event_item {
    private String eventnameincalendar;
    private String eventnoteincalendar;
    private String eventtimeincalendar;
    private String am_pmincalendar;

    public event_item(){

    }

    public event_item(String eventnameincalendar, String eventnoteincalendar, String eventtimeincalendar, String am_pmincalendar) {
        this.eventnameincalendar = eventnameincalendar;
        this.eventnoteincalendar = eventnoteincalendar;
        this.eventtimeincalendar = eventtimeincalendar;
        this.am_pmincalendar = am_pmincalendar;
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
}
