package edu.sharaga.students;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.sharaga.students.db.SchoolDatabase;

public class TransactionsFragment extends Fragment {

    private List<TransactionHistory> mTransactionHistories = new ArrayList<>();
    private TransactionsAdapter mAdapter;

    public static TransactionsFragment newInstance(long id) {
        TransactionsFragment transactionsFragment = new TransactionsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        transactionsFragment.setArguments(bundle);
        return transactionsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.transaction_screen, container, false);
        RecyclerView recyclerView = inflate.findViewById(R.id.transactions);
        mAdapter = new TransactionsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);
        AppExecutors.getInstance().diskIO().execute(() -> {
            List<TransactionHistory> historyList = SchoolDatabase.getInstance(getContext()).getTransactionHistoryDao().getAll(getArguments().getLong("id"));
            AppExecutors.getInstance().mainThread().execute(() -> {
                mTransactionHistories.clear();
                mTransactionHistories.addAll(historyList);
                mAdapter.notifyDataSetChanged();
            });
        });
        return inflate;
    }

    private class TransactionsAdapter extends RecyclerView.Adapter<VH> {

        SimpleDateFormat format = new SimpleDateFormat("2018.MM.dd 'at' HH:mm:ss");

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH vh, int i) {
            TransactionHistory transactionHistory = mTransactionHistories.get(i);
            String result = "Transaction: \n Amount: %d; Company: %s; Date: %s";
            vh.mView.setText(String.format(result, transactionHistory.amount,
                    transactionHistory.company,
                    format.format(new Date(transactionHistory.mTimestamp))));
        }

        @Override
        public int getItemCount() {
            return mTransactionHistories.size();
        }
    }

    private class VH extends RecyclerView.ViewHolder {

        TextView mView;

        public VH(@NonNull View itemView) {
            super(itemView);
            mView = itemView.findViewById(R.id.transaction);
        }
    }
}
