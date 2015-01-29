package com.belcin.mekarski.tipcalculator;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Chris on 2015-01-27.
 */
public class FragmentMain extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    EditText editTextBill, editTextTip, editTextSplit;
    TextView textViewBill;

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public FragmentMain newInstance(int sectionNumber) {
        FragmentMain fragment = new FragmentMain();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMain() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        editTextBill=(EditText)rootView.findViewById(R.id.billValue);
        textViewBill=(TextView)rootView.findViewById(R.id.textViewBill);

        // Attach TextWatcher to EditText
        editTextBill.addTextChangedListener(mTextEditorWatcher);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private TextWatcher mTextEditorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            // When No Password Entered
            textViewBill.setText("Not Entered");
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            //Stuff
        }
        @Override
        public void afterTextChanged(Editable s)
        {
            textViewBill.setText(s.toString());
        }
    };

}
