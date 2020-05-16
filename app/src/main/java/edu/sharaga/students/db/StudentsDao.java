package edu.sharaga.students.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.sharaga.students.Student;

@Dao
public interface StudentsDao {
    @Query("SELECT * FROM students join entry_history on entry_history.id = students.id")
    List<Student> getAll();

    @Insert
    long insert(Student student);

    @Query("SELECT * FROM students join entry_history on entry_history.id = students.id" +
            " WHERE name LIKE :name AND surname LIKE :surname ")
    List<Student> queryByNameSurname(String name, String surname);
}
