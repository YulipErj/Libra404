package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    EditText etBorrowTitle, etReturnTitle;
    Button btnBorrow, btnReturn, btnRefresh, btnHistory;
    RecyclerView listBooks;
    SearchView searchView;
    DatabaseHelper db;
    String currentUser;
    BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        db = new DatabaseHelper(this);
        currentUser = getIntent().getStringExtra("name");

        etBorrowTitle = findViewById(R.id.etBorrowTitle);
        etReturnTitle = findViewById(R.id.etReturnTitle);
        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);
        btnRefresh = findViewById(R.id.btnRefreshS);
        btnHistory = findViewById(R.id.btnHistory);
        listBooks = findViewById(R.id.listBooksS);
        searchView = findViewById(R.id.searchView);

        bookAdapter = new BookAdapter(new ArrayList<>());
        listBooks.setAdapter(bookAdapter);

        btnBorrow.setOnClickListener(v -> {
            String t = etBorrowTitle.getText().toString();
            String borrowDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String dueDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
            if (db.borrowBook(t, currentUser, borrowDate,dueDate)) {
                Toast.makeText(this, "Book borrowed", Toast.LENGTH_SHORT).show();
                loadBooks("");
            } else {
                Toast.makeText(this, "Failed to borrow", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturn.setOnClickListener(v -> {
            String t = etReturnTitle.getText().toString();
            String returnDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            if (db.returnBook(t, returnDate)) {
                Toast.makeText(this, "Book returned", Toast.LENGTH_SHORT).show();
                loadBooks("");
            } else {
                Toast.makeText(this, "Failed to return", Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> loadBooks(""));

        btnHistory.setOnClickListener(v -> {
            Intent i = new Intent(this, HistoryActivity.class);
            i.putExtra("student", currentUser);
            startActivity(i);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadBooks(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                loadBooks(newText);
                return false;
            }
        });

        loadBooks("");
    }

    private void loadBooks(String keyword) {
        ArrayList<Book> list;
        if (keyword.isEmpty()) {
            list = db.getAllBooks();
        } else {
            list = db.searchBooksToList(keyword);
        }
        bookAdapter.updateBooks(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
