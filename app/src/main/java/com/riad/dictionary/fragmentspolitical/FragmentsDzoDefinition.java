package com.riad.dictionary.fragmentspolitical;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riad.dictionary.R;
import com.riad.dictionary.WordMeaningActivityPolitical;

public class FragmentsDzoDefinition extends Fragment {
    public FragmentsDzoDefinition() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition_political,container, false);//Inflate Layout

        Context context=getActivity();

        TextView text = (TextView) view.findViewById(R.id.textViewD);//Find textView Id
        TextView textView=(TextView) view.findViewById(R.id.textViewCatogory);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageViews);




        String dzoDefinition= ((WordMeaningActivityPolitical)context).dzoDefinition_political;
      //  String Category=((WordMeaningActivityPolitical)context).category;
        String Images=((WordMeaningActivityPolitical)context).images;

     //   String dzoDefPolitical=((WordMeaningActivityPolitical)context).dzoDefinition;

        text.setText(dzoDefinition);
       // text.setText(dzoDefPolitical);


        if(dzoDefinition==null)
        {
            text.setText("No Example found");
        }
       // textView.setText(Category);

       /* if(dzoDefPolitical==null)
        {
            text.setText("No Example found");
        }*/
       /* textView.setText(Category);

        if(Category==null)
        {
            textView.setText("No category found");
        }
        else{
            textView.setText("Category: "+ Category);
        }
*/
        //To retrieve images from database

    /*    Context context1=imageView.getContext();
        int resID=context1.getResources().getIdentifier(Images,"drawable",context1.getPackageName());
        if(resID==0){
            //The associated resource identifier.Return 0 if no such resources wa found. (0 is not a resource ID).
           imageView.setImageResource(0);
        }else{
            imageView.setImageResource(resID);
        }*/


        return view;
    }
}
