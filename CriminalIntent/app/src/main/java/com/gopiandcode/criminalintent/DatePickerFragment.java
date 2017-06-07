package com.gopiandcode.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gopia on 05/06/2017.
 */

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private Button mButton;
    private static final String EXTRA_DATE =
            "com.gopiandcode.criminalintent.date";

    public static Date getDate(Intent intent) {
        return (Date) intent.getSerializableExtra(EXTRA_DATE);
    }


    private void sendResult(int resultCode, Date date) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        if (getTargetFragment() == null) {
            getActivity().setResult(resultCode, intent);
        } else {
            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

        }
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    public static Intent newIntent(Context packageContext, Date date){
        Intent intent = new Intent(packageContext, DatePickerActivity.class);
        intent.putExtra(ARG_DATE, date);

        return intent;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        long timeinmillis = date.getTime();
        long timeinsecs   = timeinmillis/1000;
        long timeinmins   = timeinsecs/60;
        long timeinhours  = timeinmins/60;

        long current_hour = timeinhours % 24;
        long current_mins = timeinmins % 60;
        long current_secs = timeinsecs % 60;

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
        mTimePicker.setHour((int) current_hour);
        mTimePicker.setMinute((int) current_mins);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();

                        Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })

                .create();
    }



    // For using the dialog as a fullscreen view
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_date, container, false);
        Date date = (Date) getActivity().getIntent().getSerializableExtra(ARG_DATE);

        if(date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            long timeinmillis = date.getTime();
            long timeinsecs = timeinmillis / 1000;
            long timeinmins = timeinsecs / 60;
            long timeinhours = timeinmins / 60;

            long current_hour = timeinhours % 24;
            long current_mins = timeinmins % 60;
            long current_secs = timeinsecs % 60;

            mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
            mDatePicker.init(year, month, day, null);
            mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);
            mTimePicker.setHour((int) current_hour);
            mTimePicker.setMinute((int) current_mins);

            mButton = (Button) new Button(getActivity());
            mButton.setText("Okay");
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int day = mDatePicker.getDayOfMonth();

                    int hour = mTimePicker.getHour();
                    int minute = mTimePicker.getMinute();

                    Date date = new GregorianCalendar(year, month, day, hour, minute).getTime();
                    sendResult(Activity.RESULT_OK, date);
                }
            });
            LinearLayout main = (LinearLayout) v.findViewById(R.id.main_layout);
            main.addView(mButton);
        }
        return v;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
