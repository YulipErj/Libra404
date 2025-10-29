package com.example.libra404;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> bookList;

    public BookAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvBookTitle.setText(book.getTitle());
        holder.tvBookAuthor.setText(book.getAuthor());
        holder.tvBookStatus.setText(book.isBorrowed() ? "Borrowed" : "Available");

        if (book.isBorrowed()) {
            holder.tvBookTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_bookmark_24, 0, 0, 0);
            holder.tvBookTitle.getCompoundDrawables()[0].setTint(Color.GREEN);
        } else {
            holder.tvBookTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_library_books_24, 0, 0, 0);
            holder.tvBookTitle.getCompoundDrawables()[0].setTintList(null);
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBooks(List<Book> newBookList) {
        this.bookList = newBookList;
        notifyDataSetChanged();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle;
        TextView tvBookAuthor;
        TextView tvBookStatus;

        public BookViewHolder(View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvBookAuthor = itemView.findViewById(R.id.tvBookAuthor);
            tvBookStatus = itemView.findViewById(R.id.tvBookStatus);
        }
    }
}
