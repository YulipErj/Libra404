package com.example.libra404;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<BorrowHistory> historyList;

    public HistoryAdapter(List<BorrowHistory> historyList) {
        this.historyList = historyList;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        BorrowHistory history = historyList.get(position);
        holder.tvBook.setText(history.getBook());
        holder.tvBorrowDate.setText("Borrowed: " + history.getBorrowDate());
        holder.tvDueDate.setText("Due: " + history.getDueDate());
        holder.tvReturnDate.setText("Returned: " + (history.getReturnDate() == null ? "Not returned" : history.getReturnDate()));
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvBook;
        TextView tvBorrowDate;
        TextView tvDueDate;
        TextView tvReturnDate;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            tvBook = itemView.findViewById(R.id.tvHistoryBook);
            tvBorrowDate = itemView.findViewById(R.id.tvHistoryBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvHistoryDueDate);
            tvReturnDate = itemView.findViewById(R.id.tvHistoryReturnDate);
        }
    }
}
