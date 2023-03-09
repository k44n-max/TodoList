package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddTodo extends AppCompatActivity {
    EditText addTaskTxt;
    CheckBox completedChk;
    ImageButton addTaskBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_todo_layout);
        addTaskTxt = findViewById(R.id.addTskTxt);
        completedChk = findViewById(R.id.chkBox1);
        addTaskBtn = findViewById(R.id.addTskBtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addTaskBtn.setOnClickListener(v -> {
            if (addTaskTxt.getText().toString().trim().length() != 0) {
                TodoModel todoModel = new TodoModel(-1, addTaskTxt.getText().toString().trim(), completedChk.isChecked());
                DatabaseHelper databaseHelper = new DatabaseHelper(AddTodo.this);
                databaseHelper.addTodo(todoModel);
                databaseHelper.close();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else {
                //Toast.makeText(getApplicationContext(), "Input is empty!", Toast.LENGTH_SHORT);
                addTaskTxt.setError("This field cannot be empty");
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
