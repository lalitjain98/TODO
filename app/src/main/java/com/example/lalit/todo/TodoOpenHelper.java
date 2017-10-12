package com.example.lalit.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jainl on 17-09-2017.
 */

public class TodoOpenHelper extends SQLiteOpenHelper{
    static TodoOpenHelper instance;

    public static TodoOpenHelper getInstance(Context context) {
        if(instance == null){
            instance = new TodoOpenHelper(context);
        }
        return instance;
    }

    private TodoOpenHelper(Context context) {
        super(context, "todo_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = String.format("CREATE TABLE %S ( %S INTEGER PRIMARY KEY AUTOINCREMENT , %s VARCHAR , %s INTEGER , %s TEXT, %s INTEGER )", Contracts.TODO_TABLE_NAME,Contracts.TODO_ID,Contracts.TODO_TITLE,Contracts.TODO_DATE_CREATED,Contracts.TODO_DESCRIPTION, Contracts.TODO_DATE_ALARM);
        Log.d("create table query",query);
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
