package com.example.libra404;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    DatabaseHelper db;
    RecyclerView listHistory;
    Button btnBackHistory;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(this);
        listHistory = findViewById(R.id.listHistory);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        String student = getIntent().getStringExtra("student");
        ArrayList<BorrowHistory> list = db.getBorrowHistory(student);

        historyAdapter = new HistoryAdapter(list);
        listHistory.setAdapter(historyAdapter);

        btnBackHistory.setOnClickListener(v -> finish());
    }
}
