package com.riad.dictionary.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riad.dictionary.R;
import com.riad.dictionary.WordMeaningActivity;
import com.riad.dictionary.WordMeaningActivityPolitical;

public class FragmentDzoDefinition extends Fragment {
    public FragmentDzoDefinition() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition,container, false);//Inflate Layout

        Context context=getActivity();

        TextView text = (TextView) view.findViewById(R.id.textViewD);//Find textView Id
        TextView textView=(TextView) view.findViewById(R.id.textViewCatogory);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageViews);




        String dzoDefinition= ((WordMeaningActivity)context).dzoDefinition;
        String Category=((WordMeaningActivity)context).category;
        String Images=((WordMeaningActivity)context).images;

        text.setText(dzoDefinition);


        if(dzoDefinition==null)
        {
            text.setText("No Example found");
        }
        textView.setText(Category);

        textView.setText(Category);

        if(Category==null)
        {
            textView.setText("No category found");
        }
        else{
            textView.setText("Category: "+ Category);
        }

        //To retrieve images from database

        Context context1=imageView.getContext();
        int resID=context1.getResources().getIdentifier(Images,"drawable",context1.getPackageName());
        if(resID==0){
            //The associated resource identifier.Return 0 if no such resources wa found. (0 is not a resource ID).
           imageView.setImageResource(0);
        }else{
            imageView.setImageResource(resID);
        }


        return view;
    }
}
