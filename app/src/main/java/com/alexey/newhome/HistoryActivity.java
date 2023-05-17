package com.alexey.newhome;

// Импортируйте необходимые пакеты

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    private TableLayout historyTable;
    private DatabaseHelper myDb;
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyTable = findViewById(R.id.historyTable);
        myDb = new DatabaseHelper(this);
        selectedDate = Calendar.getInstance();

        // Загрузка истории транзакций из базы данных за выбранную дату
        loadTransactionHistory(selectedDate);

        // Установка обработчика для кнопки "Назад"
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Установка обработчика для кнопки "Выбрать дату"
        Button selectDateButton = findViewById(R.id.selectDateButton);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void loadTransactionHistory(Calendar date) {
        // Очистка таблицы перед загрузкой новых данных
        historyTable.removeAllViews();

        // Получение истории транзакций из базы данных за выбранную дату
        // и отображение данных в таблице
        // Ваш код для загрузки и отображения данных истории транзакций
        // в соответствии с выбранной датой (год, месяц, день)
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

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, dayOfMonth);
                        loadTransactionHistory(selectedDate);
                    }
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        // Возвращение в MainActivity
        super.onBackPressed();
    }
}

