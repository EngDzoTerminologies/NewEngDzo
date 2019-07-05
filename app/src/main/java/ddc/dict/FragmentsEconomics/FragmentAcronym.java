package ddc.dict.FragmentsEconomics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ddc.dict.R;
import ddc.dict.WordMeaningActivityEconomics;


public class FragmentAcronym extends Fragment {
    public FragmentAcronym() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_definition_econo,container, false);//Inflate Layout

        Context context=getActivity();
        TextView text = (TextView) view.findViewById(R.id.textViewD);//Find textView Id
        String acronym= ((WordMeaningActivityEconomics)context).acronym;

        if(acronym!=null)
        {
            acronym = acronym.replaceAll(",", ",\n");
            text.setText(acronym);
        }
        if(acronym==null)
        {
            text.setText("བསྡུ་ཡིག་འཚོལ་མ་ཐོབ། (No Acronym found)");
        }

        return view;
    }
}
