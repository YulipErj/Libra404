package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    EditText etBorrowTitle, etReturnTitle;
    Button btnBorrow, btnReturn, btnRefresh, btnLogout, btnHistory;
    ListView listBooks;
    SearchView searchView;
    DatabaseHelper db;
    String currentUser = "student1"; // replace with actual logged in user later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        db = new DatabaseHelper(this);

        etBorrowTitle = findViewById(R.id.etBorrowTitle);
        etReturnTitle = findViewById(R.id.etReturnTitle);
        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);
        btnRefresh = findViewById(R.id.btnRefreshS);
        btnLogout = findViewById(R.id.btnLogoutS);
        btnHistory = findViewById(R.id.btnHistory);
        listBooks = findViewById(R.id.listBooksS);
        searchView = findViewById(R.id.searchView);

        btnBorrow.setOnClickListener(v -> {
            String t = etBorrowTitle.getText().toString();
            String borrowDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String dueDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis() + 7L*24*60*60*1000));
            if (db.borrowBook(t, currentUser, borrowDate, dueDate)) {
                Toast.makeText(this, "Book borrowed", Toast.LENGTH_SHORT).show();
                loadBooks("");
            } else {
                Toast.makeText(this, "Failed to borrow", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturn.setOnClickListener(v -> {
            String t = etReturnTitle.getText().toString();
            if (db.returnBook(t)) {
                Toast.makeText(this, "Book returned", Toast.LENGTH_SHORT).show();
                loadBooks("");
            } else {
                Toast.makeText(this, "Failed to return", Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> loadBooks(""));

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

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
        ArrayList<String> list = new ArrayList<>();
        if (keyword.isEmpty()) {
            list = db.getAllBooks();
        } else {
            var c = db.searchBooks(keyword);
            if (c.moveToFirst()) {
                do {
                    String t = c.getString(0);
                    String a = c.getString(1);
                    int b = c.getInt(2);
                    String s = b==1 ? "Borrowed" : "Available";
                    list.add(t + " • " + a + " • " + s);
                } while (c.moveToNext());
            }
            c.close();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listBooks.setAdapter(adapter);
    }
}
