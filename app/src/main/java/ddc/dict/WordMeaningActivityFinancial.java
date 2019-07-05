package ddc.dict;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ddc.dict.R;

import ddc.dict.fragmentsFinancial.FragmentAcronym;
import ddc.dict.fragmentsFinancial.FragmentDzoDefinition;
import ddc.dict.fragmentsFinancial.FragmentSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaningActivityFinancial extends AppCompatActivity {

    private ViewPager viewPager;

    String enWord;
    DatabaseHelperFinancial myDbHelper;
    Cursor c = null;
    String dzoWord;


    //public String enDefinition;
    public String dzoDefinition;
    public String speech;
    public String acronym;
    public String cat;
    public String images;
    public String dzo;
    public String eng;
    public int bundleValue;

    ImageButton btnBookmark;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning_financial);

        //received values
        Bundle bundle = getIntent().getExtras();
        bundleValue = bundle.getInt("key");
     //   SharedPreferences sharedPreferences = getApplication().getSharedPreferences("financialpreference", 0);
        if(bundle.getInt("key") == 1)
        {
            enWord= bundle.getString("en_word");
        }
        else
        {
            dzoWord= bundle.getString("dzo_word");
        }
       /* Bundle bundle1=getIntent().getExtras();
        dzoWord=bundle1.getString("dzo_word");*/

        myDbHelper = new DatabaseHelperFinancial(this);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        //  d = myDbHelper.getMeaning(dzoWord);

        if(bundle.getInt("key") == 1)
        {
            c = myDbHelper.getMeaning(enWord);
        }
        else
        {
            c = myDbHelper.getMeaning(dzoWord);
        }

        if (c.moveToFirst()) {
            eng=c.getString(c.getColumnIndex("en_word"));
            dzo=c.getString(c.getColumnIndex("dzo_word"));
            speech=c.getString(c.getColumnIndex("speech"));
            acronym=c.getString(c.getColumnIndex("acronym"));
            dzoDefinition=c.getString(c.getColumnIndex("dzo_definition"));
            images=c.getString(c.getColumnIndex("image"));
            cat=c.getString(c.getColumnIndex("category"));
        }

     //   myDbHelper.insertHistory(dzoWord);
        //myDbHelper.insertHistory(enWord);

        btnBookmark=findViewById(R.id.btnfav);
        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        //for bookmark
        int isMark = eng == null? 0:1;

        btnBookmark.setTag(isMark);

        int icon = eng ==null? R.drawable.ic_bookmark:R.drawable.ic_bookmark_border;
        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i= (int) btnBookmark.getTag();

                if(i==0){
                    btnBookmark.setImageResource(R.drawable.ic_bookmark);
                    btnBookmark.setTag(1);
                    myDbHelper.insertBookmark(eng,dzo,cat);

                    // Log.e("eng", eng);
                    //myDbHelper.addBookmark(value);
                }else if(i ==1){
                    btnBookmark.setImageResource(R.drawable.ic_bookmark_border);
                    btnBookmark.setTag(0);
                    // myDbHelper.removeBookmark(value);
                    //myDbHelper.deleteBookmark(eng);

                }


            }
        });

        //For the speaker/voice play
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaningActivityFinancial.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=tts.setLanguage(Locale.getDefault());
                            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                tts.speak(eng, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        else
                            Log.e("error", "Initialization Failed!");
                    }
                });
            }
        });



        Toolbar toolbar2 = findViewById(R.id.nToolbar);
        setSupportActionBar(toolbar2);

        TextView toolbartext = findViewById(R.id.mToolbar);

        //back arrow button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(bundle.getInt("key") == 1)
        {
            toolbartext.setText(eng);
        }
        else
        {
            toolbartext.setText(dzo);
        }



        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);

        if (viewPager != null){
            setupViewPager(viewPager);
        }

        //tabLayout

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }


    }


    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
       // adapter.addFrag(new FragmentDefinition(), "Definition");
        adapter.addFrag(new FragmentDzoDefinition(), "(དོན་དག)\nDefinition");
        adapter.addFrag(new FragmentAcronym(), "(བསྡུ་ཡིག)\nAcronym");
        adapter.addFrag(new FragmentSpeech(), "(ཚིག་སྡེ)\nPOS");



        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        if(MainActivity.isQuit)
            finish();
    }
}
