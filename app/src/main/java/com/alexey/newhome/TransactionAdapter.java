package com.alexey.newhome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Настройка данных элемента в представлении ViewHolder
        Transaction transaction = transactionList.get(position);
        holder.dateTextView.setText(transaction.getDate());
        holder.incomeTextView.setText(String.valueOf(transaction.getIncome()));
        holder.expenseNameTextView.setText(transaction.getExpenseName());
        holder.expenseTextView.setText(String.valueOf(transaction.getExpense()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView, incomeTextView, expenseNameTextView, expenseTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date_text_view);
            incomeTextView = itemView.findViewById(R.id.income_text_view);
            expenseNameTextView = itemView.findViewById(R.id.expense_name_text_view);
            expenseTextView = itemView.findViewById(R.id.expense_text_view);
        }
    }
}

