package com.example.miniprojet;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String date; // Format: "dd/MM/yyyy"
    public String title;
    public String time;
}
