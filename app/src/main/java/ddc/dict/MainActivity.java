package ddc.dict;


import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private long back_pressed =0;
    public static boolean isQuit=false;

    static DatabaseHelperWordOfTheDay myDbHelper;
    static DatabaseHelper Dbhelper;
    static boolean databaseOpened=false;

    //for the word of the day
    public TextView wod;
    Cursor cursor = null;
    String words;
    String currentDate;
    String dateTime;
    String temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewID);
        wod = findViewById(R.id.wordOfTheDayID);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        ListItem item1 = new ListItem("གློག་རིག", "Computer");
        ListItem item5 = new ListItem("དཔལ་འབྱོར།", "Economics");
        ListItem item2 = new ListItem("དངུལ་རྩིས།", "Financial");
        ListItem item4 = new ListItem("ལུས་ཁམས།", "Health");
        ListItem item3 = new ListItem("སྲིད་དོན", "Political");



        listItems.add(item1);
        listItems.add(item5);
        listItems.add(item2);
        listItems.add(item4);
        listItems.add(item3);



        adapter = new MyAdapter(MainActivity.this, listItems);
        recyclerView.setAdapter(adapter);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //fetching data from database
        myDbHelper = new DatabaseHelperWordOfTheDay(this);
        if (myDbHelper.checkDataBase()) {
            openDatabase();
            Log.wtf("database", "opened Main database");
        } else {
            LoadDatabaseAsyncWordDay task = new LoadDatabaseAsyncWordDay(MainActivity.this);
            task.execute();
        }

        //Storing data in the ArrayList
        ArrayList<String> wordOfTheDay = new ArrayList<>();
        try {
            cursor = myDbHelper.getWord();
            while (cursor.moveToNext()) {
                words = cursor.getString(cursor.getColumnIndex("eng_word"));
                wordOfTheDay.add(words);

            }
        //generating word of the day
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate= df.format(c.getTime());
        dateTime = df.format(c.getTime());
        // to store new date into shared preference
        temp = dateTime;
        loadDate();


        Log.wtf("currentDate", currentDate);
        Log.wtf("loaddate",dateTime);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!(dateTime.equals(currentDate)))
        {
            Random ran = new Random();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int num = (ran.nextInt(700));
            editor.putString("word", wordOfTheDay.get(num));
            editor.apply();
            saveDate();

        }

        String wordoftheday = sharedPreferences.getString("word", "");
        wod.setText(wordoftheday);
        }


        catch (Exception e) {
           wod.setText("Thank you for installing the app, word of the day will be displayed soon");
        }


    }

    private void loadDate() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        dateTime = sharedPreferences.getString("date","");
    }

    private void saveDate() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("date", temp);
        editor.apply();
    }


    protected static void openDatabase()
    {
        try {
            myDbHelper.openDataBase();
            databaseOpened=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 500 > System.currentTimeMillis()) {
                finishAffinity();
            } else {
                Toast.makeText(getBaseContext(), "Press once again to exit",
                        Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    }


    //For the items in navigation drawer
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.bookmark) {

            Intent intent=new Intent(MainActivity.this, BookmarkActivity.class);
            startActivity(intent);

        } else if (id == R.id.help) {

            Intent intent=new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);

        } else if (id == R.id.share) {

            Intent share=new Intent(Intent.ACTION_SEND);
            share.setType("text/*");
            share.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details:id=ddc.dict");
            startActivity(Intent.createChooser(share,"སྤེལ། / Share Using"));
            return true;
        }
        else if (id == R.id.rate) {

            //rateMe();
            final String packagename= getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?=" + packagename)));

        }
        else if (id == R.id.feedback) {
            //  Dialog dialog=new Dialog();
            Intent intent=new Intent(getApplicationContext(), FeedbackActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.about) {
          //  Dialog dialog=new Dialog();
            Intent intent=new Intent(getApplicationContext(), PopupActivity.class);
            startActivity(intent);

        }

        else if (id == R.id.exit) {

            showAlertDialog();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Are you sure?");
       // builder.setMessage("All the bookmark will be deleted");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity.isQuit=true;
                        System.exit(1);

                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }
/*
    //For rating an app
    private void rateMe() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.android.chrome")));


        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }*/
}
