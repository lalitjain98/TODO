package com.example.lalit.todo;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by jainl on 09-10-2017.
 */

public class DelayAsyncTask extends AsyncTask<Void,Void,Void>{
    OnSleepListener onSleepListener;
    public DelayAsyncTask(OnSleepListener listener){
        onSleepListener = listener;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Log.d("Exception","Thrown");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        onSleepListener.onSleep();
    }

    public interface OnSleepListener{
        public void onSleep();
    }
}
