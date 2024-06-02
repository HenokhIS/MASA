package com.example.cekcuaca.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cekcuaca.R;
import com.example.cekcuaca.sqlite.DbConfig;

public class AccountFragment extends Fragment {

    private TextView tvUsername, tvPassword;
    private ImageView ivShowPassword;
    private boolean isPasswordVisible = false;
    private DbConfig dbConfig;
    private int recordId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize views
        tvUsername = view.findViewById(R.id.tvUsername);
        tvPassword = view.findViewById(R.id.tvPassword);
        ivShowPassword = view.findViewById(R.id.ivShowPassword);

        // Initialize dbConfig
        dbConfig = new DbConfig(getActivity());

        loadUserData();

        // Set click listener for the show password icon
        ivShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });

        return view;
    }

    private void loadUserData() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_USERNAME},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                recordId = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_USERNAME));

                tvUsername.setText(username);

            } else {
                // Handle case when no data is found
                tvUsername.setText("No user logged in");
            }
        }
    }

    private void togglePasswordVisibility() {
        try (SQLiteDatabase db = dbConfig.getReadableDatabase();
             Cursor cursor = db.query(
                     DbConfig.TABLE_NAME,
                     new String[]{DbConfig.COLUMN_ID, DbConfig.COLUMN_PASSWORD},
                     DbConfig.COLUMN_IS_LOGGED_IN + " = ?",
                     new String[]{"1"},
                     null, null, null)) {

            if (cursor.moveToFirst()) {
                int password = cursor.getInt(cursor.getColumnIndexOrThrow(DbConfig.COLUMN_PASSWORD));
                if (isPasswordVisible) {
                    tvPassword.setText("********");
                    ivShowPassword.setImageResource(R.drawable.show_password);
                } else {
                    tvPassword.setText(String.valueOf(password));
                    ivShowPassword.setImageResource(R.drawable.hide_password);
                }
                isPasswordVisible = !isPasswordVisible;
            }
        }
    }
}
