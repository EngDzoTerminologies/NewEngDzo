package ddc.dict.FragmentsEconomics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ddc.dict.R;

import ddc.dict.WordMeaningActivityEconomics;


public class FragmentDzoDefinition extends Fragment {
    public FragmentDzoDefinition() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition_econo,container, false);//Inflate Layout



        Context context=getActivity();

        TextView text = (TextView) view.findViewById(R.id.textViewD);//Find textView Id
        TextView equiText = view.findViewById(R.id.equiText);
      //  TextView textView=(TextView) view.findViewById(R.id.textViewCatogory);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageViews);




        String dzoDefinition= ((WordMeaningActivityEconomics)context).dzoDefinition;
        String dzo = ((WordMeaningActivityEconomics)context).dzo;
        String eng = ((WordMeaningActivityEconomics)context).eng;
        int value = ((WordMeaningActivityEconomics)context).bundlevalue;
       // String Category=((WordMeaningActivityEconomics)context).category;
        String Images=((WordMeaningActivityEconomics)context).images;


        if(value == 1)
        {
            equiText.setText(dzo);
        }
        else
        {
            equiText.setText(eng);
        }

        text.setText(dzoDefinition);


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
