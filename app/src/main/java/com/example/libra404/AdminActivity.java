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

public class AdminActivity extends AppCompatActivity {
    EditText etTitle, etAuthor, etDeleteTitle;
    Button btnAdd, btnDelete, btnRefresh, btnLogout;
    ListView listBooks;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDeleteTitle = findViewById(R.id.etDeleteTitle);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnRefresh = findViewById(R.id.btnRefreshA);
        btnLogout = findViewById(R.id.btnLogoutA); // <-- new logout button
        listBooks = findViewById(R.id.listBooksA);

        btnAdd.setOnClickListener(v -> {
            String t = etTitle.getText().toString();
            String a = etAuthor.getText().toString();
            if (db.addBook(t, a)) {
                Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
                loadBooks();
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String t = etDeleteTitle.getText().toString();
            if (db.deleteBook(t)) {
                Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
                loadBooks();
            } else {
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
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
