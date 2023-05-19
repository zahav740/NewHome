package com.alexey.newhome;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

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

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button buttonYear = findViewById(R.id.buttonYear);
        buttonYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showYearPickerDialog();
            }
        });

        Button buttonMonth = findViewById(R.id.buttonMonth);
        buttonMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMonthPickerDialog();
            }
        });

        Button buttonDay = findViewById(R.id.buttonDay);
        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void loadTransactionHistory(Calendar date) {
        historyTable.removeAllViews();

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1;
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

        Cursor res;
        if (year != 0 && month != 0 && dayOfMonth != 0) {
            res = myDb.getTransactionHistory(year, month, dayOfMonth);
        } else if (year != 0 && month != 0) {
            res = myDb.getTransactionHistoryByMonth(year, month);
        } else if (year != 0) {
            res = myDb.getTransactionHistoryByYear(year);
        } else {
            res = myDb.getAllData();
        }

        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            String transactionDate = res.getString(1);
            String income = res.getString(2);
            String expenseName = res.getString(3);
            String expense = res.getString(4);

            addRowToTable(transactionDate, income, expenseName, expense);
        }
    }

    private void addRowToTable(String date, String income, String expenseName, String expense) {
        TableRow row = new TableRow(this);
        row.addView(createStyledTextView(date));
        row.addView(createStyledTextView(income));
        row.addView(createStyledTextView(expenseName));
        row.addView(createStyledTextView(expense));
        historyTable.addView(row);
    }

    private TextView createStyledTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextSize(18);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return textView;
    }

    private void showDatePickerDialog() {
        final Calendar currentDate = selectedDate;
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, dayOfMonth);
                        loadTransactionHistory(selectedDate);
                    }
                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.setTitle("Выберите дату");
        datePickerDialog.show();
    }

    private void showYearPickerDialog() {
        final Calendar currentDate = selectedDate;
        int year = currentDate.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, Calendar.JANUARY, 1);
                        loadTransactionHistory(selectedDate);
                    }
                },
                year,
                Calendar.JANUARY,
                1
        );

        datePickerDialog.setTitle("Выберите год");
        try {
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }

    private void showMonthPickerDialog() {
        final Calendar currentDate = selectedDate;
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate.set(year, month, 1);
                        loadTransactionHistory(selectedDate);
                    }
                },
                year,
                month,
                1
        );

        datePickerDialog.setTitle("Выберите месяц и год");
        try {
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        datePickerDialog.show();
    }
}





