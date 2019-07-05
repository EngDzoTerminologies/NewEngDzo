package ddc.dict;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ddc.dict.R;

import java.util.ArrayList;


public class RecyclerViewAdapterBookmark extends RecyclerView.Adapter<RecyclerViewAdapterBookmark.BookmarkViewHolder> {

    private ArrayList<Bookmark> bookmarks;
    private Context context;
    private DatabaseHelper databaseHelper;
    public boolean databaseOpened = false;


    public RecyclerViewAdapterBookmark(Context context, ArrayList<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
        this.context = context;

    }


    public class BookmarkViewHolder extends RecyclerView.ViewHolder {
        TextView enWord;
        TextView dzoWord;
        TextView Category;
        ImageButton button;



        public BookmarkViewHolder(View v) {
            super(v);
           enWord = v.findViewById(R.id.eng_word);
           dzoWord = v.findViewById(R.id.dzog_word);
           Category = v.findViewById(R.id.categoryID);
           button = v.findViewById(R.id.deleteID);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    databaseHelper = new DatabaseHelper(context);
                    if (databaseHelper.checkDataBase()) {
                        openDatabase();
                        Log.wtf("database", "opened Main database");
                    } else {
                        LoadDatabaseAsyncWordDay task = new LoadDatabaseAsyncWordDay(context);
                        task.execute();
                    }

                    int position = getAdapterPosition();
                    String text = bookmarks.get(position).get_en_word();
                    databaseHelper.deleteSingleBookmark(text);
                    Toast.makeText(context, text + " deleted", Toast.LENGTH_LONG).show();
                    bookmarks.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, bookmarks.size());


                }
            });


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String text = bookmarks.get(position).get_en_word();
                    String category = bookmarks.get(position).get_category();
                    if(category.equals("computer"))
                    {
                        Intent intent = new Intent(context, WordMeaningActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(category.equals("economics"))
                    {
                        Intent intent = new Intent(context, WordMeaningActivityEconomics.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(category.equals("financial"))
                    {
                        Intent intent = new Intent(context, WordMeaningActivityFinancial.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(category.equals("health"))
                    {
                        Intent intent = new Intent(context, WordMeaningActivityHealth.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    if(category.equals("political"))
                    {
                        Intent intent = new Intent(context, WordMeaningActivityPolitical.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("key", 1);
                        bundle.putString("en_word", text);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                }
            });





        }


    }

    public void openDatabase()
    {
        try {
            databaseHelper.openDataBase();
            databaseOpened=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    @Override
    public BookmarkViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_layout, parent, false);
        return new BookmarkViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BookmarkViewHolder holder, final int position) {
        holder.enWord.setText(bookmarks.get(position).get_en_word());
        holder.dzoWord.setText(bookmarks.get(position).get_dzo_word());
        holder.Category.setText(bookmarks.get(position).get_category());

    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }






}
