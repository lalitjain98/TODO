package com.example.lalit.todo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jainl on 17-09-2017.
 */

public class CustomAdapter extends ArrayAdapter{
    Context mContext;
    ArrayList<Todo> mTodoArrayList;
    DeleteButtonClickListener mDeleteButtonClickListener;
    public CustomAdapter(@NonNull Context context,ArrayList<Todo> todoArrayList, DeleteButtonClickListener deleteButtonClickListener) {
        super(context, 0);
        mContext = context;
        mTodoArrayList = todoArrayList;
        mDeleteButtonClickListener = deleteButtonClickListener;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.todo_list_row_layout,null);
            ViewHolder viewHolder = new ViewHolder();

            TextView titleView = convertView.findViewById(R.id.title);
            TextView dateCreatedView = convertView.findViewById(R.id.dateCreated);

            Todo todo = mTodoArrayList.get(position);
            titleView.setText(todo.getTitle());
            dateCreatedView.setText(todo.getDateCreated());
            viewHolder.title =titleView;
            viewHolder.dateCreated = dateCreatedView;
            convertView.setTag(viewHolder);
            Button deleteButton = convertView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteButtonClickListener.onDeleteClickListener(position,view);
                }
            });
        }
        ViewHolder holder = ((ViewHolder) convertView.getTag());
        Todo todo = mTodoArrayList.get(position);
        holder.title.setText(todo.getTitle());
        holder.dateCreated.setText(todo.getDateCreated());
        return convertView;
    }

    @Override
    public int getCount() {
        return mTodoArrayList.size();
    }

    public class ViewHolder{
        TextView title,dateCreated;
    }
    interface DeleteButtonClickListener{
        void onDeleteClickListener(int position,View view);
    }
}
