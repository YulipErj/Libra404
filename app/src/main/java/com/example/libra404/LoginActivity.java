package com.example.libra404;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etUser, etPass;
    Button btnLogin, btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String u = etUser.getText().toString().trim();
            String p = etPass.getText().toString().trim();

            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }

            String role = db.getRole(u, p);

            if (role == null) {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (role.equals("admin")) {
                startActivity(new Intent(this, AdminActivity.class).putExtra("name", u));
            } else if (role.equals("student")) {
                startActivity(new Intent(this, StudentActivity.class).putExtra("name", u));
            } else {
                Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}
