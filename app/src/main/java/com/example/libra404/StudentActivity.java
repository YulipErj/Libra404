package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    EditText etBorrowTitle, etReturnTitle;
    Button btnBorrow, btnReturn, btnRefresh, btnLogout;
    ListView listBooks;
    DatabaseHelper db;

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
        btnLogout = findViewById(R.id.btnLogoutS); // <-- new logout button
        listBooks = findViewById(R.id.listBooksS);

        btnBorrow.setOnClickListener(v -> {
            String t = etBorrowTitle.getText().toString();
            if (db.borrowBook(t)) {
                Toast.makeText(this, "Book borrowed", Toast.LENGTH_SHORT).show();
                loadBooks();
            } else {
                Toast.makeText(this, "Failed to borrow (maybe not available)", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturn.setOnClickListener(v -> {
            String t = etReturnTitle.getText().toString();
            if (db.returnBook(t)) {
                Toast.makeText(this, "Book returned", Toast.LENGTH_SHORT).show();
                loadBooks();
            } else {
                Toast.makeText(this, "Failed to return (maybe already available)", Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> loadBooks());

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadBooks();
    }

    private void loadBooks() {
        ArrayList<String> list = db.getAllBooks();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listBooks.setAdapter(adapter);
    }
}
