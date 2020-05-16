package edu.sharaga.students.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.sharaga.students.TransactionHistory;

@Dao
public interface TransactionHistoryDao {
    @Query("SELECT * FROM transaction_history WHERE card_id = :id")
    List<TransactionHistory> getAll(long id);

    @Insert
    long insert(TransactionHistory transactionHistory);
}
