package ddc.dict.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ddc.dict.R;
import ddc.dict.WordMeaningActivity;

public class FragmentSpeech extends Fragment {
    public FragmentSpeech() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewC);//Find textView Id
        String speech= ((WordMeaningActivity)context).speech;


        if(speech!=null)
        {
            speech = speech.replaceAll(",", ",\n");
            text.setText(speech);
        }
        if(speech==null)
        {
            text.setText("ཚིག་སྡེ་འཚོལ་མ་ཐོབ། (No Parts of speech found)");
        }

        return view;
    }
}
