package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    DatabaseHelper db;
    ListView listHistory;
    Button btnBackHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(this);
        listHistory = findViewById(R.id.listHistory);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        String student = getIntent().getStringExtra("student");
        ArrayList<String> list = db.getBorrowHistory(student);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listHistory.setAdapter(adapter);

        btnBackHistory.setOnClickListener(v -> {
            Intent i = new Intent(this, StudentActivity.class);
            startActivity(i);
            finish();
        });
    }
}
