package ddc.dict;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import ddc.dict.R;

public class EconomicsActivity extends AppCompatActivity {

    SearchView search;
    private Switch searchLanguage;
    private TextView textSelected;
    private TextView bookdescription;

    static DatabaseHelperEconomics myDbHelper;
    static boolean databaseOpened=false;
    SimpleCursorAdapter suggestionAdapter;


    public static final String pref_name = "economicspreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_economics);

        final SharedPreferences sharedPreferences;
        sharedPreferences = getApplicationContext().getSharedPreferences(pref_name, 0);
        searchLanguage = findViewById(R.id.switchLanguageID);
        textSelected = findViewById(R.id.language);
        search = findViewById(R.id.search_view);
        bookdescription = findViewById(R.id.somedescriptionE);

        sharedPreferences.getInt("economicsSwitchValue", 1);
        update(sharedPreferences);

        if (sharedPreferences.getInt("economicsSwitchValue", 1) == 1) {
            searchLanguage.setChecked(false);
            bookdescription.setText(R.string.economics_description);
        } else {
            searchLanguage.setChecked(true);
            bookdescription.setText(R.string.economics_descriptionDzo);
            search.setQueryHint("འཚོལ...");

        }

        searchLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //sharedPreferences = getApplicationContext().getSharedPreferences(pref_name, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (isChecked) {
                    editor.putInt("economicsSwitchValue", 0);
                    editor.apply();
                    textSelected.setText("Dzongkha");
                    bookdescription.setText(R.string.economics_descriptionDzo);
                    update(sharedPreferences);
                    search.setQueryHint("འཚོལ...");

                } else {
                    editor.putInt("economicsSwitchValue", 1);
                    editor.apply();
                    update(sharedPreferences);
                    bookdescription.setText(R.string.economics_description);
                    search.setQueryHint("search...");

                }
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fabs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EconomicsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //back arrow button
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search = (SearchView) findViewById(R.id.search_view);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);
            }
        });


        myDbHelper = new DatabaseHelperEconomics(this);

        if (myDbHelper.checkDataBase()) {
            openDatabase();

        } else {
            LoadDatabaseAsyncEconomics task = new LoadDatabaseAsyncEconomics(EconomicsActivity.this);
            task.execute();
        }


    }

    public void update( final SharedPreferences sharedPreferences)
    {
        String[] from;
        int[] to;


        //sharedPreferences = getApplicationContext().getSharedPreferences(pref_name, 0);
        if(sharedPreferences.getInt("economicsSwitchValue", 1) == 1)
        {
            from = new String[] {"en_word"};
        }
        else
        {
            from = new String[] {"dzo_word"};
        }
        to = new int[] {R.id.suggestion_text};


        suggestionAdapter = new SimpleCursorAdapter(EconomicsActivity.this,
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
                //sharedPreferences = getApplicationContext().getSharedPreferences(pref_name, 0);
                if(sharedPreferences.getInt("economicsSwitchValue", 1) == 1)
                {
                    CursorAdapter ca = search.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
                    String clicked_word =  cursor.getString(cursor.getColumnIndex("en_word"));
                    search.setQuery(clicked_word,false);
                    //search.setQuery("",false);
                    search.clearFocus();
                    search.setFocusable(false);
                    Intent intent = new Intent(EconomicsActivity.this, WordMeaningActivityEconomics.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",clicked_word);
                    bundle.putInt("key", 1);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {
                    CursorAdapter ca = search.getSuggestionsAdapter();
                    Cursor cursor = ca.getCursor();
                    cursor.moveToPosition(position);
                    String clicked_word =  cursor.getString(cursor.getColumnIndex("dzo_word"));
                    search.setQuery(clicked_word,false);
                    //search.setQuery("",false);
                    search.clearFocus();
                    search.setFocusable(false);
                    Intent intent = new Intent(EconomicsActivity.this, WordMeaningActivityEconomics.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("dzo_word",clicked_word);
                    bundle.putInt("key", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }


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
                    if(sharedPreferences.getInt("economicsSwitchValue", 1) == 1){
                        search.setQuery("",false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(EconomicsActivity.this, R.style.MyDialogTheme);
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
                        search.setQuery("",false);
                        AlertDialog.Builder builder = new AlertDialog.Builder(EconomicsActivity.this, R.style.MyDialogTheme);
                        builder.setTitle("ཚིག་འདི་འཚོལ་མ་འཐོབ།\nལོག་ཏེ་འཚོལ་གནང།");

                        String positiveText = getString(R.string.okdzo);
                        builder.setPositiveButton(positiveText,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // positive button logic
                                    }
                                });

                        String negativeText = getString(R.string.canceldzo);
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

                }

                else
                {
                    //search.setQuery("",false);
                    search.clearFocus();
                    search.setFocusable(false);
                    Intent intent = new Intent(EconomicsActivity.this, WordMeaningActivityEconomics.class);
                    Bundle bundle = new Bundle();
                    //sharedPreferences = getApplicationContext().getSharedPreferences(pref_name, 0);
                    if(sharedPreferences.getInt("economicsSwitchValue", 1) == 1)
                    {
                        bundle.putString("en_word",text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else
                    {
                        bundle.putString("dzo_word",text);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

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
    @Override
    protected void onRestart(){
        super.onRestart();
        if(MainActivity.isQuit)
            System.exit(1);
    }
}