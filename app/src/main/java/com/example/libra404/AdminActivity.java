package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    EditText etTitle, etAuthor, etDeleteTitle;
    Button btnAdd, btnDelete, btnRefresh;
    RecyclerView listBooks;
    DatabaseHelper db;
    BookAdapter bookAdapter;

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
        listBooks = findViewById(R.id.listBooksA);

        bookAdapter = new BookAdapter(new ArrayList<>());
        listBooks.setAdapter(bookAdapter);

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

        loadBooks();
    }

    private void loadBooks() {
        ArrayList<Book> list = db.getAllBooks();
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
