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
            String u = etUser.getText().toString();
            String p = etPass.getText().toString();
            String role = db.getRole(u, p);
            if (role == null) {
                Toast.makeText(this,"Invalid login",Toast.LENGTH_SHORT).show();
                return;
            }
            if (role.equals("admin")) {
                Intent i = new Intent(this, AdminActivity.class);
                i.putExtra("name", u);
                startActivity(i);
            } else {
                Intent i = new Intent(this, StudentActivity.class);
                i.putExtra("name", u);
                startActivity(i);
            }
        });

        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        });
    }
}
