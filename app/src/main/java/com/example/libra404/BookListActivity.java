package com.example.libra404;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private BookAdapter bookAdapter;
    private String currentUser;
    private String currentUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_book_list);

        db = new DatabaseHelper(this);
        currentUser = getIntent().getStringExtra("student");
        currentUserRole = getIntent().getStringExtra("role");

        RecyclerView rvBooks = findViewById(R.id.rvBooks);
        SearchView searchView = findViewById(R.id.searchViewBooks);

        bookAdapter = new BookAdapter(new ArrayList<>());
        rvBooks.setAdapter(bookAdapter);

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
        ArrayList<Book> list;
        if (keyword.isEmpty()) {
            list = db.getAllBooks();
        } else {
            list = db.searchBooksToList(keyword);
        }
        bookAdapter.updateBooks(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        menu.findItem(R.id.action_catalog).setVisible(false);

        if ("admin".equals(currentUserRole)) {
            menu.findItem(R.id.action_history).setVisible(false);
        }

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
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_history) {
            Intent i = new Intent(this, HistoryActivity.class);
            i.putExtra("role", currentUserRole);
            i.putExtra("student", currentUser);
            startActivity(i);
            return true;
        } else if (id == R.id.action_home) {
            if ("admin".equals(currentUserRole)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, StudentActivity.class));
            }
            return true;
        } else if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            i.putExtra("role", currentUserRole);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
