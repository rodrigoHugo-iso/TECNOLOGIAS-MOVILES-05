package com.ucsm.taskauth.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Task {

    @DocumentId
    private String id;
    private String title;
    private String description;
    private boolean completed;
    private Timestamp dueDate;       // Fecha del calendario
    private String userId;

    // Constructor vacío requerido por Firestore
    public Task() {}

    public Task(String title, String description, Timestamp dueDate, String userId) {
        this.title       = title;
        this.description = description;
        this.dueDate     = dueDate;
        this.userId      = userId;
        this.completed   = false;
    }

    // Getters & Setters
    public String getId()                    { return id; }
    public void   setId(String id)           { this.id = id; }

    public String getTitle()                 { return title; }
    public void   setTitle(String title)     { this.title = title; }

    public String getDescription()           { return description; }
    public void   setDescription(String d)   { this.description = d; }

    public boolean isCompleted()             { return completed; }
    public void    setCompleted(boolean c)   { this.completed = c; }

    public Timestamp getDueDate()            { return dueDate; }
    public void      setDueDate(Timestamp d) { this.dueDate = d; }

    public String getUserId()                { return userId; }
    public void   setUserId(String userId)   { this.userId = userId; }
}
