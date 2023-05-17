package com.alexey.newhome;

public class One {

//    package com.alexey.newhome;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Scanner;
//
//public class MainActivity extends AppCompatActivity {
//    private EditText incomeInput;
//    private EditText expenseNameInput;
//    private EditText expenseInput;
//    private Button balanceButton;
//    private TableLayout tableLayout;
//    private TextView balanceTextView;
//
//    private float balance = 0.0f;
//
//    private static final String FILENAME = "finances.json";
//    private static final String FILENAME_ARCHIVE = "finances-%s.json";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        incomeInput = findViewById(R.id.incomeInput);
//        expenseNameInput = findViewById(R.id.expenseNameInput);
//        expenseInput = findViewById(R.id.expenseInput);
//        balanceButton = findViewById(R.id.balanceButton);
//        tableLayout = findViewById(R.id.tableLayout);
//        balanceTextView = findViewById(R.id.balanceTextView);
//
//        balanceButton.setOnClickListener(v -> calculateBalance());
//
//
//        loadFromJson();
//        archiveLastMonthTransactions();
//    }
//
//    private void calculateBalance() {
//        float income = 0.0f;
//        float expense = 0.0f;
//        try {
//            income = Float.parseFloat(incomeInput.getText().toString());
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            expense = Float.parseFloat(expenseInput.getText().toString());
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//        balance += income - expense;
//
//        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        String incomeStr = String.valueOf(income);
//        String expenseName = expenseNameInput.getText().toString();
//        String expenseStr = String.valueOf(expense);
//
//        JSONObject transaction = new JSONObject();
//        try {
//            transaction.put("date", date);
//            transaction.put("income", incomeStr);
//            transaction.put("expenseName", expenseName);
//            transaction.put("expense", expenseStr);
//            saveToJson(transaction);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        addRowToTable(date, incomeStr, expenseName, expenseStr);
//
//        balanceTextView.setText(String.valueOf(balance));
//        balanceButton.setText("Баланс: " + balance);
//
//        incomeInput.setText("");
//        expenseNameInput.setText("");
//        expenseInput.setText("");
//    }
//
//
//    private void saveToJson(JSONObject transaction) {
//        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
//        File file = new File(getFilesDir(), FILENAME);
//        JSONObject mainObject = new JSONObject();
//
//        try {
//            if (file.exists()) {
//                Scanner scanner = new Scanner(file);
//                StringBuilder builder = new StringBuilder();
//                while (scanner.hasNextLine()) {
//                    builder.append(scanner.nextLine());
//                }
//                mainObject = new JSONObject(builder.toString());
//                scanner.close();
//            }
//
//            JSONArray monthTransactions;
//            if (mainObject.has(month)) {
//                monthTransactions = mainObject.getJSONArray(month);
//            } else {
//                monthTransactions = new JSONArray();
//            }
//            monthTransactions.put(transaction);
//
//            mainObject.put(month, monthTransactions);
//
//            FileWriter writer = new FileWriter(file);
//            writer.write(mainObject.toString());
//            writer.close();
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void loadFromJson() {
//        String month = new SimpleDateFormat("yyyy-MM").format(new Date());
//        File file = new File(getFilesDir(), FILENAME);
//
//        if (file.exists()) {
//            try {
//                Scanner scanner = new Scanner(file);
//                StringBuilder builder = new StringBuilder();
//                while (scanner.hasNextLine()) {
//                    builder.append(scanner.nextLine());
//                }
//                scanner.close();
//
//                JSONObject mainObject = new JSONObject(builder.toString());
//
//                Iterator<String> keys = mainObject.keys();
//                while (keys.hasNext()) {
//                    String key = keys.next();
//                    JSONObject transaction = mainObject.getJSONObject(key);
//                    addRowToTable(transaction.getString("date"), transaction.getString("income"), transaction.getString("expenseName"), transaction.getString("expense"));
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void addRowToTable(String date, String income, String expenseName, String expense) {
//        TableRow row = new TableRow(this);
//        row.addView(createTextView(date));
//        row.addView(createTextView(income));
//        row.addView(createTextView(expenseName));
//        row.addView(createTextView(expense));
//        tableLayout.addView(row);
//    }
//
//    private TextView createTextView(String text) {
//        TextView textView = new TextView(this);
//        textView.setText(text);
//        return textView;
//    }
//
//    private void archiveLastMonthTransactions() {
//        String lastMonth = new SimpleDateFormat("yyyy-MM").format(new Date(System.currentTimeMillis() - 2629800000L));
//        File file = new File(getFilesDir(), FILENAME);
//        JSONObject mainObject = new JSONObject();
//
//        if (file.exists()) {
//            try {
//                Scanner scanner = new Scanner(file);
//                StringBuilder builder = new StringBuilder();
//                while (scanner.hasNextLine()) {
//                    builder.append(scanner.nextLine());
//                }
//                mainObject = new JSONObject(builder.toString());
//                scanner.close();
//
//                if (mainObject.has(lastMonth)) {
//                    JSONArray lastMonthTransactions = mainObject.getJSONArray(lastMonth);
//
//                    File archiveFile = new File(getFilesDir(), String.format(FILENAME_ARCHIVE, lastMonth));
//                    FileWriter writer = new FileWriter(archiveFile);
//                    writer.write(lastMonthTransactions.toString());
//                    writer.close();
//
//                    // remove last month transactions from the main file
//                    mainObject.remove(lastMonth);
//                    FileWriter mainFileWriter = new FileWriter(file);
//                    mainFileWriter.write(mainObject.toString());
//                    mainFileWriter.close();
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
}
