package com.example.miniprojet;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM Event WHERE date = :date")
    List<Event> getEventsForDate(String date);
}
