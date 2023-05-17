package com.alexey.newhome;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private TableLayout historyTable;
    private DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTable = findViewById(R.id.historyTable);
        myDb = new DatabaseHelper(this);

        // Загрузка истории транзакций из базы данных
        loadTransactionHistory();

        // Установка обработчика для кнопки "Назад"
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadTransactionHistory() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            addRowToTable(res.getString(1), res.getString(2), res.getString(3), res.getString(4));
        }
    }

    private void addRowToTable(String date, String income, String expenseName, String expense) {
        TableRow row = new TableRow(this);
        row.addView(createTextView(date));
        row.addView(createTextView(income));
        row.addView(createTextView(expenseName));
        row.addView(createTextView(expense));
        historyTable.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        return textView;
    }

    @Override
    public void onBackPressed() {
        // Возвращение в MainActivity
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
