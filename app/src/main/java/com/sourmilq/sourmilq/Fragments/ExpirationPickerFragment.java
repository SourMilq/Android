package com.sourmilq.sourmilq.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.sourmilq.sourmilq.DataModel.Item;
import com.sourmilq.sourmilq.DataModel.Model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Philip on 2016-11-21.
 */

public class ExpirationPickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Item item;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        SETTING_INITIAL_DATE:
        {
            Bundle args = getArguments();
            if (args == null) break SETTING_INITIAL_DATE;
            item = (Item) args.getSerializable("item");
            if (item == null) break SETTING_INITIAL_DATE;
            Calendar expiration = item.getExpiration();
            if (expiration == null) break SETTING_INITIAL_DATE;
            year = expiration.get(Calendar.YEAR);
            month = expiration.get(Calendar.MONTH);
            day = expiration.get(Calendar.DAY_OF_MONTH);
        }

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar date = new GregorianCalendar();
        date.set(year, monthOfYear, dayOfMonth, 12, 0, 0);

        Model model = Model.getInstance(getActivity().getApplicationContext());
        model.setExpiration(item, date);
    }
}
