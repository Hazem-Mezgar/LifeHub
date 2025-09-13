package com.example.miniprojet;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {NewsArticle.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsArticleDao newsArticleDao();

    private static volatile NewsDatabase instance;

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            NewsDatabase.class, "news_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}