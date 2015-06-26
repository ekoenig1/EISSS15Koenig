package com.spiel21.application.util;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

// DATE-Klasse mit dem zusammenbau des Kalenders
@SuppressLint("ValidFragment")
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public int id;

    public DateDialog(int id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // erstellt eine neue Instanz des DatePickerDialog und senden es
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        populateSetDate(year, month + 1, day);
    }

    // auswahl des Datums im EditText
    public void populateSetDate(int jahr, int monat, int tag) {
        EditText editTextDate = (EditText) getActivity().findViewById(id);
        editTextDate.setText(tag + "." + monat + "." + jahr);
    }
}