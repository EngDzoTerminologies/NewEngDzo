package ddc.dict;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelperWordOfTheDay extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAMES = "eng_dictionary.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    Cursor cursor = null;


    public DatabaseHelperWordOfTheDay(Context context) {
        super(context, DB_NAMES, null, 4);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);
        }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {

            this.getReadableDatabase();
            try {
                myContext.deleteDatabase(DB_NAMES);
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAMES;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e)
        {
            //
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }


    private void copyDataBase() throws IOException {

        InputStream myInput = myContext.getAssets().open(DB_NAMES);
        String outFileName = DB_PATH + DB_NAMES;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        Log.i("copyDataBase", "Database copied");


    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAMES;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }


    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {
            this.getReadableDatabase();
            myContext.deleteDatabase(DB_NAMES);
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Cursor getWord()
    {

        cursor= myDataBase.rawQuery("SELECT eng_word FROM wordOfTDay",null);
        return cursor;
    }


}
