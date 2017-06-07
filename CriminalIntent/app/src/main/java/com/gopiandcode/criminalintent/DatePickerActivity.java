package com.gopiandcode.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by gopia on 07/06/2017.
 */

public class DatePickerActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DatePickerFragment();
    }
}
