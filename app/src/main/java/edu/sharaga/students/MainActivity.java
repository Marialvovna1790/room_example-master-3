package edu.sharaga.students;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.sharaga.students.db.SchoolDatabase;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Student> mStudents = new ArrayList<>();
    private ArrayList<Student> mSearchResult = new ArrayList<>();

    private String[] mGroups = new String[]{"P3-3", "P2-3", "1-KSK-11", "2-IS-13"};
    private String[] mCompanies = new String[]{"FACEBOOK", "YAHOO", "GOOGLE", "MAILRU", "YANDEX"};

    private StudentsAdapter mAdapter;
    private StudentsAdapter mResultListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText name = findViewById(R.id.name);
        final EditText surname = findViewById(R.id.surname);
        RecyclerView list = findViewById(R.id.list);
        final RecyclerView resultList = findViewById(R.id.result_list);
        mAdapter = new StudentsAdapter(mStudents);
        mResultListAdapter = new StudentsAdapter(mSearchResult);
        resultList.setAdapter(mResultListAdapter);
        list.setAdapter(mAdapter);
        resultList.setLayoutManager(new LinearLayoutManager(this, 1, false));
        list.setLayoutManager(new LinearLayoutManager(this, 1, false));

        Button button = findViewById(R.id.add);
        Button buttonSearch = findViewById(R.id.search);

        buttonSearch.setOnClickListener(v -> {
            hideKeyboard(this);
            mSearchResult.clear();
            mResultListAdapter.notifyDataSetChanged();
            EditText sname = findViewById(R.id.s_name);
            EditText ssurname = findViewById(R.id.s_surname);
            search(sname.getText().toString(), ssurname.getText().toString());
        });

        button.setOnClickListener(v -> AppExecutors.getInstance().diskIO().execute(() -> {
            insertRecords(name, surname);
            refreshList();
        }));
        AppExecutors.getInstance().diskIO().execute(this::refreshList);
    }

    private void insertRecords(EditText name, EditText surname) {
        // creating card
        Card card = new Card();
        long generatedId = SchoolDatabase.getInstance(getApplicationContext()).getCardDao().insert(card);

        // creating student
        Student student = new Student(generatedId, name.getText().toString(), surname.getText().toString(), randomElement(mGroups));
        SchoolDatabase.getInstance(getApplicationContext()).getStudentsDao().insert(student);

        // creating entry history record
        EntryHistory history = new EntryHistory(generatedId, generateRandomDate());
        SchoolDatabase.getInstance(getApplicationContext()).getEntryHistoryDao().insert(history);

        for (int i = 0; i < 10; i++) {
            TransactionHistory transaction = new TransactionHistory(generatedId, generateRandomDate(), randomElement(mCompanies), new Random().nextInt(1000));
            SchoolDatabase.getInstance(getApplicationContext()).getTransactionHistoryDao().insert(transaction);
        }
    }

    private String randomElement(String[] elements) {
        int g = new Random().nextInt(elements.length);
        return elements[g];
    }

    private void refreshList() {
        List<Student> all = SchoolDatabase.getInstance(getApplicationContext()).getStudentsDao().getAll();
        AppExecutors.getInstance().mainThread().execute(() -> {
            mStudents.clear();
            mStudents.addAll(all);
            mAdapter.notifyDataSetChanged();
        });
    }

    private void search(String name, String surname) {
        name = "%" + name + "%";
        surname = "%" + surname + "%";
        String finalName = name;
        String finalSurname = surname;
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<Student> all = SchoolDatabase.getInstance(getApplicationContext())
                    .getStudentsDao().queryByNameSurname(finalName, finalSurname);
            AppExecutors.getInstance().mainThread().execute(() -> {
                mSearchResult.addAll(new ArrayList<>(all));
                mResultListAdapter.notifyDataSetChanged();
            });
        });
    }

    private long generateRandomDate() {
        return new Random().nextLong();
    }


    private class VH extends RecyclerView.ViewHolder {

        private final TextView mNameSurname;

        public VH(@NonNull View itemView) {
            super(itemView);
            mNameSurname = itemView.findViewById(R.id.nameSurname);
        }
    }

    private class StudentsAdapter extends RecyclerView.Adapter<VH> {

        SimpleDateFormat format = new SimpleDateFormat("2018.MM.dd 'at' HH:mm:ss");
        private ArrayList<Student> mItems;

        public StudentsAdapter(ArrayList<Student> students) {
            mItems = students;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.student_item, viewGroup, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {
            Student student = mItems.get(i);
            vh.mNameSurname.setText(student.mName + " " + "\n" + student.mSurname + " " + " " + student.mGroup + " " +
                    format.format(new Date(student.getEntryTime())));
            vh.itemView.setOnClickListener(v -> {
                getSupportFragmentManager().beginTransaction()
                        .replace(android.R.id.content, TransactionsFragment.newInstance(student.mId), "tag")
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
