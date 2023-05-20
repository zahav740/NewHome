
package com.alexey.newhome;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.alexey.newhome.Jv.SwipeDetector;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private EditText incomeInput;
    private EditText expenseNameInput;
    private EditText expenseInput;
    private Button balanceButton;
    private TableLayout tableLayout;
    private TextView balanceTextView;

    private float balance = 0.0f;

    DatabaseHelper myDb;

    private SwipeDetector swipeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        myDb = new DatabaseHelper(this);

        incomeInput = findViewById(R.id.incomeInput);
        expenseNameInput = findViewById(R.id.expenseNameInput);
        expenseInput = findViewById(R.id.expenseInput);
        balanceButton = findViewById(R.id.balanceButton);
        tableLayout = findViewById(R.id.tableLayout);
        balanceTextView = findViewById(R.id.balanceTextView);
        Button exitButton = findViewById(R.id.exitButton);

        swipeDetector = new SwipeDetector(20) {
            @Override
            public void onSwipeDetected(Direction direction) {
                switch (direction) {
                    case LEFT:
                        // Add your code for left swipe
                        break;
                    case RIGHT:
                        openHistoryActivity();
                        break;
                    case UP:
                        // Add your code for up swipe
                        break;
                    case DOWN:
                        // Add your code for down swipe
                        break;
                }
            }
        };

        balanceButton.setOnClickListener(v -> calculateBalance());

        loadDataFromDatabase();
        exitButton.setOnClickListener(v -> finishAffinity());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    private void openHistoryActivity() {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void calculateBalance() {
        float income = 0.0f;
        float expense = 0.0f;
        try {
            income = Float.parseFloat(incomeInput.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        try {
            expense = Float.parseFloat(expenseInput.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        balance += income - expense;

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String incomeStr = String.valueOf(income);
        String expenseName = expenseNameInput.getText().toString();
        String expenseStr = String.valueOf(expense);

        boolean isInserted = myDb.insertData(date, income, expenseName, expense);

        if(isInserted) {
            addRowToTable(date, incomeStr, expenseName, expenseStr);
            balanceTextView.setText(String.valueOf(balance));
            balanceButton.setText("Баланс: " + balance);
            incomeInput.setText("");
            expenseNameInput.setText("");
            expenseInput.setText("");
        }
    }

    private void loadDataFromDatabase() {
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return;
        }

        while (res.moveToNext()) {
            addRowToTable(res.getString(1), res.getString(2), res.getString(3), res.getString(4));
        }

        balance = calculateCurrentBalance();
        balanceTextView.setText(String.valueOf(balance));
        balanceButton.setText("Баланс: " + balance);
    }

    private float calculateCurrentBalance() {
        float currentBalance = 0.0f;
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return currentBalance;
        }

        while (res.moveToNext()) {
            float income = Float.parseFloat(res.getString(2));
            float expense = Float.parseFloat(res.getString(4));
            currentBalance += income - expense;
        }

        return currentBalance;
    }

    private void addRowToTable(String date, String income, String expenseName, String expense) {
        TableRow row = new TableRow(this);
        row.addView(createTextView(date));
        row.addView(createTextView(income));
        row.addView(createTextView(expenseName));
        row.addView(createTextView(expense));
        tableLayout.addView(row);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
