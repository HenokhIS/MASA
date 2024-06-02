package com.example.cekcuaca.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cekcuaca.LoginActivity;
import com.example.cekcuaca.R;
import com.example.cekcuaca.sqlite.DbConfig;
import com.example.cekcuaca.fragment.MainFragment;

public class SettingsFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private Button buttonChangeUsername, buttonChangePassword, buttonLogout;
    private SharedPreferences sharedPreferences;
    private DbConfig dbConfig;
    private int recordId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonChangeUsername = view.findViewById(R.id.buttonChangeUsername);
        buttonChangePassword = view.findViewById(R.id.buttonChangePassword);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        dbConfig = new DbConfig(getContext());
        sharedPreferences = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        // Retrieve the logged-in user's record ID
        recordId = getLoggedInUserId();


        buttonChangeUsername.setOnClickListener(v -> changeUsername());
        buttonChangePassword.setOnClickListener(v -> changePassword());
        buttonLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private int getLoggedInUserId() {
        int userId = -1;
        String username = sharedPreferences.getString("username", "");
        SQLiteDatabase db = dbConfig.getReadableDatabase();
        Cursor cursor = db.query(DbConfig.TABLE_NAME, new String[]{DbConfig.COLUMN_ID},
                DbConfig.COLUMN_USERNAME + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
            cursor.close();
        }
        return userId;
    }

    private void changeUsername() {
        String newUsername = editTextUsername.getText().toString().trim();
        if (newUsername.isEmpty()) {
            editTextUsername.setError("Please enter a new username");
        } else {
            if (dbConfig.updateUsername(newUsername)) {
                Toast.makeText(getContext(), "Username updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update username", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changePassword() {
        String newPassword = editTextPassword.getText().toString().trim();
        if (newPassword.isEmpty()) {
            editTextPassword.setError("Please enter a new password");
        } else {
            if (dbConfig.updatePassword(newPassword)) {
                Toast.makeText(getContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void logoutUser() {
        try (SQLiteDatabase db = dbConfig.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(DbConfig.COLUMN_IS_LOGGED_IN, 0);
            db.update(DbConfig.TABLE_NAME, values, DbConfig.COLUMN_IS_LOGGED_IN + " = ?", new String[]{"1"});
        }

        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
