package ddc.dict;

import android.content.Context;
import android.content.SharedPreferences;
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

public class DatabaseHelper extends SQLiteOpenHelper {

    private String DB_PATH = null;
    private static String DB_NAME = "eng_dictionary.db";
   // private static String DBPOLITICAL_NAME="eng_dictionary.db";
    private SQLiteDatabase myDataBase;
    private Context myContext;
   /* private String DATABASE_LOCATION="";
    private String DATABASE_FULL_PATH="";
    private final String COL_KEY="key";
    private final String COL_VALUE="value_en";
    private final String TBL_ENG_DZO="eng_dzo";
    private final String TBL_DZO_DZO="dzo_dzo";
*/
    ComputerActivity com = new ComputerActivity();


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.e("Path 1", DB_PATH);

       /* if(!isExistingDB()){

            try {
                //create a directory before copy database

                File dbLocation= new File (DATABASE_LOCATION);
                dbLocation.mkdirs();
                extractAssetDatabaseDirectory(DB_NAME);

            }
            catch (IOException e){
                e.printStackTrace();

            }

        }
        myDataBase = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULL_PATH,null);*/

        }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {

            this.getReadableDatabase();
            try {
                myContext.deleteDatabase(DB_NAME);
                copyDataBase();

            } catch (IOException e) {
                throw new Error("Error copying database");
            }

        }

    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
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

        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
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
        String myPath = DB_PATH + DB_NAME;
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
            myContext.deleteDatabase(DB_NAME);
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public Cursor getMeaning(String text)
    {
        Cursor c= myDataBase.rawQuery("SELECT _id,en_word,acronym,speech,dzo_definition,dzo_word,images,category FROM words WHERE en_word == ('"+text+"') OR dzo_word == ('"+text+"')",null);
        return c;
    }

    public Cursor getSuggestions(String text)
    {

       SharedPreferences sharedPreferences = myContext.getSharedPreferences("computerpreference", 0);
        if( sharedPreferences.getInt("computerSwitchValue", 1) == 1)
        {
            Cursor c = myDataBase.rawQuery("SELECT _id,en_word FROM words WHERE en_word LIKE '" + text + "%' LIMIT 40", null);
            return c;
        }
        else
        {
            Cursor c = myDataBase.rawQuery("SELECT _id,dzo_word FROM words WHERE dzo_word LIKE '" + text + "%' LIMIT 40", null);
            return c;
        }



    }


    public void  insertHistory(String text)
    {
        myDataBase.execSQL("INSERT INTO history(word) VALUES(UPPER('"+text+"'))");

    }

//Bookmark
public void  insertBookmark(String english, String dzongkha, String category)
{
    myDataBase.execSQL("INSERT INTO bookmark(value_en,value_dzo, category) VALUES('"+english+"','"+dzongkha+"','"+category+"')");
}

    public Cursor getBookmark()
    {
//        Cursor c= myDataBase.rawQuery("select value_en from bookmark",null);
        Cursor c= myDataBase.rawQuery("select distinct value_en,value_dzo,category from bookmark order by _id desc",null);
        return c;
    }

    public void  deletesBookmark()
    {
        myDataBase.execSQL("DELETE FROM bookmark");
    }

    public void deleteSingleBookmark(String text)
    {
        myDataBase.execSQL("DELETE FROM bookmark WHERE value_en==('"+text+"')");
    }
    public void  deleteBookmark(String text)
    {
        myDataBase.execSQL("DELETE  FROM bookmark WHERE value_en==(UPPER('"+text+"'))");
    }




    /*
  *//*  boolean isExistingDB(){
        File file=new File (DATABASE_FULL_PATH);
        return file.exists();

    }
    public void extractAssetDatabaseDirectory(String flleName) throws IOException{
        int length;
        InputStream sourceDatabase=this.myContext.getAssets().open(flleName);
        File destinationPath = new File(DATABASE_FULL_PATH);
        OutputStream destination= new FileOutputStream(destinationPath);

        byte[] buffer= new byte[4096];
        while((length= sourceDatabase.read(buffer))>0){
            destination.write(buffer,0,length);
        }

        sourceDatabase.close();
        destination.flush();
        destination.close();
    }*//*


    public ArrayList<String> getWord(int dicType){

        String tableName=getTableName(dicType);
        String q="SELECT * FROM "+ tableName;
        Cursor result=myDataBase.rawQuery(q,null);

        ArrayList<String> source= new ArrayList<>();
        while(result.moveToNext()){
            source.add(result.getString(result.getColumnIndex(COL_KEY)));


        }
        return source;

    }
    public Word getWord(String key, String dicType){

        Cursor tableName=getMeaning(dicType);
        String q="SELECT * FROM "+tableName+" WHERE upper([key])=upper(?)";
        Cursor result=myDataBase.rawQuery(q,new String[]{key});

        Word word= new Word();
        while(result.moveToNext()){
            word.key = result.getString(result.getColumnIndex(COL_KEY));
            word.value_en= result.getString(result.getColumnIndex(COL_VALUE));

        }
        return null;
    }

    public void addBookmark(String text){
        try{
          //  String q ="INSERT INTO bookmark ([key],[value_en]) VALUES(?,?);";
            myDataBase.execSQL("INSERT INTO bookmark(word) VALUES(UPPER('"+text+"'))");
            //myDataBase.execSQL(q,new Object[]{word});
           // myDataBase.execSQL(q,new Object[]{word.key,word.value_en});
        }catch(SQLException ex){

        }
    }


    public void removeBookmark(Word word){
        try{
            String q ="DELETE FROM bookmark WHERE upper(["+COL_KEY+"])=upper(?) AND ["+COL_VALUE+"]=?;";
            myDataBase.execSQL(q,new Object[]{word.key,word.value_en});
        }catch(SQLException ex){

        }
    }

    public void removeBookmark(String key){
        try{
            String q ="DELETE FROM bookmark WHERE upper(["+COL_KEY+"])=upper(?) ;";
          myDataBase.execSQL(q,new Object[]{key});
        }catch(SQLException ex){

        }
    }


    public ArrayList<String> getAllWordFromBookmark(){

        String q=" SELECT * FROM bookmark ORDER BY [date] DESC;";
        Cursor result=myDataBase.rawQuery(q,null);

        ArrayList<String> source= new ArrayList<>();
        Word word= new Word();
        while(result.moveToNext()){
            source.add(word.key = result.getString(result.getColumnIndex(COL_KEY)));


        }
        return source;
    }

    public boolean isWordMark(Word word){


        String q="SELECT * FROM bookmark WHERE upper([key])=upper(?) AND [value]=?";
        Cursor result=myDataBase.rawQuery(q,new String[]{word.key,word.value_en});

        return result.getCount() > 0;

    }

    public Word getWordFromBookmark(String key){

        String q="SELECT * FROM bookmark WHERE upper([key])=upper(?)";
        Cursor result=myDataBase.rawQuery(q,new String[]{key});

        Word word=null;
        while(result.moveToNext()){
            word=new Word();
            word.key= result.getString(result.getColumnIndex(COL_KEY));
            word.value_en= result.getString(result.getColumnIndex(COL_VALUE));

        }
        return word;
    }


    public String getTableName(int dicType){

        String tableName="";
        return tableName;

    }

    public void clearBookmark() {
        try{
            String q ="DELETE FROM bookmark;";
            myDataBase.execSQL(q);
        }catch(SQLException ex){

        }

    }*/

}
