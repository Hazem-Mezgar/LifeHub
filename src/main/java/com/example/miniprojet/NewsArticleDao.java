package com.example.miniprojet;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface NewsArticleDao {
    @Insert
    void insert(NewsArticle article);

    @Query("SELECT * FROM news_articles ORDER BY published_at DESC")
    List<NewsArticle> getAllArticles();

    @Query("DELETE FROM news_articles")
    void deleteAll();
}