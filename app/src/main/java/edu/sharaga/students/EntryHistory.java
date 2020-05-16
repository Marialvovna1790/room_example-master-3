package edu.sharaga.students;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "entry_history", foreignKeys = @ForeignKey(entity = Card.class,
        parentColumns = "id",
        childColumns = "id",
        onDelete = CASCADE))
public class EntryHistory {

    @ColumnInfo(name = "id")
    public long id;

    @PrimaryKey
    @ColumnInfo(name = "entry_time")
    long mTimestamp;

    public EntryHistory(long id, long timestamp) {
        this.id = id;
        mTimestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
