package com.riad.dictionary.fragmentspolitical;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riad.dictionary.R;
import com.riad.dictionary.WordMeaningActivity;
import com.riad.dictionary.WordMeaningActivityPolitical;

public class FragmentsAcronym extends Fragment {
    public FragmentsAcronym() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition_political,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);//Find textView Id
        String acronym= ((WordMeaningActivityPolitical)context).acronym_political;
       // String acronympol= ((WordMeaningActivityPolitical)context).acronym;
        if(acronym!=null)
        {
            acronym = acronym.replaceAll(",", ",\n");
            text.setText(acronym);
        }
        if(acronym==null)
        {
            text.setText("No antonyms found");
        }

        return view;
    }
}
