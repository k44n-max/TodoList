package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditTodo extends AppCompatActivity {
    EditText taskTxt;
    CheckBox completedBox;
    ImageButton saveBtn;
    ImageButton deleteBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_todo_layout);

        taskTxt = findViewById(R.id.editTskTxt);
        completedBox = findViewById(R.id.chkBox2);
        saveBtn = findViewById(R.id.saveEditBtn);
        deleteBtn = findViewById(R.id.delBtn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int todo_id = getIntent().getIntExtra("todo_id",-1);
        DatabaseHelper databaseHelper = new DatabaseHelper(EditTodo.this);
        TodoModel todoModel = databaseHelper.getTodoById(todo_id);

        //Log.i("DEV", "Got todo: " + todoModel.toString());
        taskTxt.setText(todoModel.getTask());
        completedBox.setChecked(todoModel.isCompleted());

        deleteBtn.setOnClickListener(v -> {
            databaseHelper.deleteTodo(todoModel);
            databaseHelper.close();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        saveBtn.setOnClickListener(v -> {
            String text = taskTxt.getText().toString().trim();
            if (text.length() != 0) {
                databaseHelper.editTodo(todoModel.getId(), text, completedBox.isChecked());
                databaseHelper.close();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else {
                taskTxt.setError("This field cannot be empty");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
