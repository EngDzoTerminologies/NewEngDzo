package com.riad.dictionary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class PoliticalActivity extends AppCompatActivity {

    SearchView search;

    static DatabaseHelperPolitical myDbHelper;
    static boolean databaseOpened=false;
    SimpleCursorAdapter suggestionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_political);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PoliticalActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //back arrow button
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search =  (SearchView) findViewById(R.id.search_view);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                search.setIconified(false);
            }
        });


        myDbHelper = new DatabaseHelperPolitical(this);

        if(myDbHelper.checkDataBase())
        {
            openDatabase();

        }
        else
        {
            LoadDatabaseAsyncPolitical task = new LoadDatabaseAsyncPolitical(PoliticalActivity.this);
            task.execute();
        }




        // setup SimpleCursorAdapter

        final String[] from = new String[] {"en_word"};
        final int[] to = new int[] {R.id.suggestion_text};

        suggestionAdapter = new SimpleCursorAdapter(PoliticalActivity.this,
                R.layout.suggestion_row, null, from, to, 0){
            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);
            }

        };

        search.setSuggestionsAdapter(suggestionAdapter);

        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {

                // Add clicked text to search box
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word =  cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word,false);

                //search.setQuery("",false);

                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(PoliticalActivity.this, WordMeaningActivityPolitical.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word",clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                // Your code here
                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                String text =  search.getQuery().toString();

                Cursor c = myDbHelper.getMeaning(text);


                if(c.getCount()==0)
                {
                    search.setQuery("",false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(PoliticalActivity.this, R.style.MyDialogTheme);
                    builder.setTitle("Word Not Found");
                    builder.setMessage("Please search again");

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // positive button logic
                                }
                            });

                    String negativeText = getString(android.R.string.cancel);
                    builder.setNegativeButton(negativeText,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    search.clearFocus();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    // display dialog
                    dialog.show();
                }

                else
                {
                    //search.setQuery("",false);
                    search.clearFocus();
                    search.setFocusable(false);

                    Intent intent = new Intent(PoliticalActivity.this, WordMeaningActivityPolitical.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",text);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                return false;
            }


            @Override
            public boolean onQueryTextChange(final String s) {

                search.setIconifiedByDefault(false); //Give Suggestion list margins
                Cursor cursorSuggestion=myDbHelper.getSuggestions(s);
                suggestionAdapter.changeCursor(cursorSuggestion);

                return false;
            }

        });





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


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(ComputerActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_exit) {
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
