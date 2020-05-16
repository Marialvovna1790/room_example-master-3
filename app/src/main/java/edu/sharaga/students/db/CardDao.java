package edu.sharaga.students.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.sharaga.students.Card;

@Dao
public interface CardDao {
    // Data access object

    @Query("SELECT * FROM card")
    List<Card> getAll();

    @Insert
    long insert(Card card);
}
