package edu.sharaga.students;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "transaction_history", foreignKeys = @ForeignKey(entity = Card.class,
        parentColumns = "id",
        childColumns = "card_id",
        onDelete = CASCADE))
public class TransactionHistory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    public long id;

    @ColumnInfo(name = "card_id")
    public long cardId;

    @ColumnInfo(name = "transaction_time")
    public long mTimestamp;

    @ColumnInfo(name = "amount")
    public long amount;

    @ColumnInfo(name = "company")
    public String company;

    public TransactionHistory(long cardId, long timestamp, String company, long amount) {
        this.company = company;
        this.cardId = cardId;
        this.amount = amount;
        this.mTimestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public long getTimestamp() {
        return mTimestamp;
    }
}
