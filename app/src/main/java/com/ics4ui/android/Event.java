package com.ics4ui.android;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Event {
    public String title;
    public String description;
    public String location;
    public String group;
    public Time startTime;
    public Time endTime;

    public Event() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("location", location);
        result.put("group", group);
        result.put("startTime", startTime);
        result.put("endTime", endTime);

        return result;
    }
}
