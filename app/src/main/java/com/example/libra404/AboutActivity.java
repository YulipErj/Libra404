package com.example.libra404;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private String currentUserRole;
    private boolean fromLoginOrRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_about);

        currentUserRole = getIntent().getStringExtra("role");
        fromLoginOrRegister = getIntent().getBooleanExtra("fromLoginOrRegister", false);

        // Set up the action bar with back button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Hide about since we're already in about
        menu.findItem(R.id.action_about).setVisible(false);

        // If coming from login/register, hide all navigation options except theme
        if (fromLoginOrRegister) {
            menu.findItem(R.id.action_home).setVisible(false);
            menu.findItem(R.id.action_catalog).setVisible(false);
            menu.findItem(R.id.action_history).setVisible(false);
            menu.findItem(R.id.action_logout).setVisible(false);
        } else if ("admin".equals(currentUserRole)) {
            menu.findItem(R.id.action_history).setVisible(false);
        }

        // Set theme icon
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

        // Handle back button click
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_theme) {
            ThemeHelper.toggleTheme(this);
            recreate();
            return true;
        }

        // Don't handle other menu items if coming from login/register
        if (fromLoginOrRegister) {
            return super.onOptionsItemSelected(item);
        }

        // Handle other menu items only if logged in
        if (id == R.id.action_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_home) {
            if ("admin".equals(currentUserRole)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else {
                startActivity(new Intent(this, StudentActivity.class));
            }
            return true;
        } else if (id == R.id.action_catalog) {
            Intent intent = new Intent(this, BookListActivity.class);
            intent.putExtra("role", currentUserRole);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_history) {
            Intent intent = new Intent(this, HistoryActivity.class);
            intent.putExtra("role", currentUserRole);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
