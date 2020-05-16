package edu.sharaga.students;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "students", foreignKeys = @ForeignKey(entity = Card.class,
        parentColumns = "id",
        childColumns = "id",
        onDelete = CASCADE))
public class Student {

    @PrimaryKey
    @ColumnInfo(name = "id")
    public long mId;

    @ColumnInfo(name = "name")
    String mName;

    @ColumnInfo(name = "surname")
    String mSurname;

    @ColumnInfo(name = "group")
    String mGroup;

    /* in-memory field for join with EntryHistory table */
    public long entry_time;

    public Student(long id, String name, String surname, String group) {
        mId = id;
        mName = name;
        mSurname = surname;
        mGroup = group;
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getSurname() {
        return mSurname;
    }

    public String getGroup() {
        return mGroup;
    }

    public long getEntryTime() {
        return entry_time;
    }
}
