package ddc.dict.fragmentspolitical;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ddc.dict.R;

import ddc.dict.WordMeaningActivityPolitical;

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
        TextView text = (TextView) view.findViewById(R.id.textViewP);//Find textView Id
        String acronym= ((WordMeaningActivityPolitical)context).acronym_political;
       // String acronympol= ((WordMeaningActivityPolitical)context).acronym;

        text.setText(acronym);
        if(acronym.equals(""))
        {
            text.setText("བསྡུ་ཡིག་འཚོལ་མ་ཐོབ། (No Acronym found)");
        }

        return view;
    }
}
