package com.alexey.newhome;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "finance.db";
    private static final String TABLE_NAME = "transaction_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "DATE";
    private static final String COL_3 = "INCOME";
    private static final String COL_4 = "EXPENSE_NAME";
    private static final String COL_5 = "EXPENSE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,INCOME REAL,EXPENSE_NAME TEXT,EXPENSE REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String date, float income, String expenseName, float expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, income);
        contentValues.put(COL_4, expenseName);
        contentValues.put(COL_5, expense);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public Cursor getTransactionHistory(int year, int month, int dayOfMonth) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE DATE = ?";
        String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, dayOfMonth);
        return db.rawQuery(query, new String[]{dateString});
    }

    public Cursor getTransactionHistoryByMonth(int year, int month) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DATE, SUM(INCOME) AS INCOME, SUM(EXPENSE) AS EXPENSE FROM " + TABLE_NAME +
                " WHERE strftime('%Y', DATE) = ? AND strftime('%m', DATE) = ?" +
                " GROUP BY strftime('%Y-%m', DATE)";
        String yearString = String.valueOf(year);
        String monthString = String.format(Locale.getDefault(), "%02d", month);
        return db.rawQuery(query, new String[]{yearString, monthString});
    }

    public Cursor getTransactionHistoryByYear(int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT DATE, SUM(INCOME) AS INCOME, SUM(EXPENSE) AS EXPENSE FROM " + TABLE_NAME +
                " WHERE strftime('%Y', DATE) = ?" +
                " GROUP BY strftime('%Y', DATE)";
        String yearString = String.valueOf(year);
        return db.rawQuery(query, new String[]{yearString});
    }

}



