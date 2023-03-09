package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_TASK = "TASK";
    private static final String COLUMN_COMPLETE = "COMPLETE";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TASK + " TEXT, " + COLUMN_COMPLETE + " BOOL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addTodo(TodoModel todoModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK, todoModel.getTask());
        cv.put(COLUMN_COMPLETE, todoModel.isCompleted());

        long insert = db.insert(TABLE_NAME, null,cv);
        Boolean result = insert == -1 ? false : true;
        db.close();
        return result;
    }

    public List<TodoModel> getData() {
        List<TodoModel> dataList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
               int id = cursor.getInt(0);
               String task = cursor.getString(1);
               Boolean complete = cursor.getInt(2) == 1 ? true : false;

               TodoModel todoModel = new TodoModel(id, task, complete);
               dataList.add(todoModel);
           } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public boolean deleteTodo(TodoModel todoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + todoModel.getId();
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.moveToFirst();
        db.close();
        return result;
    }

    public boolean completeUpdateTodo(TodoModel todoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, todoModel.getId());
        cv.put(COLUMN_TASK, todoModel.getTask());
        cv.put(COLUMN_COMPLETE, !todoModel.isCompleted());
        String row_id = String.valueOf(todoModel.getId());
        int update = db.update(TABLE_NAME, cv, "ID=?", new String[]{row_id});
        boolean result = update == -1 ? false : true;
        return result;
    }

    public TodoModel getTodoById(int todo_id) {
        TodoModel todoModel;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + todo_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String task = cursor.getString(1);
            Boolean complete = cursor.getInt(2) == 1 ? true : false;
            todoModel = new TodoModel(id, task, complete);
            cursor.close();
            db.close();
            return todoModel;
        }
        else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public boolean editTodo(int todo_id, String newTask, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, todo_id);
        cv.put(COLUMN_TASK, newTask);
        cv.put(COLUMN_COMPLETE, completed);
        String row_id = String.valueOf(todo_id);
        int update = db.update(TABLE_NAME, cv, "ID=?", new String[]{row_id});
        boolean result = update == -1 ? false : true;
        return result;
    }

    public boolean clearTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        boolean result = cursor.moveToFirst();
        db.close();
        return result;
    }

}
