package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    Context ctx;
    List<TodoModel> data;

    public TodoAdapter(Context ctx, List<TodoModel> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @NonNull
    @Override
    public TodoAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(ctx).inflate(R.layout.todo_row_layout, parent, false);
        TodoViewHolder viewHolder = new TodoViewHolder(row);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.TodoViewHolder holder, int position) {
        TodoModel todoModel = data.get(position);
        //Log.i("DEV", "Todo 1: " + todoModel.toString());

        boolean isChecked = todoModel.isCompleted();

        holder.txtTitle.setText(todoModel.getTask());
        holder.checkBox.setChecked(isChecked);

        if (isChecked) {
            holder.txtTitle.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.txtTitle.setPaintFlags(0);
        }

        holder.checkBox.setOnClickListener(v -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(ctx);
            databaseHelper.completeUpdateTodo(todoModel);
            List<TodoModel> newData = databaseHelper.getData();
            updateData(newData);
            databaseHelper.close();

            //Log.i("DEV", "Todo 2: " + todoModel.toString());
            //Log.i("DEV", "Complete Update Todo Result = " + result);
        });

        holder.row.setOnClickListener(v -> {
            Intent i = new Intent(ctx, EditTodo.class);
            i.putExtra("todo_id", todoModel.getId());
            ctx.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<TodoModel> newData) {
        data = newData;
        this.notifyDataSetChanged();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        CheckBox checkBox;
        CardView row;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            checkBox = itemView.findViewById(R.id.checkBox);
            row = itemView.findViewById(R.id.row);
        }

    }
}
