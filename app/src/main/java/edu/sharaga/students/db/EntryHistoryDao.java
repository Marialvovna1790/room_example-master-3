package edu.sharaga.students.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.sharaga.students.Card;
import edu.sharaga.students.EntryHistory;
import edu.sharaga.students.TransactionHistory;

@Dao
public interface EntryHistoryDao {
    // Data access object

    @Query("SELECT * FROM entry_history")
    List<EntryHistory> getAll();

    @Insert
    long insert(EntryHistory entryHistory);
}
