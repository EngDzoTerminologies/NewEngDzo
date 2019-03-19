package com.riad.dictionary;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

public class LoadDatabaseAsyncWordDay extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private AlertDialog alertDialog;
    private DatabaseHelperWordOfTheDay myDbHelper;

    public LoadDatabaseAsyncWordDay(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();


        AlertDialog.Builder d = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_diolog_database_copying, null);
        d.setTitle("Loading Database...");
        d.setView(dialogView);
        alertDialog = d.create();

        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    protected Boolean doInBackground(Void... params) {

        myDbHelper = new DatabaseHelperWordOfTheDay(context);

        try {
            myDbHelper.createDataBase();

        } catch (IOException e) {

            throw new Error ("Database was not created");
        }
        myDbHelper.close();
        return null;
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        alertDialog.dismiss();
        MainActivity.openDatabase();
        // PoliticalActivity.openDatabase();
    }


}
