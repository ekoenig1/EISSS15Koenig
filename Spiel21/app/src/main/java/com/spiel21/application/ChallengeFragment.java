package com.spiel21.application;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;


public class ChallengeFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    Context context;

    public ChallengeFragment(Context context) {
        this.context = context;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Toast.makeText(context, "Date: " + monthOfYear + " / " + dayOfMonth + " / " + year, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ChallengeFragment dateSetting = new ChallengeFragment(getActivity());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;
        //dialog = new ChallengeFragment(getActivity(), dateSetting, year, month, day);

        //return dialog;
        return null;
    }

    public void setDate(View view) {
        //PickerDialog
    }

    //public static ChallengeFragment newInstance(String param1, String param2) {
        //ChallengeFragment fragment = new ChallengeFragment();
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        //return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_challenge, container, false);

        final DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.datePicker);
        final Button button = (Button) rootView.findViewById(R.id.button_picker);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), datePicker.getDayOfMonth() + " " + datePicker.getMonth() + " " + datePicker.getYear(), Toast.LENGTH_SHORT).show();

            }
        });


        return rootView;
    }


}
