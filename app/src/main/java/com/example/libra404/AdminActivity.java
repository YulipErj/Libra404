package com.example.libra404;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    TextView tvGreet;
    EditText etTitle, etAuthor, etDelete;
    Button btnAdd, btnDelete, btnRefresh;
    ListView listView;
    ArrayAdapter<String> adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        tvGreet = findViewById(R.id.tvGreetAdmin);
        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDelete = findViewById(R.id.etDeleteTitle);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnRefresh = findViewById(R.id.btnRefreshA);
        listView = findViewById(R.id.listBooksA);
        db = new DatabaseHelper(this);

        String name = getIntent().getStringExtra("name");
        tvGreet.setText("Welcome Ma’am/Sir");

        btnAdd.setOnClickListener(v -> {
            String t = etTitle.getText().toString();
            String a = etAuthor.getText().toString();
            if (t.isEmpty() || a.isEmpty()) {
                Toast.makeText(this,"Enter title and author",Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.addBook(t,a);
            if (ok) {
                Toast.makeText(this,"Book added",Toast.LENGTH_SHORT).show();
                etTitle.setText("");
                etAuthor.setText("");
                loadList();
            } else {
                Toast.makeText(this,"Failed or duplicate",Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            String t = etDelete.getText().toString();
            if (t.isEmpty()) {
                Toast.makeText(this,"Enter title",Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.deleteBook(t);
            if (ok) {
                Toast.makeText(this,"Book deleted",Toast.LENGTH_SHORT).show();
                etDelete.setText("");
                loadList();
            } else {
                Toast.makeText(this,"Not found",Toast.LENGTH_SHORT).show();
            }
        });

        btnRefresh.setOnClickListener(v -> loadList());

        loadList();
    }

    private void loadList() {
        ArrayList<String> data = db.getAllBooks();
        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
            listView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }
}
