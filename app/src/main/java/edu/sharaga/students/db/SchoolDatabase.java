package edu.sharaga.students.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import edu.sharaga.students.Card;
import edu.sharaga.students.EntryHistory;
import edu.sharaga.students.Student;
import edu.sharaga.students.TransactionHistory;

@Database(entities = {Student.class, Card.class, EntryHistory.class, TransactionHistory.class}, version = 1)
public abstract class SchoolDatabase extends RoomDatabase {
    private static SchoolDatabase sInstance;

    public abstract StudentsDao getStudentsDao();

    public abstract CardDao getCardDao();

    public abstract EntryHistoryDao getEntryHistoryDao();

    public abstract TransactionHistoryDao getTransactionHistoryDao();

    public static synchronized SchoolDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context, SchoolDatabase.class, "school_database.db")
                    .build();
        }
        return sInstance;
    }
}
