package com.riad.dictionary;

import android.database.Cursor;
import android.database.SQLException;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.riad.dictionary.fragments.FragmentAcronym;
import com.riad.dictionary.fragments.FragmentDzoDefinition;
import com.riad.dictionary.fragments.FragmentSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WordMeaningActivity extends AppCompatActivity {

    private ViewPager viewPager;

    String enWord;
    DatabaseHelper myDbHelper;
    Cursor c = null;
    String dzoWord;


    //public String enDefinition;
    public String dzoDefinition;
    public String speech;
    public String acronym;
    public String category;
    public String images;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);

        //received values
        Bundle bundle = getIntent().getExtras();
        enWord= bundle.getString("en_word");

       /* Bundle bundle1=getIntent().getExtras();
        dzoWord=bundle1.getString("dzo_word");*/



        myDbHelper = new DatabaseHelper(this);

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

      //  d = myDbHelper.getMeaning(dzoWord);
        c = myDbHelper.getMeaning(enWord);

        if (c.moveToFirst()) {
            dzoWord=c.getString(c.getColumnIndex("dzo_word"));
            //enDefinition= c.getString(c.getColumnIndex("en_definition"));
            speech=c.getString(c.getColumnIndex("speech"));
            acronym=c.getString(c.getColumnIndex("acronym"));
            dzoDefinition=c.getString(c.getColumnIndex("dzo_definition"));
           // category=c.getString(c.getColumnIndex("category"));

            images=c.getString(c.getColumnIndex("images"));



        }
   /*     if (d.moveToFirst()) {

            enDefinition= d.getString(d.getColumnIndex("en_definition"));
            speech=d.getString(d.getColumnIndex("speech"));
            acronym=d.getString(d.getColumnIndex("acronym"));
            dzoDefinition=d.getString(d.getColumnIndex("dzo_definition"));

        }*/

     //   myDbHelper.insertHistory(dzoWord);
        myDbHelper.insertHistory(enWord);


        ImageButton btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        //For the speaker/voice play
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts = new TextToSpeech(WordMeaningActivity.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        // TODO Auto-generated method stub
                        if(status == TextToSpeech.SUCCESS){
                            int result=tts.setLanguage(Locale.getDefault());
                            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                                Log.e("error", "This Language is not supported");
                            }
                            else{
                                tts.speak(enWord, TextToSpeech.QUEUE_FLUSH, null);
                            }
                        }
                        else
                            Log.e("error", "Initialization Failed!");
                    }
                });
            }
        });



        TextView toolbar = (TextView) findViewById(R.id.mToolbar);
        toolbar.setText(dzoWord);

       // getSupportActionBar().setTitle(dzoWord);


       // toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.oToolbar);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle(enWord);

        //back arrow button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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
        adapter.addFrag(new FragmentDzoDefinition(), "Definition");
        adapter.addFrag(new FragmentAcronym(), "Acronym");
        adapter.addFrag(new FragmentSpeech(), "Speech");



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
}
