package ddc.dict;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.riad.dictionary.R;

import java.util.ArrayList;

public class BookmarkActivity extends AppCompatActivity {

   // SearchView search;

    static DatabaseHelper myDbHelper;
    static DatabaseHelperEconomics myDbHelperEconomics;
    static boolean databaseOpened=false;

    SimpleCursorAdapter suggestionAdapter;

    ArrayList<Bookmark> bookmarkList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter bookmarkAdapter;
   // ListView list;

    RelativeLayout emptyBookmark;
    Cursor cursorBookmark;
    Cursor cursorBookmarkEconimics;
    TextView clearall;
    TextView titleBookmark;

    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        emptyBookmark=findViewById(R.id.empty_bookmark);
       // list = findViewById(R.id.recycler_bookmark);// for listview

        recyclerView = findViewById(R.id.recycler_bookmark);
        clearall= findViewById(R.id.clear_bookmark);
        titleBookmark=findViewById(R.id.titlebookmark);

        layoutManager = new LinearLayoutManager(BookmarkActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        layoutManager=new LinearLayoutManager(BookmarkActivity.this);

        myDbHelper = new DatabaseHelper(this);
        myDbHelperEconomics = new DatabaseHelperEconomics(this);

        // for computer database

        if (myDbHelper.checkDataBase()) {
            openDatabase();
            Log.wtf("database", "opened Main database");
        } else {
            LoadDatabaseAsyncWordDay task = new LoadDatabaseAsyncWordDay(BookmarkActivity.this);
            task.execute();
        }


        // for economics database
        if (myDbHelperEconomics.checkDataBase()) {
            openDatabase();
            Log.wtf("database", "opened Main database");
        } else {
            LoadDatabaseAsyncWordDay task = new LoadDatabaseAsyncWordDay(BookmarkActivity.this);
            task.execute();
        }


        fetch_bookmark();


        clearall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewIn) {
                myDbHelper = new DatabaseHelper(BookmarkActivity.this);
                try {
                    myDbHelper.openDataBase();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                showAlertDialog();
            }
        });

    }

    private void fetch_bookmark() {

        bookmarkList=new ArrayList<>();
        bookmarkAdapter = new RecyclerViewAdapterBookmark(this,bookmarkList);
        recyclerView.setAdapter(bookmarkAdapter);

        //Bookmark h;
        Bookmark h;
       // Log.e("book1", "hello");


       // ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.bookmark_layout, bookmarkList);
        //list.setAdapter(adapter);
        if(databaseOpened)
        {
            cursorBookmark = myDbHelper.getBookmark();
            //Log.e("book", String.valueOf(cursorBookmark));
           /* if (cursorBookmark.moveToFirst()) {
                Log.e("book1", String.valueOf(cursorBookmark.getString(cursorBookmark.getColumnIndex("value_dzo"))));
                }*/
            if (cursorBookmark.moveToFirst()) {
                do {
                    h= new Bookmark(cursorBookmark.getString(cursorBookmark.getColumnIndex("value_en")),
                            cursorBookmark.getString(cursorBookmark.getColumnIndex("value_dzo")),
                            cursorBookmark.getString(cursorBookmark.getColumnIndex("category")));

                    bookmarkList.add(h);
                }
                while (cursorBookmark.moveToNext());
            }


            bookmarkAdapter.notifyDataSetChanged();
//            adapter.notifyDataSetChanged();
        }

        if (bookmarkList.size() == 0)
        {
            emptyBookmark.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyBookmark.setVisibility(View.GONE);
        }


        if(bookmarkList.size() == 0 )
        {
            clearall.setVisibility(View.INVISIBLE);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        fetch_bookmark();
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

    private void showAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(BookmarkActivity.this, R.style.MyDialogTheme);
        builder.setTitle("Are you sure?");
        builder.setMessage("All the bookmark will be deleted");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myDbHelper.deletesBookmark();
                        for(int i = 0; i < bookmarkList.size(); i++)
                        {
                            bookmarkList.remove(i);
                            bookmarkAdapter. notifyItemRemoved(i);
                            bookmarkAdapter.notifyItemRangeChanged(i, bookmarkList.size());
                        }


                        if(bookmarkList.size()!=0){
                            bookmarkList.remove(0);
                            bookmarkAdapter. notifyItemRemoved(0);
                            bookmarkAdapter.notifyItemRangeChanged(0, bookmarkList.size());
                        }

                        if(bookmarkList.size() == 0 )
                        {
                            clearall.setVisibility(View.INVISIBLE);

                        }

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

  /*  @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }*/
}
