package com.example.cekcuaca;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cekcuaca.sqlite.DbConfig;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login, btn_register;
    private EditText et_username, et_password;
    private DbConfig dbConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbConfig = new DbConfig(this);

        et_username = findViewById(R.id.et_nim);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btn_login.setOnClickListener(view -> {
            String username = et_username.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (username.isEmpty()) {
                et_username.setError("Please enter your username");
            } else if (password.isEmpty()) {
                et_password.setError("Please enter your password");
            } else {
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_USERNAME + "=? AND " + DbConfig.COLUMN_PASSWORD + "=?",
                     new String[]{username, password},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                updateLoginStatus(username, true);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updateLoginStatus(String username, boolean isLoggedIn) {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, isLoggedIn ? 1 : 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_USERNAME + " = ?", new String[]{username});
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkLoginStatus();
    }

    private void checkLoginStatus() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.getCount() > 0) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}
