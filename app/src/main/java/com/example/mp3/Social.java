package com.example.mp3;

import android.net.Uri;

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
        this.id = id; // this is the id for the key, "key.png"
        this.interested = interest;
    }

    public String getEventName() {
        return event_name;
    }

    public String getPoster() { return poster; }

    public String getInterested() { return interested; }

    public String getId() { return id; }

    public String getDate() { return date; }

}
