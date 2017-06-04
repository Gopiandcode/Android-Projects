package com.gopiandcode.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by gopia on 03/06/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
