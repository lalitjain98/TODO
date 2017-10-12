package com.example.lalit.todo;

/**
 * Created by jainl on 17-09-2017.
 */

public class Todo {
    private int id;
    private String title;
    private String dateCreated;
    private String description;
    private long alarmDate;
    public Todo(int id, String title, String dateCreated, String description, long alarmDate) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.description = description;
        this.alarmDate = alarmDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public long getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(long alarmDate) {
        this.alarmDate = alarmDate;
    }
}
