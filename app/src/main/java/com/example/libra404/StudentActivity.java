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

public class StudentActivity extends AppCompatActivity {
    TextView tvGreet;
    EditText etBorrow, etReturn;
    Button btnBorrow, btnReturn, btnRefresh;
    ListView listView;
    ArrayAdapter<String> adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        tvGreet = findViewById(R.id.tvGreetStudent);
        etBorrow = findViewById(R.id.etBorrowTitle);
        etReturn = findViewById(R.id.etReturnTitle);
        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);
        btnRefresh = findViewById(R.id.btnRefreshS);
        listView = findViewById(R.id.listBooksS);
        db = new DatabaseHelper(this);

        String name = getIntent().getStringExtra("name");
        tvGreet.setText("Welcome Classmate");

        btnBorrow.setOnClickListener(v -> {
            String t = etBorrow.getText().toString();
            if (t.isEmpty()) {
                Toast.makeText(this,"Enter title",Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.borrowBook(t);
            if (ok) {
                Toast.makeText(this,"Book borrowed",Toast.LENGTH_SHORT).show();
                etBorrow.setText("");
                loadList();
            } else {
                Toast.makeText(this,"Unavailable or not found",Toast.LENGTH_SHORT).show();
            }
        });

        btnReturn.setOnClickListener(v -> {
            String t = etReturn.getText().toString();
            if (t.isEmpty()) {
                Toast.makeText(this,"Enter title",Toast.LENGTH_SHORT).show();
                return;
            }
            boolean ok = db.returnBook(t);
            if (ok) {
                Toast.makeText(this,"Book returned",Toast.LENGTH_SHORT).show();
                etReturn.setText("");
                loadList();
            } else {
                Toast.makeText(this,"Not borrowed or not found",Toast.LENGTH_SHORT).show();
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
