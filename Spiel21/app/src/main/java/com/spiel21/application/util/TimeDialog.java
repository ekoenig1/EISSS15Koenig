package com.spiel21.application.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


// TIME-Klasse mit dem zusammenbau des Kalenders
@SuppressLint("ValidFragment")
public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public int id;

    public TimeDialog(int id) {
        this.id = id;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // erstellt eine neue Instanz des TimePickerDialog und senden es
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        populateSetTime(hour, minute);
    }

    // auswahl der Zeit im EditText
    public void populateSetTime(int stunde, int minute) {
        EditText editTextDate = (EditText) getActivity().findViewById(id);
        editTextDate.setText(stunde + ":" + minute);
    }
}
