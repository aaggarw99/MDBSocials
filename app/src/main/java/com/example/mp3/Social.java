package com.example.mp3;

public class Social {

    public String event_name;
    public String poster;
    public String desc;
    public String date;
    public String id;
    public String interested;


    public Social() { }

    public Social(String ename, String email, String description, String date, String id, String interest) {
        this.event_name = ename;
        this.poster = email;
        this.desc = description;
        this.date = date;
        this.id = id;
        this.interested = interest;
    }

    public String getEventName() {
        return event_name;
    }

}
