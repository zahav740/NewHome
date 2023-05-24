package com.alexey.newhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.alexey.newhome.Jv.SwipeDetector;

import java.util.ArrayList;
import java.util.List;

public class DatabaseViewerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private SwipeDetector swipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_viewer);

        recyclerView = findViewById(R.id.recyclerView);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor tablesCursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String[]> data = new ArrayList<>();

        while (tablesCursor.moveToNext()) {
            String tableName = tablesCursor.getString(0);
            if (tableName.equals("android_metadata") || tableName.equals("sqlite_sequence")) continue;

            Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
            data.add(new String[]{"Table: " + tableName});

            if (cursor.moveToFirst()) {
                String[] columnNames = cursor.getColumnNames();
                do {
                    String[] row = new String[columnNames.length];
                    for (int i = 0; i < columnNames.length; i++) {
                        row[i] = cursor.getString(i);
                    }
                    data.add(row);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        tablesCursor.close();
        db.close();

        adapter = new MyAdapter(data);
        recyclerView.setAdapter(adapter);

        swipeDetector = new SwipeDetector(20) {
            @Override
            public void onSwipeDetected(Direction direction) {
                switch (direction) {
                    case LEFT:
                        Intent intentLeft = new Intent(DatabaseViewerActivity.this, HistoryActivity.class);
                        startActivity(intentLeft);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    case RIGHT:
                        Intent intentRight = new Intent(DatabaseViewerActivity.this, MainActivity.class);
                        startActivity(intentRight);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                }
            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }
}

