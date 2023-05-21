package com.alexey.newhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, income, expenseName, expense;

        public MyViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_text_view);
            income = itemView.findViewById(R.id.income_text_view);
            expenseName = itemView.findViewById(R.id.expense_name_text_view);
            expense = itemView.findViewById(R.id.expense_text_view);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.date.setText(transaction.getDate());
        holder.income.setText(String.valueOf(transaction.getIncome()));
        holder.expenseName.setText(transaction.getExpenseName());
        holder.expense.setText(String.valueOf(transaction.getExpense()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void updateTransactions(List<Transaction> newTransactions) {
        transactions = newTransactions;
        notifyDataSetChanged();
    }
}
