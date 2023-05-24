package com.alexey.newhome;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class HistoryActivity extends AppCompatActivity {

    private TableLayout historyTable;
    private DatabaseHelper myDb;
    private Calendar selectedDate;
    private StringBuilder data;
    private ActivityResultLauncher<Intent> saveFileLauncher;
    private Button backButton;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));

        historyTable = findViewById(R.id.historyTable);
        myDb = new DatabaseHelper(this);
        selectedDate = Calendar.getInstance();

        Button buttonYear = findViewById(R.id.buttonYear);
        buttonYear.setOnClickListener(v -> showYearPickerDialog());

        Button buttonMonth = findViewById(R.id.buttonMonth);
        buttonMonth.setOnClickListener(v -> showMonthPickerDialog());

        Button buttonDay = findViewById(R.id.buttonDay);
        buttonDay.setOnClickListener(v -> showDatePickerDialog());

        saveFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        saveDataToFile(uri);
                    } else {
                        showMessage("Не удалось получить URI файла");
                    }
                } else {
                    showMessage("Не удалось получить данные о сохраненном файле");
                }
            } else {
                showMessage("Файл не был сохранен");
            }
        });

        Button downloadButton = findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(v -> exportTransactionHistory());

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX < 0) { // меняем условие на "меньше"
                        onSwipeRight();
                    }
                }
            }
            return true;
        }

        public void onSwipeRight() {
            Intent intent = new Intent(HistoryActivity.this, DatabaseViewerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
        }
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
        textView.setTextColor(getResources().getColor(R.color.white));
        return textView;
    }

    private void showDatePickerDialog() {
        final Calendar currentDate = selectedDate;
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int dayOfMonth = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth1) -> {
                    selectedDate.set(year1, month1, dayOfMonth1);
                    loadTransactionHistory(selectedDate);
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
                (view, year1, month, dayOfMonth) -> {
                    selectedDate.set(year1, Calendar.JANUARY, 1);
                    loadTransactionHistory(selectedDate);
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
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, 1);
                    loadTransactionHistory(selectedDate);
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

    private void exportTransactionHistory() {
        Cursor res = myDb.getAllData();

        if (res == null || res.getCount() == 0) {
            showMessage("Нет данных для экспорта");
            return;
        }

        data = new StringBuilder();
        data.append("Дата,Доход,Статья,Расход\n");
        while (res.moveToNext()) {
            String transactionDate = res.getString(1);
            String income = res.getString(2);
            String expenseName = res.getString(3);
            String expense = res.getString(4);
            data.append(transactionDate).append(",").append(income).append(",").append(expenseName).append(",").append(expense).append("\n");
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "transaction_history.csv");

        saveFileLauncher.launch(intent);
    }

    private void saveDataToFile(Uri uri) {
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            if (outputStream != null) {
                outputStream.write(data.toString().getBytes());
                outputStream.close();
                showMessage("Файл успешно сохранен");
            } else {
                showMessage("Не удалось открыть поток для сохранения файла");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Ошибка при сохранении файла");
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
