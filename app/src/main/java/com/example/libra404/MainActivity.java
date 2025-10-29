package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        ThemeHelper.applyTheme(this);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
