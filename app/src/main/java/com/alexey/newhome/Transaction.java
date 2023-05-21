package com.alexey.newhome;

public class Transaction {
    private int id;
    private String date;
    private float income;
    private String expenseName;
    private float expense;

    public Transaction(int id, String date, float income, String expenseName, float expense) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.expenseName = expenseName;
        this.expense = expense;
    }

    public String getDate() {
        return date;
    }

    public float getIncome() {
        return income;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public float getExpense() {
        return expense;
    }
}
