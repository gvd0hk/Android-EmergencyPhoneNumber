package com.promlert.emergencyphonenumber;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.promlert.emergencyphonenumber.db.PhoneDb;
import com.promlert.emergencyphonenumber.model.Category;

import java.util.ArrayList;

/**
 * Created by Promlert on 2017-04-13.
 */

public class App extends Application {

    private static App sInstance;
    private ArrayList<Category> mPhoneData = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static App getInstance() {
        return sInstance;
    }

    public ArrayList<Category> getPhoneData() {
        return mPhoneData;
    }

    public void loadPhoneData(final LoadPhoneDataCallback callback) {
        new LoadPhoneDataTask(this, new LoadPhoneDataCallback() {
            @Override
            public void onLoadFinish(ArrayList<Category> categoryList) {
                mPhoneData = categoryList;
                callback.onLoadFinish(categoryList);
            }
        }).execute();
    }

    public interface LoadPhoneDataCallback {
        void onLoadFinish(ArrayList<Category> categoryList);
    }

    private static class LoadPhoneDataTask extends AsyncTask<Void, Void, ArrayList<Category>> {

        private Context context;
        private LoadPhoneDataCallback callback;

        public LoadPhoneDataTask(Context context, LoadPhoneDataCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected ArrayList<Category> doInBackground(Void... params) {
/*
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
*/
            return new PhoneDb(context).getAllData();
        }

        @Override
        protected void onPostExecute(ArrayList<Category> categoryList) {
            super.onPostExecute(categoryList);
            callback.onLoadFinish(categoryList);
        }
    }
}
