package com.belcin.mekarski.tipcalculator;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.RatingBar;
import android.widget.Button;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.Context;
import android.app.Application;
import android.view.View.OnTouchListener;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.widget.ViewSwitcher;
import android.widget.RatingBar.OnRatingBarChangeListener;





/**
 * Created by Chris on 2015-01-27.
 */
public class FragmentMain extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private EditText billEditText, tipEditText, splitEditText;
    private TextView billTextView, tipTextView, totalTextView, tipPerPerson, totalPerPerson;
    private SeekBar tipSeekBar, splitSeekBar;
    private RatingBar tipRatingBar;
    private Button suggestButton;
    private ViewSwitcher viewSwitcher;
    View myFirstView, mySecondView;


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
        //create view
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        myFirstView= rootView.findViewById(R.id.mainView);
        mySecondView = rootView.findViewById(R.id.ratingView);

        // Try to get prefs (doesn't work)
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        //sharedPref.registerOnSharedPreferenceChangeListener(getActivity());

        double defaultTip = 15;
        try {
            //defaultTip = Integer.parseInt((prefs.getString("example_text")));
            defaultTip = sharedPref.getInt(getString(R.xml.pref_general), 15);
        } catch (Exception e) {
            //
        }

        viewSwitcher = (ViewSwitcher)rootView.findViewById(R.id.ViewSwitcher);

        //bill
        billEditText=(EditText)rootView.findViewById(R.id.billEditText);
        billTextView=(TextView)rootView.findViewById(R.id.billAmount);
        //tip
        tipEditText=(EditText)rootView.findViewById(R.id.tipEditText);
        tipSeekBar=(SeekBar)rootView.findViewById(R.id.tipSeekBar);
        tipTextView=(TextView)rootView.findViewById(R.id.tipAmount);
        tipRatingBar=(RatingBar)rootView.findViewById(R.id.ratingBar);
        suggestButton=(Button)rootView.findViewById(R.id.suggestButton);

        //split
        splitEditText=(EditText)rootView.findViewById(R.id.splitEditText);
        splitSeekBar=(SeekBar)rootView.findViewById(R.id.splitSeekBar);

        //summary
        totalTextView=(TextView)rootView.findViewById(R.id.totalAmount);
        tipPerPerson=(TextView)rootView.findViewById(R.id.tipPerPerson);
        totalPerPerson=(TextView)rootView.findViewById(R.id.totalPerPerson);

        // Initial values
        tipEditText.setText(Double.toString(defaultTip)); // Initial tip value
        tipSeekBar.setProgress((int)defaultTip);
        splitEditText.setText(Integer.toString(1)); // Initial split value
        //init summary with zeros
        billTextView.setText(String.valueOf(0));
        tipTextView.setText(String.valueOf(0));
        totalTextView.setText(String.valueOf(0));
        tipPerPerson.setText(String.valueOf(0));
        totalPerPerson.setText(String.valueOf(0));

        // Attach TextWatcher to EditText
        billEditText.addTextChangedListener(mTextEditorWatcher);
        tipEditText.addTextChangedListener(mTextEditorWatcher);
        splitEditText.addTextChangedListener(mTextEditorWatcher);
        tipSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tipEditText.setText(Integer.toString(progress));
            }
        });

        splitSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                splitEditText.setText(Integer.toString(progress+1));
            }
        });

        suggestButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (viewSwitcher.getCurrentView() != myFirstView){

                    viewSwitcher.showPrevious();
                } else if (viewSwitcher.getCurrentView() != mySecondView){

                    viewSwitcher.showNext();
                }
            }
        });


        tipRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                tipEditText.setText(Float.toString(10+(rating*2)));
                tipSeekBar.setProgress(10+((int)rating*2));
                viewSwitcher.showPrevious();
            }
        });


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
            // When Nothing Entered
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            //Stuff
        }
        @Override
        public void afterTextChanged(Editable s)
        {
            double billValue = 0;
            int tipValue = 0;
            int splitValue = 0;
            try {
                billValue = Double.parseDouble(billEditText.getText().toString());
            } catch (Exception e) {
                //
            }
            try {
                tipValue = Integer.parseInt(tipEditText.getText().toString());
            } catch (Exception e) {
                //
            }
            try {
                splitValue = Integer.parseInt(splitEditText.getText().toString());
            } catch (Exception e) {
                //
            }
            double theTotal = Math.round((billValue+(billValue*((double)tipValue*Math.pow(10,-2))))*100.0)/100.0;
            double theTip =  Math.round((billValue*((double)tipValue*Math.pow(10,-2)))*100.0)/100.0;
            double theTipPerPerson = Math.round((theTip/(double)splitValue)*100.0)/100.0;
            double theTotalPerPerson = Math.round((theTotal/(double)splitValue)*100.0)/100.0;
            billTextView.setText(String.valueOf(billValue));
            tipTextView.setText(String.valueOf(theTip));
            totalTextView.setText(String.valueOf(theTotal));
            tipPerPerson.setText(String.valueOf(theTipPerPerson));
            totalPerPerson.setText(String.valueOf(theTotalPerPerson));
            tipSeekBar.setProgress(tipValue);
        }
    };

}
