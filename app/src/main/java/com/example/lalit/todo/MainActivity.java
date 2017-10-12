package com.example.lalit.todo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.MailTo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Todo> todoarrayList;
    CustomAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        todoarrayList = new ArrayList<>();
        changeData();//add all rows
        adapter = new CustomAdapter(this, todoarrayList, new CustomAdapter.DeleteButtonClickListener() {
            @Override
            public void onDeleteClickListener(final int position, View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Item");
                builder.setMessage("Are You Sure to Delete this item ?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Todo itemForDeletion = todoarrayList.get(position);
                        int itemId = itemForDeletion.getId();
                        TodoOpenHelper todoOpenhelper = TodoOpenHelper.getInstance(MainActivity.this);
                        SQLiteDatabase todoDB = todoOpenhelper.getWritableDatabase();
                        todoDB.delete(Contracts.TODO_TABLE_NAME, Contracts.TODO_ID + " = ?", new String[]{Integer.toString(itemId)});
                        changeData();
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_DETAIL_MODE, Constants.KEY_EDIT);
                intent.putExtra(Constants.KEY_ID, todoarrayList.get(i).getId());
                startActivityForResult(intent, Constants.KEY_EDIT);
            }
        });
        /*listView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Item");
                builder.setMessage("Are You Sure to Delete this item ?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });*/
        //Log.d("Data :: ",todoarrayList.toString());
    }

    private void changeData() {
        todoarrayList.clear();
        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(this);
        SQLiteDatabase todoDB = todoOpenHelper.getReadableDatabase();
        Cursor cursor = todoDB.query(Contracts.TODO_TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contracts.TODO_ID));
            String title = cursor.getString(cursor.getColumnIndex(Contracts.TODO_TITLE));
            String dateCreated = DateFormat.format("dd/MM/yyyy", new Date(cursor.getLong(cursor.getColumnIndex(Contracts.TODO_DATE_CREATED)))).toString();
            String desc = cursor.getString(cursor.getColumnIndex(Contracts.TODO_DESCRIPTION));
            long alarmDate = cursor.getInt(cursor.getColumnIndex(Contracts.TODO_DATE_ALARM));
            Log.d("Databade item "+ id , alarmDate + "");
            Todo item = new Todo(id, title, dateCreated, desc, alarmDate);
            todoarrayList.add(item);
        }
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addTodo) {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(Constants.KEY_DETAIL_MODE, Constants.KEY_ADD);
            startActivityForResult(intent, Constants.KEY_ADD);
        }
        else if( id == R.id.feedback){
            Intent feedback = new Intent(Intent.ACTION_SENDTO);
            feedback.setData(Uri.parse("mailto:jain.lalit90@yahoo.com"));
            feedback.putExtra(Intent.EXTRA_SUBJECT,"TODO App Feedback");
            startActivity(feedback);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.KEY_ADD && resultCode == 4) {
            changeData();
            adapter.notifyDataSetChanged();
        } else if (requestCode == Constants.KEY_EDIT && resultCode == 3) {
            changeData();
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
/*
 new CustomAdapter.DeleteButtonClickListener() {
            @Override
            public void onDeleteClickListener(final int position, View view) {

                AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove Item");
                builder.setMessage("Are You Sure to Delete this item ?");
                builder.setPositiveButton("Cancel", null);
                builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        T//odo itemForDeletion = todoarrayList.get(position);
                        int itemId = itemForDeletion.getId();
                        TodoOpenHelper todoOpenhelper = TodoOpenHelper.getInstance(MainActivity.this);
                        SQLiteDatabase todoDB = todoOpenhelper.getWritableDatabase();
                        todoDB.delete(Contracts.TODO_TABLE_NAME,Contracts.TODO_ID + " = ?", new String[]{Integer.toString(itemId)});
                        changeData();
                        adapter.notifyDataSetChanged();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
                return;
            }
        }*/