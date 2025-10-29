package com.example.libra404;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {
    EditText etBorrowTitle, etReturnTitle;
    Button btnBorrow, btnReturn;
    DatabaseHelper db;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_student);

        db = new DatabaseHelper(this);
        currentUser = getIntent().getStringExtra("name");

        etBorrowTitle = findViewById(R.id.etBorrowTitle);
        etReturnTitle = findViewById(R.id.etReturnTitle);
        btnBorrow = findViewById(R.id.btnBorrow);
        btnReturn = findViewById(R.id.btnReturn);

        btnBorrow.setOnClickListener(v -> {
            String t = etBorrowTitle.getText().toString();
            String borrowDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String dueDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));
            if (db.borrowBook(t, currentUser, borrowDate,dueDate)) {
                Toast.makeText(this, "Book borrowed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to borrow", Toast.LENGTH_SHORT).show();
            }
        });

        btnReturn.setOnClickListener(v -> {
            String t = etReturnTitle.getText().toString();
            String returnDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            if (db.returnBook(t, returnDate)) {
                Toast.makeText(this, "Book returned", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to return", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_home).setVisible(false);

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        MenuItem themeItem = menu.findItem(R.id.action_theme);
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeItem.setIcon(R.drawable.baseline_wb_sunny_24);
        } else {
            themeItem.setIcon(R.drawable.baseline_dark_mode_24);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_theme) {
            ThemeHelper.toggleTheme(this);
            recreate();
            return true;
        } else if (id == R.id.action_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.action_history) {
            Intent i = new Intent(this, HistoryActivity.class);
            i.putExtra("role", "student");
            i.putExtra("student", currentUser);
            startActivity(i);
            return true;
        } else if (id == R.id.action_catalog) {
            Intent i = new Intent(this, BookListActivity.class);
            i.putExtra("role", "student");
            i.putExtra("student", currentUser);
            startActivity(i);
            return true;
        } else if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            i.putExtra("role", "student");
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
