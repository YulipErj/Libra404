package com.example.libra404;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AdminActivity extends AppCompatActivity {
    EditText etTitle, etAuthor, etDeleteTitle;
    Button btnAdd, btnDelete, btnImport;
    DatabaseHelper db;

    private final ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                parseFile(uri);
            }
        }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_admin);

        db = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitle);
        etAuthor = findViewById(R.id.etAuthor);
        etDeleteTitle = findViewById(R.id.etDeleteTitle);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnImport = findViewById(R.id.btnImport);

        btnAdd.setOnClickListener(v -> {
            String t = etTitle.getText().toString();
            String a = etAuthor.getText().toString();
            if (db.addBook(t, a)) {
                Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
            }
        });

        btnImport.setOnClickListener(v -> importBooksFromFile());

        btnDelete.setOnClickListener(v -> {
            String t = etDeleteTitle.getText().toString();
            if (db.deleteBook(t)) {
                Toast.makeText(this, "Book deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void importBooksFromFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        filePickerLauncher.launch(intent);
    }

    private void parseFile(Uri uri) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(uri)))) {
            String line;
            int addedCount = 0;
            int skippedCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (db.addBook(parts[0], parts[1])) {
                        addedCount++;
                    } else {
                        skippedCount++;
                    }
                }
            }
            Toast.makeText(this, "Import complete. Added: " + addedCount + ", Skipped: " + skippedCount, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to read file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.action_home).setVisible(false);
        menu.findItem(R.id.action_history).setVisible(false);

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
        } else if (id == R.id.action_catalog) {
            Intent intent = new Intent(this, BookListActivity.class);
            intent.putExtra("role", "admin");
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            intent.putExtra("role", "admin");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
