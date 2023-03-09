package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recView;
    TextView emptyTxt;
    TodoAdapter todoAdapter;
    List<TodoModel> todoData;
    FloatingActionButton addActBtn;
    FloatingActionButton delActBtn;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addActBtn = findViewById(R.id.addActBtn);
        delActBtn = findViewById(R.id.delActBtn);

        emptyTxt = findViewById(R.id.emptyTxt);
        emptyTxt.setVisibility(View.INVISIBLE);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        todoData = databaseHelper.getData();

        if (todoData.size() == 0) {
            emptyTxt.setVisibility(View.VISIBLE);
        }

        recView = findViewById(R.id.recView);
        recView.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(MainActivity.this, todoData);
        recView.setAdapter(todoAdapter);

        addActBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddTodo.class);
                startActivity(i);
            }
        });

        delActBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Clear Todos");
            builder.setMessage("Do you want to delete all todos? This action cannot be undone.");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    databaseHelper.clearTable();
                    recreate();
                }
            });
            builder.show();

        });

    }

}